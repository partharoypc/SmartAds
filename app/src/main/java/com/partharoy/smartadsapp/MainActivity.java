package com.partharoy.smartadsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.partharoy.smartads.SmartAds;
import com.partharoy.smartads.listeners.BannerAdListener;
import com.partharoy.smartads.listeners.InterstitialAdListener;
import com.partharoy.smartads.listeners.NativeAdListener;
import com.partharoy.smartads.listeners.RewardedAdListener;

public class MainActivity extends AppCompatActivity {

    private FrameLayout bannerContainer;
    private FrameLayout nativeContainer;
    private Button btnShowInterstitial, btnShowRewarded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // === Find Views ===
        bannerContainer = findViewById(R.id.banner_container);
        nativeContainer = findViewById(R.id.native_container);
        btnShowInterstitial = findViewById(R.id.show_interstitial_btn);
        btnShowRewarded = findViewById(R.id.show_rewarded_btn);

        // ===== Banner Ad =====
        SmartAds.getInstance().showBannerAd(this, bannerContainer, new BannerAdListener() {
            @Override
            public void onAdLoaded(View adView) {
                // Optional: Log or toast "Banner Loaded"
            }

            @Override
            public void onAdFailed(String errorMessage) {
                // Optional: Handle error
            }
        });

        // ===== Interstitial Ad =====
        SmartAds.getInstance().loadInterstitialAd(this);
        btnShowInterstitial.setOnClickListener(v ->
                SmartAds.getInstance().showInterstitialAd(this, new InterstitialAdListener() {
                    @Override
                    public void onAdDismissed() {
                        // Reload for next use
                        SmartAds.getInstance().loadInterstitialAd(MainActivity.this);
                    }

                    @Override
                    public void onAdFailedToShow(String errorMessage) {
                    }
                })
        );

        // ===== Rewarded Ad =====
        SmartAds.getInstance().loadRewardedAd(this);
        btnShowRewarded.setOnClickListener(v ->
                SmartAds.getInstance().showRewardedAd(this, new RewardedAdListener() {
                    @Override
                    public void onUserEarnedReward() {
                        // Grant reward to user
                    }

                    @Override
                    public void onAdDismissed() {
                        // Reload after close
                        SmartAds.getInstance().loadRewardedAd(MainActivity.this);
                    }

                    @Override
                    public void onAdFailedToShow(String errorMessage) {
                    }
                })
        );

        // ===== Native Ad =====
        SmartAds.getInstance().showNativeAd(this, nativeContainer, R.layout.native_ad_layout, new NativeAdListener() {
            @Override
            public void onAdLoaded(View nativeAdView) {
            }

            @Override
            public void onAdFailed(String errorMessage) {
            }
        });

        // ===== App Open Ad =====
        // Handled in MyApplication (auto)

    }
}