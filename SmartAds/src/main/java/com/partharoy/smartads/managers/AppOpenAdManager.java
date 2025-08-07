package com.partharoy.smartads.managers;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.partharoy.smartads.SmartAds;
import com.partharoy.smartads.TestAdIds;

public class AppOpenAdManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String TAG = "AppOpenAdManager";
    private final Application application;
    private AppOpenAd appOpenAd = null;
    private Activity currentActivity;
    private boolean isShowingAd = false;

    public AppOpenAdManager(Application application) {
        this.application = application;
        this.application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void onMoveToForeground() {
        if (SmartAds.getInstance().canShowAds()) {
            showAdIfAvailable();
        }
    }

    public void fetchAd() {
        if (appOpenAd != null) {
            return;
        }
        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                AppOpenAdManager.this.appOpenAd = ad;
                Log.i(TAG, "App Open Ad loaded.");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.w(TAG, "App Open Ad failed to load: " + loadAdError.getMessage());
            }
        };
        String adUnitId = SmartAds.getInstance().getConfig().isTestMode() ?
                TestAdIds.ADMOB_APP_OPEN_ID : SmartAds.getInstance().getConfig().getAdMobAppOpenId();

        if (adUnitId == null || adUnitId.isEmpty()) {
            Log.e(TAG, "App Open Ad ID is not set.");
            return;
        }

        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(application, adUnitId, request, loadCallback);
    }

    public void showAdIfAvailable() {
        if (!isShowingAd && appOpenAd != null && currentActivity != null) {
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
                }
            };
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);
        } else {
            fetchAd();
        }
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