package com.partharoypc.smartads.managers;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.partharoypc.smartads.AdStatus;
import com.partharoypc.smartads.SmartAds;
import com.partharoypc.smartads.TestAdIds;
import com.partharoypc.smartads.listeners.AppOpenAdListener;

public class AppOpenAdManager extends BaseFullScreenAdManager
        implements DefaultLifecycleObserver, Application.ActivityLifecycleCallbacks {
    private final Application application;
    private AppOpenAd appOpenAd = null;
    private Activity currentActivity;
    private boolean isShowingAd = false;
    private long loadTimeMs = 0L;
    private static final long MAX_AD_AGE_MS = 1L * 60L * 60L * 1000L; // 1 hour for high revenue freshness
    private AppOpenAdListener developerListener;

    public AppOpenAdManager(Application application) {
        this.application = application;
        this.application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        this.isAutoReloadEnabled = true;
    }

    public void setListener(AppOpenAdListener listener) {
        this.developerListener = listener;
    }

    public Activity getCurrentActivityForUmp() {
        return currentActivity;
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        if (SmartAds.getInstance().canShowAds()) {
            showAdIfAvailable();
        }
    }

    public void fetchAd() {
        if (appOpenAd != null && isAdFresh()) {
            return;
        }
        if (isLoading) {
            return;
        }
        isLoading = true;
        adStatus = AdStatus.LOADING;

        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                AppOpenAdManager.this.appOpenAd = ad;
                loadTimeMs = System.currentTimeMillis();
                onAdLoadedBase();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                onAdFailedToLoadBase();
                scheduleRetry(application, null, AppOpenAdManager.this::fetchAd);
            }
        };
        String adUnitId = SmartAds.getInstance().getConfig().isTestMode() ? TestAdIds.ADMOB_APP_OPEN_ID
                : SmartAds.getInstance().getConfig().getAdMobAppOpenId();

        if (adUnitId == null || adUnitId.isEmpty()) {
            isLoading = false;
            adStatus = AdStatus.FAILED;
            return;
        }

        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(application, adUnitId, request, loadCallback);
    }

    public void showAdIfAvailable() {

        if (!isShowingAd && appOpenAd != null && isAdFresh() && currentActivity != null
                && !currentActivity.isFinishing() && !currentActivity.isDestroyed()
                && !SmartAds.getInstance().isAnyAdShowing()) {

            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    AppOpenAdManager.this.appOpenAd = null;
                    isShowingAd = false;
                    fetchAd();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    isShowingAd = true;
                    adStatus = AdStatus.SHOWN;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    AppOpenAdManager.this.appOpenAd = null;
                    isShowingAd = false;
                    fetchAd();
                }

                @Override
                public void onAdImpression() {
                    if (developerListener != null) {
                        developerListener.onAdImpression();
                    }
                }

                @Override
                public void onAdClicked() {
                    if (developerListener != null) {
                        developerListener.onAdClicked();
                    }
                }
            };
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);
        } else {
            if (appOpenAd == null || !isAdFresh()) {
                fetchAd();
            }
        }
    }

    private boolean isAdFresh() {
        return appOpenAd != null && (System.currentTimeMillis() - loadTimeMs) < MAX_AD_AGE_MS;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (currentActivity == activity) {
            currentActivity = null;
        }
    }
}
