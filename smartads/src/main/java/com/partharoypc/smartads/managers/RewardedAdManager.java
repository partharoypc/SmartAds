package com.partharoypc.smartads.managers;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.partharoypc.smartads.AdStatus;
import com.partharoypc.smartads.SmartAdsConfig;
import com.partharoypc.smartads.TestAdIds;
import com.partharoypc.smartads.listeners.RewardedAdListener;

public class RewardedAdManager extends BaseFullScreenAdManager {
    private RewardedAd admobRewardedAd;
    private RewardedAdListener developerListener;

    public void loadAd(Context context, SmartAdsConfig config) {
        if (adStatus == AdStatus.LOADING || adStatus == AdStatus.LOADED || isLoading) {
            return;
        }
        adStatus = AdStatus.LOADING;
        isLoading = true;
        loadAdMob(context, config);
    }

    private void loadAdMob(Context context, SmartAdsConfig config) {
        String adUnitId = config.isTestMode() ? TestAdIds.ADMOB_REWARDED_ID : config.getAdMobRewardedId();
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
        RewardedAd.load(context, adUnitId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                admobRewardedAd = rewardedAd;
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

    public void showAd(Activity activity, RewardedAdListener listener) {
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

        if (isFrequencyCapped(com.partharoypc.smartads.SmartAds.getInstance().getConfig())) {
            if (listener != null)
                listener.onAdFailedToShow("Ad is frequency capped.");
            return;
        }

        if (adStatus != AdStatus.LOADED) {
            // Not loaded and not loading -> Start loading and wait
            isShowPending = true;
            pendingActivity = activity;
            showLoadingDialog(activity);
            SmartAdsConfig config = com.partharoypc.smartads.SmartAds.getInstance().getConfig();
            loadAd(activity, config);
            return;
        }

        // Ad is LOADED, show it
        if (admobRewardedAd != null) {
            admobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    if (developerListener != null)
                        developerListener.onAdDismissed();
                    admobRewardedAd = null;
                    adStatus = AdStatus.IDLE;
                    if (isAutoReloadEnabled) {
                        loadAd(activity, com.partharoypc.smartads.SmartAds.getInstance().getConfig());
                    }
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    if (developerListener != null) {
                        developerListener.onAdFailedToShow(adError.getMessage());
                    }
                    admobRewardedAd = null;
                    adStatus = AdStatus.IDLE;
                    if (isAutoReloadEnabled) {
                        loadAd(activity, com.partharoypc.smartads.SmartAds.getInstance().getConfig());
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
            admobRewardedAd.show(activity, rewardItem -> {
                if (developerListener != null)
                    developerListener.onUserEarnedReward();
            });
            adStatus = AdStatus.SHOWN;
        } else {
            if (developerListener != null)
                listener.onAdFailedToShow("Ad not loaded.");
        }
    }
}