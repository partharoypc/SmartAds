package com.partharoy.smartads;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.RequiresPermission;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.partharoy.smartads.helpers.AdIntervalHelper;
import com.partharoy.smartads.helpers.InstallTimeHelper;
import com.partharoy.smartads.listeners.BannerAdListener;
import com.partharoy.smartads.listeners.InterstitialAdListener;
import com.partharoy.smartads.listeners.NativeAdListener;
import com.partharoy.smartads.listeners.RewardedAdListener;
import com.partharoy.smartads.managers.AppOpenAdManager;
import com.partharoy.smartads.managers.BannerAdManager;
import com.partharoy.smartads.managers.InterstitialAdManager;
import com.partharoy.smartads.managers.NativeAdManager;
import com.partharoy.smartads.managers.RewardedAdManager;

public class SmartAds {

    private static final String TAG = "SmartAds";
    private static SmartAds instance;
    private SmartAdsConfig config;
    private boolean adsEnabled; // Master switch for all ads

    private AppOpenAdManager appOpenAdManager;
    private InterstitialAdManager interstitialAdManager;
    private RewardedAdManager rewardedAdManager;
    private BannerAdManager bannerAdManager;
    private NativeAdManager nativeAdManager;

    private SmartAds() {
    }

    public static SmartAds getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SmartAds.initialize() must be called in your Application class before use.");
        }
        return instance;
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public static void initialize(Application application, SmartAdsConfig config) {
        if (instance == null) {
            instance = new SmartAds();
            instance.config = config;
            instance.adsEnabled = config.isAdsEnabled(); // Set initial state from config

            // Initialize Helpers
            InstallTimeHelper.initialize(application);
            AdIntervalHelper.initialize();

            // Initialize SDKs
            MobileAds.initialize(application);
            AudienceNetworkAds.initialize(application);

            // Initialize Managers
            instance.appOpenAdManager = new AppOpenAdManager(application);
            instance.interstitialAdManager = new InterstitialAdManager();
            instance.rewardedAdManager = new RewardedAdManager();
            instance.bannerAdManager = new BannerAdManager();
            instance.nativeAdManager = new NativeAdManager();

            Log.i(TAG, "Smart Ads Library Initialized. Ads are " + (instance.adsEnabled ? "ON" : "OFF"));
            if (config.isTestMode()) {
                Log.w(TAG, "TEST MODE IS ENABLED.");
            }
        }
    }

    public void setAdsEnabled(boolean enabled) {
        this.adsEnabled = enabled;
        Log.i(TAG, "Overall Ads Status set to: " + (enabled ? "ON" : "OFF"));
    }

    public boolean areAdsEnabled() {
        return this.adsEnabled;
    }

    public SmartAdsConfig getConfig() {
        return config;
    }

    public boolean canShowAds() {
        if (!this.adsEnabled) {
            Log.w(TAG, "All ads are globally disabled.");
            return false;
        }
        if (!InstallTimeHelper.isAdGatingPeriodOver(config.getShowAdsAfterDays())) {
            Log.w(TAG, "Ads are gated for " + config.getShowAdsAfterDays() + " days.");
            return false;
        }
        return true;
    }

    // --- Interstitial Ads ---
    public void loadInterstitialAd(Context context) {
        if (canShowAds()) {
            interstitialAdManager.loadAd(context, config);
        }
    }

    public void showInterstitialAd(Activity activity, InterstitialAdListener listener) {
        if (canShowAds() && AdIntervalHelper.canShowAd(config.getAdShowIntervalMillis())) {
            interstitialAdManager.showAd(activity, listener);
        } else {
            if (listener != null) listener.onAdFailedToShow("Ad condition not met.");
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
        if (canShowAds()) {
            rewardedAdManager.showAd(activity, listener);
        } else {
            if (listener != null) listener.onAdFailedToShow("Ad condition not met.");
        }
    }

    public AdStatus getRewardedAdStatus() {
        return rewardedAdManager.getAdStatus();
    }

    // --- Banner Ads ---
    @RequiresPermission(Manifest.permission.INTERNET)
    public void showBannerAd(Activity activity, FrameLayout adContainer, BannerAdListener listener) {
        if (canShowAds()) {
            bannerAdManager.loadAndShowAd(activity, adContainer, config, listener);
        } else {
            if (listener != null) listener.onAdFailed("Ad condition not met.");
        }
    }

    // --- Native Ads ---
    public void showNativeAd(Activity activity, FrameLayout adContainer, @LayoutRes int layoutRes, NativeAdListener listener) {
        if (canShowAds()) {
            nativeAdManager.loadAndShowAd(activity, adContainer, layoutRes, config, listener);
        } else {
            if (listener != null) listener.onAdFailed("Ad condition not met.");
        }
    }
}