package com.partharoy.smartads.managers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.partharoy.smartads.AdStatus;
import com.partharoy.smartads.SmartAdsConfig;
import com.partharoy.smartads.TestAdIds;
import com.partharoy.smartads.helpers.AdIntervalHelper;
import com.partharoy.smartads.listeners.InterstitialAdListener;
import com.partharoy.smartads.ui.LoadingAdDialog;

public class InterstitialAdManager {
    private static final String TAG = "InterstitialAdManager";
    private InterstitialAd admobInterstitial;
    private com.facebook.ads.InterstitialAd metaInterstitial;
    private AdStatus adStatus = AdStatus.IDLE;
    private LoadingAdDialog loadingDialog;
    private InterstitialAdListener developerListener;

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
            loadingDialog.show("Loading Ad...");
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
        String adUnitId = config.isTestMode() ? TestAdIds.ADMOB_INTERSTITIAL_ID : config.getAdMobInterstitialId();
        if (adUnitId == null || adUnitId.isEmpty()) {
            if (config.isUseMetaBackup()) loadMeta(context, config);
            else {
                adStatus = AdStatus.FAILED;
                dismissLoadingDialog();
            }
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                admobInterstitial = interstitialAd;
                adStatus = AdStatus.LOADED;
                dismissLoadingDialog();
                Log.i(TAG, "AdMob Interstitial Loaded.");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.w(TAG, "AdMob Interstitial failed: " + loadAdError.getMessage());
                if (config.isUseMetaBackup()) loadMeta(context, config);
                else {
                    adStatus = AdStatus.FAILED;
                    dismissLoadingDialog();
                }
            }
        });
    }

    private void loadMeta(Context context, SmartAdsConfig config) {
        String placementId = config.isTestMode() ? TestAdIds.META_INTERSTITIAL_ID.replace("YOUR_PLACEMENT_ID", config.getMetaInterstitialId()) : config.getMetaInterstitialId();
        if (placementId == null || placementId.isEmpty()) {
            adStatus = AdStatus.FAILED;
            dismissLoadingDialog();
            return;
        }

        metaInterstitial = new com.facebook.ads.InterstitialAd(context, placementId);
        com.facebook.ads.InterstitialAdListener metaListener = new com.facebook.ads.InterstitialAdListener() {
            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                adStatus = AdStatus.LOADED;
                dismissLoadingDialog();
                Log.i(TAG, "Meta Interstitial Loaded.");
            }

            @Override
            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                adStatus = AdStatus.FAILED;
                dismissLoadingDialog();
                Log.e(TAG, "Meta Interstitial failed: " + adError.getErrorMessage());
                if (developerListener != null)
                    developerListener.onAdFailedToShow(adError.getErrorMessage());
            }

            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                if (developerListener != null) developerListener.onAdDismissed();
                metaInterstitial = null;
                adStatus = AdStatus.IDLE;
            }

            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {
            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
            }
        };
        metaInterstitial.loadAd(metaInterstitial.buildLoadAdConfig().withAdListener(metaListener).build());
    }

    public void showAd(Activity activity, InterstitialAdListener listener) {
        if (adStatus != AdStatus.LOADED) {
            if (listener != null) listener.onAdFailedToShow("Ad not loaded.");
            return;
        }

        this.developerListener = listener;

        if (admobInterstitial != null) {
            admobInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    if (developerListener != null) developerListener.onAdDismissed();
                    admobInterstitial = null;
                    adStatus = AdStatus.IDLE;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    if (developerListener != null)
                        developerListener.onAdFailedToShow(adError.getMessage());
                    admobInterstitial = null;
                    adStatus = AdStatus.IDLE;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    adStatus = AdStatus.SHOWN;
                    AdIntervalHelper.onAdShown();
                }
            });
            admobInterstitial.show(activity);
        } else if (metaInterstitial != null && metaInterstitial.isAdLoaded()) {
            metaInterstitial.show();
            adStatus = AdStatus.SHOWN;
            AdIntervalHelper.onAdShown();
        }
    }
}