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
                .enableTestMode(true) // test mode → Google test ads
                .setUseMetaBackup(true)
                .setAdShowIntervalMillis(60_000) // 1 min frequency cap
                .setShowAdsAfterDays(0) // show from install day
                .setShowLoadingDialog(true)
                // AdMob test IDs
                .setAdMobBannerId("ca-app-pub-3940256099942544/6300978111")
                .setAdMobInterstitialId("ca-app-pub-3940256099942544/1033173712")
                .setAdMobRewardedId("ca-app-pub-3940256099942544/5224354917")
                .setAdMobNativeId("ca-app-pub-3940256099942544/2247696110")
                .setAdMobAppOpenId("ca-app-pub-3940256099942544/3419835294")
                // Meta backup placements (use your IDs or test placements)
                .setMetaBannerId("IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")
                .setMetaInterstitialId("IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")
                .setMetaRewardedId("VID_HD_16_9_15S_APP_INSTALL#YOUR_PLACEMENT_ID")
                .setMetaNativeId("IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID")
                .build();

        SmartAds.initialize(this, config);
    }
}

