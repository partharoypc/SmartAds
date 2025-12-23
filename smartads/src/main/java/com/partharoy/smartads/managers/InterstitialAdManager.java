package com.partharoy.smartads.managers;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.partharoy.smartads.AdStatus;
import com.partharoy.smartads.SmartAdsConfig;
import com.partharoy.smartads.TestAdIds;
import com.partharoy.smartads.listeners.InterstitialAdListener;

public class InterstitialAdManager extends BaseFullScreenAdManager {
    private InterstitialAd admobInterstitial;
    private InterstitialAdListener developerListener;

    public void loadAd(Context context, SmartAdsConfig config) {
        if (adStatus == AdStatus.LOADING || adStatus == AdStatus.LOADED || isLoading) {
            return;
        }
        adStatus = AdStatus.LOADING;
        isLoading = true;
        loadAdMob(context, config);
    }

    private void loadAdMob(Context context, SmartAdsConfig config) {
        String adUnitId = config.isTestMode() ? TestAdIds.ADMOB_INTERSTITIAL_ID : config.getAdMobInterstitialId();
        if (adUnitId == null || adUnitId.isEmpty()) {
            onAdFailedToLoadBase();

            if (isShowPending && developerListener != null) {
                developerListener.onAdFailedToShow("Ad Unit ID is missing.");
            }
            isShowPending = false;
            pendingActivity = null;
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                admobInterstitial = interstitialAd;
                onAdLoadedBase();

                if (isShowPending && pendingActivity != null) {
                    showAd(pendingActivity, developerListener);
                    isShowPending = false;
                    pendingActivity = null;
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                onAdFailedToLoadBase();

                if (isShowPending && developerListener != null) {
                    developerListener.onAdFailedToShow(loadAdError.getMessage());
                }
                isShowPending = false;
                pendingActivity = null;

                scheduleRetry(context, config, () -> loadAd(context, config));
            }
        });
    }

    public void showAd(Activity activity, InterstitialAdListener listener) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            if (listener != null)
                listener.onAdFailedToShow("Activity is invalid.");
            return;
        }

        this.developerListener = listener;

        if (adStatus == AdStatus.LOADING) {
            isShowPending = true;
            pendingActivity = activity;
            showLoadingDialog(activity);
            return;
        }

        if (isFrequencyCapped(com.partharoy.smartads.SmartAds.getInstance().getConfig())) {
            if (listener != null)
                listener.onAdFailedToShow("Ad is frequency capped.");
            return;
        }

        if (adStatus != AdStatus.LOADED) {
            // Not loaded -> load and wait
            isShowPending = true;
            pendingActivity = activity;
            showLoadingDialog(activity);
            SmartAdsConfig config = com.partharoy.smartads.SmartAds.getInstance().getConfig();
            loadAd(activity, config);
            return;
        }

        if (admobInterstitial != null) {
            admobInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    if (developerListener != null)
                        developerListener.onAdDismissed();
                    admobInterstitial = null;
                    adStatus = AdStatus.IDLE;
                    if (isAutoReloadEnabled) {
                        loadAd(activity, com.partharoy.smartads.SmartAds.getInstance().getConfig());
                    }
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    if (developerListener != null) {
                        developerListener.onAdFailedToShow(adError.getMessage());
                    }
                    admobInterstitial = null;
                    adStatus = AdStatus.IDLE;
                    if (isAutoReloadEnabled) {
                        loadAd(activity, com.partharoy.smartads.SmartAds.getInstance().getConfig());
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    adStatus = AdStatus.SHOWN;
                    lastShownTime = System.currentTimeMillis();
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
            });
            admobInterstitial.show(activity);
        } else {
            if (developerListener != null)
                listener.onAdFailedToShow("Ad not loaded.");
        }
    }
}
