package com.partharoy.smartads.managers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.partharoy.smartads.AdStatus;
import com.partharoy.smartads.SmartAdsConfig;
import com.partharoy.smartads.TestAdIds;
import com.partharoy.smartads.listeners.RewardedAdListener;
import com.partharoy.smartads.ui.LoadingAdDialog;

public class RewardedAdManager {
    private static final String TAG = "RewardedAdManager";
    private RewardedAd admobRewardedAd;
    private com.facebook.ads.RewardedVideoAd metaRewardedAd;
    private AdStatus adStatus = AdStatus.IDLE;
    private LoadingAdDialog loadingDialog;
    private RewardedAdListener developerListener;

    public AdStatus getAdStatus() {
        return adStatus;
    }

    public void loadAd(Context context, SmartAdsConfig config) {
        if (adStatus == AdStatus.LOADING || adStatus == AdStatus.LOADED) {
            return;
        }
        adStatus = AdStatus.LOADING;
        if (config.shouldShowLoadingDialog()) {
            loadingDialog = new LoadingAdDialog(context);
            loadingDialog.show("Loading Reward...");
        }
        loadAdMob(context, config);
    }

    private void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    private void loadAdMob(Context context, SmartAdsConfig config) {
        String adUnitId = config.isTestMode() ? TestAdIds.ADMOB_REWARDED_ID : config.getAdMobRewardedId();
        if (adUnitId == null || adUnitId.isEmpty()) {
            if (config.isUseMetaBackup()) loadMeta(context, config);
            else {
                adStatus = AdStatus.FAILED;
                dismissLoadingDialog();
            }
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, adUnitId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                admobRewardedAd = rewardedAd;
                adStatus = AdStatus.LOADED;
                dismissLoadingDialog();
                Log.i(TAG, "AdMob Rewarded Ad Loaded.");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.w(TAG, "AdMob Rewarded Ad failed: " + loadAdError.getMessage());
                if (config.isUseMetaBackup()) loadMeta(context, config);
                else {
                    adStatus = AdStatus.FAILED;
                    dismissLoadingDialog();
                }
            }
        });
    }

    private void loadMeta(Context context, SmartAdsConfig config) {
        String placementId = config.isTestMode() ? TestAdIds.META_REWARDED_ID.replace("YOUR_PLACEMENT_ID", config.getMetaRewardedId()) : config.getMetaRewardedId();
        if (placementId == null || placementId.isEmpty()) {
            adStatus = AdStatus.FAILED;
            dismissLoadingDialog();
            return;
        }

        metaRewardedAd = new com.facebook.ads.RewardedVideoAd(context, placementId);
        com.facebook.ads.RewardedVideoAdListener metaListener = new com.facebook.ads.RewardedVideoAdListener() {
            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                adStatus = AdStatus.LOADED;
                dismissLoadingDialog();
                Log.i(TAG, "Meta Rewarded Ad Loaded.");
            }

            @Override
            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                adStatus = AdStatus.FAILED;
                dismissLoadingDialog();
                Log.e(TAG, "Meta Rewarded Ad failed: " + adError.getErrorMessage());
                if (developerListener != null)
                    developerListener.onAdFailedToShow(adError.getErrorMessage());
            }

            @Override
            public void onRewardedVideoCompleted() {
                if (developerListener != null) developerListener.onUserEarnedReward();
            }

            @Override
            public void onRewardedVideoClosed() {
                if (developerListener != null) developerListener.onAdDismissed();
                metaRewardedAd = null;
                adStatus = AdStatus.IDLE;
            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
            }
        };
        metaRewardedAd.loadAd(metaRewardedAd.buildLoadAdConfig().withAdListener(metaListener).build());
    }

    public void showAd(Activity activity, RewardedAdListener listener) {
        if (adStatus != AdStatus.LOADED) {
            if (listener != null) listener.onAdFailedToShow("Ad not loaded.");
            return;
        }

        this.developerListener = listener;

        if (admobRewardedAd != null) {
            admobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    if (developerListener != null) developerListener.onAdDismissed();
                    admobRewardedAd = null;
                    adStatus = AdStatus.IDLE;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    if (developerListener != null)
                        developerListener.onAdFailedToShow(adError.getMessage());
                    admobRewardedAd = null;
                    adStatus = AdStatus.IDLE;
                }
            });
            admobRewardedAd.show(activity, rewardItem -> {
                if (developerListener != null) developerListener.onUserEarnedReward();
            });
            adStatus = AdStatus.SHOWN;
        } else if (metaRewardedAd != null && metaRewardedAd.isAdLoaded()) {
            metaRewardedAd.show();
            adStatus = AdStatus.SHOWN;
        }
    }
}