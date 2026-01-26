package com.partharoypc.smartads;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.RequiresPermission;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.partharoypc.smartads.listeners.AppOpenAdListener;
import com.partharoypc.smartads.listeners.BannerAdListener;
import com.partharoypc.smartads.listeners.InterstitialAdListener;
import com.partharoypc.smartads.listeners.NativeAdListener;
import com.partharoypc.smartads.listeners.RewardedAdListener;
import com.partharoypc.smartads.managers.AppOpenAdManager;
import com.partharoypc.smartads.managers.BannerAdManager;
import com.partharoypc.smartads.managers.InterstitialAdManager;
import com.partharoypc.smartads.managers.NativeAdManager;
import com.partharoypc.smartads.managers.RewardedAdManager;

public class SmartAds {

    private static volatile SmartAds instance;
    private SmartAdsConfig config;
    private boolean adsEnabled;
    private AppOpenAdManager appOpenAdManager;
    private InterstitialAdManager interstitialAdManager;
    private RewardedAdManager rewardedAdManager;

    private BannerAdManager bannerAdManager;
    private NativeAdManager nativeAdManager;

    private Application application;

    private SmartAds() {
    }

    private void requestUmpWithActivity(Application application) {
        ConsentRequestParameters params = new ConsentRequestParameters.Builder().build();
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(application);
        consentInformation.requestConsentInfoUpdate(
                appOpenAdManager != null ? appOpenAdManager.getCurrentActivityForUmp() : null,
                params,
                () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            appOpenAdManager != null ? appOpenAdManager.getCurrentActivityForUmp() : null,
                            formError -> initializeSdks(application));
                },
                requestConsentError -> initializeSdks(application));
    }

    private void initializeManagersIfNeeded(Application application) {
        if (appOpenAdManager == null) {
            appOpenAdManager = new AppOpenAdManager(application);
        }
        if (interstitialAdManager == null) {
            interstitialAdManager = new InterstitialAdManager();
        }
        if (rewardedAdManager == null) {
            rewardedAdManager = new RewardedAdManager();
        }
        if (bannerAdManager == null) {
            bannerAdManager = new BannerAdManager();
        }
        if (nativeAdManager == null) {
            nativeAdManager = new NativeAdManager();
        }
    }

    public static SmartAds getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "SmartAds.initialize() must be called in your Application class before use.");
        }
        return instance;
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public static void initialize(Application application, SmartAdsConfig config) {
        if (instance == null) {
            synchronized (SmartAds.class) {
                if (instance == null) {
                    instance = new SmartAds();
                    instance.application = application;
                    instance.config = config;
                    instance.adsEnabled = config.isAdsEnabled();

                    RequestConfiguration.Builder rcBuilder = new RequestConfiguration.Builder();

                    if (config.getTestDeviceIds() != null && !config.getTestDeviceIds().isEmpty()) {
                        rcBuilder.setTestDeviceIds(config.getTestDeviceIds());
                    }
                    if (config.getMaxAdContentRating() != null) {
                        rcBuilder.setMaxAdContentRating(config.getMaxAdContentRating());
                    }
                    if (config
                            .getTagForChildDirectedTreatment() != RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_UNSPECIFIED) {
                        rcBuilder.setTagForChildDirectedTreatment(config.getTagForChildDirectedTreatment());
                    }
                    if (config
                            .getTagForUnderAgeOfConsent() != RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_UNSPECIFIED) {
                        rcBuilder.setTagForUnderAgeOfConsent(config.getTagForUnderAgeOfConsent());
                    }

                    MobileAds.setRequestConfiguration(rcBuilder.build());

                    // UMP Consent (optional)
                    if (config.useUmpConsent()) {
                        instance.initializeManagersIfNeeded(application);
                        instance.requestUmpWithActivity(application);
                    } else {
                        instance.initializeManagersIfNeeded(application);
                        instance.initializeSdks(application);
                    }

                }
            }
        }
    }

    public void launchAdInspector(Activity activity) {
        MobileAds.openAdInspector(activity, error -> {
            /* Ad inspector closed or error */
        });
    }

    public boolean isPrivacyOptionsRequired() {
        return UserMessagingPlatform.getConsentInformation(application)
                .getPrivacyOptionsRequirementStatus() == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED;
    }

    public void preloadAds(Context context) {
        if (!canShowAds())
            return;

        if (appOpenAdManager != null && config.isAppOpenConfigured()) {
            appOpenAdManager.fetchAd();
        }
        if (interstitialAdManager != null && config.isInterstitialConfigured()) {
            interstitialAdManager.loadAd(context, config);
        }
        if (rewardedAdManager != null && config.isRewardedConfigured()) {
            rewardedAdManager.loadAd(context, config);
        }
    }

    private void initializeSdks(Application application) {
        MobileAds.initialize(application, initializationStatus -> {
            try {
                if (appOpenAdManager != null) {
                    appOpenAdManager.fetchAd();
                }
            } catch (Exception e) {
                /* Pre-fetching failed */
            }
        });

        initializeManagersIfNeeded(application);
    }

    public void showPrivacyOptionsForm(Activity activity) {
        try {
            UserMessagingPlatform.showPrivacyOptionsForm(activity, formError -> {
                /* no-op */ });
        } catch (Exception ignored) {
        }
    }

    // --- App Open Ad (Manual) ---
    public void showAppOpenAd(Activity activity) {
        if (canShowAds() && appOpenAdManager != null) {
            appOpenAdManager.showAdIfAvailable();
        }
    }

    public AdStatus getAppOpenAdStatus() {
        if (appOpenAdManager != null) {
            return appOpenAdManager.getAdStatus();
        }
        return AdStatus.IDLE;
    }

    public void setAdsEnabled(boolean enabled) {
        this.adsEnabled = enabled;
    }

    public boolean areAdsEnabled() {
        return this.adsEnabled;
    }

    public void setAppOpenAdListener(AppOpenAdListener listener) {
        if (appOpenAdManager != null) {
            appOpenAdManager.setListener(listener);
        }
    }

    public void updateConfig(SmartAdsConfig newConfig) {
        this.config = newConfig;
        this.adsEnabled = newConfig.isAdsEnabled();
        // Configuration updated. Managers will use new IDs on next load.
    }

    public SmartAdsConfig getConfig() {
        return config;
    }

    public boolean canShowAds() {
        return this.adsEnabled && config != null && config.isAnyAdConfigured();
    }

    // --- Interstitial Ads ---
    public void loadInterstitialAd(Context context) {
        if (canShowAds()) {
            interstitialAdManager.loadAd(context, config);
        }
    }

    public void showInterstitialAd(Activity activity, InterstitialAdListener listener) {
        if (canShowAds() && config.isInterstitialConfigured()) {
            interstitialAdManager.showAd(activity, listener);
        } else {
            if (listener != null)
                listener.onAdFailedToShow("Ad condition not met or ad unit not configured.");
        }
    }

    public AdStatus getInterstitialAdStatus() {
        return interstitialAdManager.getAdStatus();
    }

    // --- Rewarded Ads ---
    public void loadRewardedAd(Context context) {
        if (canShowAds()) {
            rewardedAdManager.loadAd(context, config);
        }
    }

    public void showRewardedAd(Activity activity, RewardedAdListener listener) {
        if (canShowAds() && config.isRewardedConfigured()) {
            rewardedAdManager.showAd(activity, listener);
        } else {
            if (listener != null)
                listener.onAdFailedToShow("Ad condition not met or ad unit not configured.");
        }
    }

    public AdStatus getRewardedAdStatus() {
        return rewardedAdManager.getAdStatus();
    }

    public boolean isAnyAdShowing() {
        boolean interShowing = interstitialAdManager != null && interstitialAdManager.getAdStatus() == AdStatus.SHOWN;
        boolean rewardedShowing = rewardedAdManager != null && rewardedAdManager.getAdStatus() == AdStatus.SHOWN;
        boolean appOpenShowing = appOpenAdManager != null && appOpenAdManager.getAdStatus() == AdStatus.SHOWN;
        return interShowing || rewardedShowing || appOpenShowing;
    }

    // --- Banner Ads ---
    @RequiresPermission(Manifest.permission.INTERNET)
    public void showBannerAd(Activity activity, FrameLayout adContainer, BannerAdListener listener) {
        if (canShowAds() && config.isBannerConfigured()) {
            bannerAdManager.loadAndShowAd(activity, adContainer, config, listener);
        } else {
            if (listener != null)
                listener.onAdFailed("Ad condition not met or ad unit not configured.");
        }
    }

    // --- Native Ads ---
    public void showNativeAd(Activity activity, FrameLayout adContainer, @LayoutRes int layoutRes,
            NativeAdListener listener) {
        if (canShowAds() && config.isNativeConfigured()) {
            nativeAdManager.loadAndShowAd(activity, adContainer, layoutRes, config, listener);
        } else {
            if (listener != null)
                listener.onAdFailed("Ad condition not met or ad unit not configured.");
        }
    }

    public void showNativeAd(Activity activity, FrameLayout adContainer, NativeAdSize size, NativeAdListener listener) {
        if (canShowAds() && config.isNativeConfigured()) {
            nativeAdManager.loadAndShowAd(activity, adContainer, size, config, listener);
        } else {
            if (listener != null)
                listener.onAdFailed("Ad condition not met or ad unit not configured.");
        }
    }

    // --- Helpers to clean up views to avoid leaks ---
    public void destroyBannerIn(FrameLayout adContainer) {
        if (adContainer == null)
            return;
        int childCount = adContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            android.view.View child = adContainer.getChildAt(i);
            if (child instanceof com.google.android.gms.ads.AdView) {
                try {
                    ((com.google.android.gms.ads.AdView) child).destroy();
                } catch (Exception ignored) {
                }
            }
        }
        adContainer.removeAllViews();
    }

    public void clearNativeIn(FrameLayout adContainer) {
        if (adContainer == null)
            return;
        adContainer.removeAllViews();
    }
}
