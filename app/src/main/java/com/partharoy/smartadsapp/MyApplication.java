package com.partharoy.smartadsapp;

import android.app.Application;

import com.partharoy.smartads.SmartAds;
import com.partharoy.smartads.SmartAdsConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SmartAdsConfig config = new SmartAdsConfig.Builder()
                .setAdsEnabled(true)
                .enableTestMode(true)
                .setAdMobBannerId("ca-app-pub-3940256099942544/6300978111")
                .setAdMobInterstitialId("ca-app-pub-3940256099942544/1033173712")
                .setAdMobRewardedId("ca-app-pub-3940256099942544/5224354917")
                .setAdMobNativeId("ca-app-pub-3940256099942544/2247696110")
                .setAdMobAppOpenId("ca-app-pub-3940256099942544/9257395921")
                .enableCollapsibleBanner(true)
                .setUseUmpConsent(true)
                .setFrequencyCap(15)
                .setLoadingDialogText("Loading awesome ad...")
                .setLoadingDialogColor(android.graphics.Color.DKGRAY, android.graphics.Color.YELLOW)
                .build();

        SmartAds.initialize(this, config);
    }
}
