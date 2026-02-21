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
import com.partharoypc.smartads.analytics.SmartAdsAnalyticsListener;
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

    private SmartAdsAnalyticsListener analyticsListener;

    private Application application;

    private SmartAds() {
    }

    private void requestUmpWithActivity(Application application) {
        if (appOpenAdManager == null)
            return;
        ConsentRequestParameters params = new ConsentRequestParameters.Builder().build();
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(application);
        consentInformation.requestConsentInfoUpdate(
                appOpenAdManager.getCurrentActivityForUmp(),
                params,
                () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            appOpenAdManager.getCurrentActivityForUmp(),
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
                    "SmartAds not initialized. You must call SmartAds.initialize(application, config) " +
                            "before calling getInstance(). Use SmartAds.isInitialized() to check status.");
        }
        return instance;
    }

    /**
     * Checks if the SmartAds SDK is currently initialized and ready to use.
     *
     * @return true if initialized, false otherwise.
     */
    public static boolean isInitialized() {
        return instance != null;
    }

    /**
     * Returns the current version of the library.
     */
    public static String getVersion() {
        return "5.6.0";
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public static void initialize(Application application, SmartAdsConfig config) {
        if (instance == null) {
            synchronized (SmartAds.class) {
                if (instance == null) {
                    if (application == null || config == null) {
                        SmartAdsLogger.e("SmartAds Initialize failed: Application or Config is null.");
                        return;
                    }

                    if (androidx.core.content.ContextCompat.checkSelfPermission(application,
                            Manifest.permission.INTERNET) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        SmartAdsLogger.e("⚠️ INTERNET permission is missing! SmartAds requires it to function.");
                    }

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

                    instance.initializeManagersIfNeeded(application);

                    // UMP Consent (optional)
                    if (config.useUmpConsent()) {
                        instance.requestUmpWithActivity(application);
                    } else {
                        instance.initializeSdks(application);
                    }

                    SmartAdsLogger.d("SmartAds Initialized (v" + getVersion() + ")");
                }
            }
        }
    }

    /**
     * Checks if the device currently has an active internet connection.
     * Delegates to
     * {@link com.partharoypc.smartads.utils.NetworkUtils#isNetworkAvailable(Context)}.
     */
    public static boolean isNetworkAvailable(Context context) {
        return com.partharoypc.smartads.utils.NetworkUtils.isNetworkAvailable(context);
    }

    /**
     * Fully shuts down the SmartAds library, unregistering listeners and clearing
     * managers.
     * Should be called if the application is being destroyed or if you want to stop
     * ad services completely.
     */
    public void shutdown() {
        if (application != null && appOpenAdManager != null) {
            appOpenAdManager.destroy(application);
        }
        config = null;
        appOpenAdManager = null;
        interstitialAdManager = null;
        rewardedAdManager = null;
        bannerAdManager = null;
        nativeAdManager = null;
        analyticsListener = null;
        application = null;
        instance = null;
        SmartAdsLogger.d("SmartAds SDK Shutdown complete.");
    }

    /**
     * Launches the AdMob Ad Inspector.
     * Useful for verifying ad network integration.
     *
     * @param activity current activity context.
     */
    public void launchAdInspector(Activity activity) {
        MobileAds.openAdInspector(activity, error -> {
            /* Ad inspector closed or error */
        });
    }

    /**
     * Checks if the user is required to see privacy options (e.g., GDPR).
     *
     * @return true if privacy options are required.
     */
    public boolean isPrivacyOptionsRequired() {
        return UserMessagingPlatform.getConsentInformation(application)
                .getPrivacyOptionsRequirementStatus() == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED;
    }

    /**
     * Pre-loads ads (App Open, Interstitial, Rewarded) based on configuration.
     * Should be called after initialization or when appropriate.
     */
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

    /**
     * Shows the UMP Privacy Options Form to the user.
     */
    public void showPrivacyOptionsForm(Activity activity) {
        try {
            UserMessagingPlatform.showPrivacyOptionsForm(activity, formError -> {
                /* no-op */
            });
        } catch (Exception ignored) {
        }
    }

    // --- App Open Ad (Manual) ---

    /**
     * Manually shows an App Open Ad if available.
     *
     * @param activity The current activity.
     */
    public void showAppOpenAd(Activity activity) {
        if (canShowAds() && appOpenAdManager != null) {
            appOpenAdManager.showAdIfAvailable();
        }
    }

    /**
     * Checks if an App Open Ad is currently available to show.
     */
    public boolean isAppOpenAdAvailable() {
        return appOpenAdManager != null && appOpenAdManager.isAdAvailable();
    }

    /**
     * Returns the current status of the App Open Ad.
     */
    public AdStatus getAppOpenAdStatus() {
        if (appOpenAdManager != null) {
            return appOpenAdManager.getAdStatus();
        }
        return AdStatus.IDLE;
    }

    /**
     * Temporarily enables or disables ads (e.g. if user purchased premium).
     */
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

    public void setAnalyticsListener(SmartAdsAnalyticsListener listener) {
        this.analyticsListener = listener;
    }

    /**
     * Internal method used by Ad Managers to report revenue.
     */
    public void reportPaidEvent(com.google.android.gms.ads.AdValue adValue,
            com.google.android.gms.ads.ResponseInfo responseInfo,
            String adUnitId,
            String adFormat) {
        if (analyticsListener != null) {
            String networkName = "Google AdMob";

            analyticsListener.onAdRevenuePaid(
                    adUnitId,
                    adFormat,
                    networkName,
                    adValue.getValueMicros(),
                    adValue.getCurrencyCode(),
                    adValue.getPrecisionType(),
                    null);

            SmartAdsLogger.d("💰 Paid Event: " +
                    adValue.getValueMicros() + " " + adValue.getCurrencyCode() +
                    " | Network: " + networkName +
                    " | Format: " + adFormat);
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

    /**
     * Checks if an Interstitial Ad is loaded and ready to show.
     */
    public boolean isInterstitialAdAvailable() {
        return interstitialAdManager != null && interstitialAdManager.getAdStatus() == AdStatus.LOADED;
    }

    /**
     * Manually starts loading an Interstitial Ad.
     */
    public void loadInterstitialAd(Context context) {
        if (canShowAds()) {
            interstitialAdManager.loadAd(context, config);
        }
    }

    /**
     * Shows the Interstitial Ad if available.
     *
     * @param activity The activity context.
     * @param listener The listener for ad events.
     */
    public void showInterstitialAd(Activity activity, InterstitialAdListener listener) {
        if (canShowAds()) {
            interstitialAdManager.showAd(activity, listener);
        }
    }

    public AdStatus getInterstitialAdStatus() {
        return interstitialAdManager.getAdStatus();
    }

    // --- Rewarded Ads ---

    /**
     * Checks if a Rewarded Ad is loaded and ready to show.
     */
    public boolean isRewardedAdAvailable() {
        return rewardedAdManager != null && rewardedAdManager.getAdStatus() == AdStatus.LOADED;
    }

    /**
     * Manually starts loading a Rewarded Ad.
     */
    public void loadRewardedAd(Context context) {
        if (canShowAds()) {
            rewardedAdManager.loadAd(context, config);
        }
    }

    /**
     * Shows a Rewarded Ad if loaded.
     *
     * @param activity Current activity.
     * @param listener Callback listener.
     */
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
        boolean appOpenShowing = appOpenAdManager != null
                && (appOpenAdManager.getAdStatus() == AdStatus.SHOWN || appOpenAdManager.isShowingAd());
        return interShowing || rewardedShowing || appOpenShowing;
    }

    // --- Banner Ads ---

    /**
     * Loads and shows a Banner Ad in the provided container.
     *
     * @param activity    Current activity.
     * @param adContainer FrameLayout to hold the banner.
     * @param listener    Callback listener.
     */
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

    /**
     * Loads and shows a Native Ad with a custom layout reference.
     *
     * @param activity    Current activity.
     * @param adContainer FrameLayout to hold the native ad.
     * @param layoutRes   Resource ID of the native ad layout.
     * @param listener    Callback listener.
     */
    public void showNativeAd(Activity activity, FrameLayout adContainer, @LayoutRes int layoutRes,
            NativeAdListener listener) {
        if (canShowAds() && config.isNativeConfigured()) {
            nativeAdManager.loadAndShowAd(activity, adContainer, layoutRes, config, listener);
        } else {
            if (listener != null)
                listener.onAdFailed("Ad condition not met or ad unit not configured.");
        }
    }

    /**
     * Loads and shows a Native Ad with a standard size template.
     *
     * @param activity    Current activity.
     * @param adContainer FrameLayout to hold the native ad.
     * @param size        Size enum (SMALL, MEDIUM, LARGE).
     * @param listener    Callback listener.
     */
    public void showNativeAd(Activity activity, FrameLayout adContainer, NativeAdSize size, NativeAdListener listener) {
        if (canShowAds() && config.isNativeConfigured()) {
            nativeAdManager.loadAndShowAd(activity, adContainer, size, config, listener);
        } else {
            if (listener != null)
                listener.onAdFailed("Ad condition not met or ad unit not configured.");
        }
    }

    // --- Helpers to clean up views to avoid leaks ---

    /**
     * Destroys existing banner ads in the container and clears the view.
     */
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

    /**
     * Clears potential native ad views from the container.
     */
    public void clearNativeIn(FrameLayout adContainer) {
        if (adContainer == null)
            return;
        adContainer.removeAllViews();
    }
}
