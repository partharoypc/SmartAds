package com.partharoy.smartads.managers;

import android.Manifest;
import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.partharoy.smartads.SmartAdsConfig;
import com.partharoy.smartads.TestAdIds;
import com.partharoy.smartads.listeners.BannerAdListener;

public class BannerAdManager {
    private static final String TAG = "BannerAdManager";

    @RequiresPermission(Manifest.permission.INTERNET)
    public void loadAndShowAd(Activity activity, FrameLayout adContainer, SmartAdsConfig config, BannerAdListener listener) {
        loadAdMob(activity, adContainer, config, listener);
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    private void loadAdMob(Activity activity, FrameLayout adContainer, SmartAdsConfig config, BannerAdListener listener) {
        String adUnitId = config.isTestMode() ? TestAdIds.ADMOB_BANNER_ID : config.getAdMobBannerId();
        if (adUnitId == null || adUnitId.isEmpty()) {
            if (config.isUseMetaBackup()) loadMeta(activity, adContainer, config, listener);
            else if (listener != null)
                listener.onAdFailed("No AdMob ID provided and backup disabled.");
            return;
        }

        AdView admobBanner = new AdView(activity);
        admobBanner.setAdSize(AdSize.BANNER);
        admobBanner.setAdUnitId(adUnitId);
        admobBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adContainer.removeAllViews();
                adContainer.addView(admobBanner);
                if (listener != null) listener.onAdLoaded(admobBanner);
                Log.i(TAG, "AdMob Banner Loaded.");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.w(TAG, "AdMob Banner failed: " + loadAdError.getMessage());
                if (config.isUseMetaBackup()) loadMeta(activity, adContainer, config, listener);
                else if (listener != null) listener.onAdFailed(loadAdError.getMessage());
            }
        });
        admobBanner.loadAd(new AdRequest.Builder().build());
    }

    private void loadMeta(Activity activity, FrameLayout adContainer, SmartAdsConfig config, BannerAdListener listener) {
        String placementId = config.isTestMode() ? TestAdIds.META_BANNER_ID.replace("YOUR_PLACEMENT_ID", config.getMetaBannerId()) : config.getMetaBannerId();
        if (placementId == null || placementId.isEmpty()) {
            if (listener != null) listener.onAdFailed("No Meta Placement ID provided for backup.");
            return;
        }

        com.facebook.ads.AdView metaBanner = new com.facebook.ads.AdView(activity, placementId, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        metaBanner.loadAd(metaBanner.buildLoadAdConfig().withAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                adContainer.removeAllViews();
                adContainer.addView(metaBanner);
                if (listener != null) listener.onAdLoaded(metaBanner);
                Log.i(TAG, "Meta Banner Loaded.");
            }

            @Override
            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                Log.e(TAG, "Meta Banner failed: " + adError.getErrorMessage());
                if (listener != null) listener.onAdFailed(adError.getErrorMessage());
            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
            }
        }).build());
    }
}
