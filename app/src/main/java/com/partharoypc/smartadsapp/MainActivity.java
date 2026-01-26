package com.partharoypc.smartadsapp;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.partharoypc.smartads.NativeAdSize;
import com.partharoypc.smartads.SmartAds;
import com.partharoypc.smartads.listeners.BannerAdListener;
import com.partharoypc.smartads.listeners.InterstitialAdListener;
import com.partharoypc.smartads.listeners.NativeAdListener;
import com.partharoypc.smartads.listeners.RewardedAdListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private TextView textSdkStatus;
    private FrameLayout bannerContainer;
    private Button btnLoadBanner, btnLoadCollapsible;

    // Interstitial
    private TextView statusInterstitial;
    private Button btnLoadInterstitial, btnShowInterstitial;

    // Rewarded
    private TextView statusRewarded;
    private Button btnLoadRewarded, btnShowRewarded;

    // App Open
    private TextView statusAppOpen;
    private Button btnShowAppOpen;

    // Native
    private RadioGroup radioGroupNativeSize;
    private Button btnLoadNative;
    private FrameLayout nativeContainer;

    // Debug Utilities
    private Button btnAdInspector, btnPrivacyOptions;

    // Logger
    private TextView textLogger;
    private Button btnClearLog;
    private ScrollView logScrollView;

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

        initViews();
        setupListeners();

        // Initial Status
        updateSdkStatus();
        log("App Started. Ready to test ads.");
    }

    private void initViews() {
        textSdkStatus = findViewById(R.id.text_sdk_status);
        bannerContainer = findViewById(R.id.banner_container);
        btnLoadBanner = findViewById(R.id.btn_load_banner);
        btnLoadCollapsible = findViewById(R.id.btn_load_collapsible);

        statusInterstitial = findViewById(R.id.status_interstitial);
        btnLoadInterstitial = findViewById(R.id.btn_load_interstitial);
        btnShowInterstitial = findViewById(R.id.btn_show_interstitial);

        statusRewarded = findViewById(R.id.status_rewarded);
        btnLoadRewarded = findViewById(R.id.btn_load_rewarded);
        btnShowRewarded = findViewById(R.id.btn_show_rewarded);

        statusAppOpen = findViewById(R.id.status_app_open);
        btnShowAppOpen = findViewById(R.id.btn_show_app_open);

        radioGroupNativeSize = findViewById(R.id.radio_group_native_size);
        btnLoadNative = findViewById(R.id.btn_load_native);
        nativeContainer = findViewById(R.id.native_container);

        textLogger = findViewById(R.id.text_logger);
        btnClearLog = findViewById(R.id.btn_clear_log);
        logScrollView = (ScrollView) textLogger.getParent();

        btnAdInspector = findViewById(R.id.btn_ad_inspector);
        btnPrivacyOptions = findViewById(R.id.btn_privacy_options);
    }

    private void setupListeners() {
        // Banner
        btnLoadBanner.setOnClickListener(v -> loadBanner(false));
        btnLoadCollapsible.setOnClickListener(v -> loadBanner(true));

        // Interstitial
        btnLoadInterstitial.setOnClickListener(v -> loadInterstitial());
        btnShowInterstitial.setOnClickListener(v -> showInterstitial());

        // Rewarded
        btnLoadRewarded.setOnClickListener(v -> loadRewarded());
        btnShowRewarded.setOnClickListener(v -> showRewarded());

        // App Open
        btnShowAppOpen.setOnClickListener(v -> {
            log("Attempting to show App Open Ad...");
            SmartAds.getInstance().showAppOpenAd(this);
        });

        // Native
        btnLoadNative.setOnClickListener(v -> loadNativeAd());

        // Log
        btnClearLog.setOnClickListener(v -> {
            textLogger.setText("> Logs cleared...\n");
        });

        // Debug Utilities
        btnAdInspector.setOnClickListener(v -> {
            log("Opening Ad Inspector...");
            SmartAds.getInstance().launchAdInspector(this);
        });

        btnPrivacyOptions.setOnClickListener(v -> {
            if (SmartAds.getInstance().isPrivacyOptionsRequired()) {
                log("Opening Privacy Options Form...");
                SmartAds.getInstance().showPrivacyOptionsForm(this);
            } else {
                log("Privacy Options Form not required at this time.");
                Toast.makeText(this, "Privacy Options not required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSdkStatus() {
        boolean isTestMode = SmartAds.getInstance().getConfig().isTestMode();
        textSdkStatus.setText("SDK Initialized • Test Mode: " + isTestMode);

        // Background thread to poll status every second for demo purposes or just
        // update once
        // For simplicity, let's just update based on events, but for App Open we can
        // check periodically or on window focus
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            updateAdStatuses();
        }
    }

    private void updateAdStatuses() {
        if (statusAppOpen != null) {
            statusAppOpen.setText("Status: " + SmartAds.getInstance().getAppOpenAdStatus());
        }
    }

    // ================= BANNER =================
    private void loadBanner(boolean collapsible) {
        log("Loading Banner (Collapsible: " + collapsible + ")...");

        SmartAds.getInstance().showBannerAd(this, bannerContainer, new BannerAdListener() {
            @Override
            public void onAdLoaded(View adView) {
                log("Banner Loaded");
                Toast.makeText(MainActivity.this, "Banner Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailed(String errorMessage) {
                log("Banner Failed: " + errorMessage);
                Toast.makeText(MainActivity.this, "Banner Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================= INTERSTITIAL =================
    private void loadInterstitial() {
        log("Loading Interstitial...");
        statusInterstitial.setText("Loading...");
        statusInterstitial.setTextColor(getColor(R.color.text_secondary));

        SmartAds.getInstance().loadInterstitialAd(this);
        btnShowInterstitial.setEnabled(true);
        statusInterstitial.setText("Request Sent (Check Logs)");
    }

    private void showInterstitial() {
        SmartAds.getInstance().showInterstitialAd(this, new InterstitialAdListener() {
            @Override
            public void onAdDismissed() {
                log("Interstitial Dismissed");
                statusInterstitial.setText("Dismissed");
                btnShowInterstitial.setEnabled(false);
            }

            @Override
            public void onAdFailedToShow(String errorMessage) {
                log("Interstitial Failed to Show: " + errorMessage);
                statusInterstitial.setText("Failed");
            }
        });
    }

    // ================= REWARDED =================
    private void loadRewarded() {
        log("Loading Rewarded Ad...");
        statusRewarded.setText("Loading...");
        SmartAds.getInstance().loadRewardedAd(this);
        btnShowRewarded.setEnabled(true);
        statusRewarded.setText("Request Sent");
    }

    private void showRewarded() {
        SmartAds.getInstance().showRewardedAd(this, new RewardedAdListener() {
            @Override
            public void onUserEarnedReward() {
                log("User Earned Reward!");
                Toast.makeText(MainActivity.this, "Reward Earned!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdDismissed() {
                log("Rewarded Ad Dismissed");
                statusRewarded.setText("Dismissed");
                btnShowRewarded.setEnabled(false);
            }

            @Override
            public void onAdFailedToShow(String errorMessage) {
                log("Rewarded Failed: " + errorMessage);
                statusRewarded.setText("Failed");
            }
        });
    }

    // ================= NATIVE =================
    private void loadNativeAd() {
        int selectedId = radioGroupNativeSize.getCheckedRadioButtonId();
        NativeAdSize size = NativeAdSize.MEDIUM;
        if (selectedId == R.id.rb_small)
            size = NativeAdSize.SMALL;
        else if (selectedId == R.id.rb_large)
            size = NativeAdSize.LARGE;

        log("Loading Native Ad (" + size.name() + ")...");

        // Clear previous
        SmartAds.getInstance().clearNativeIn(nativeContainer);

        SmartAds.getInstance().showNativeAd(this, nativeContainer, size, new NativeAdListener() {
            @Override
            public void onAdLoaded(android.view.View nativeAdView) {
                log("Native Ad Loaded");
            }

            @Override
            public void onAdFailed(String errorMessage) {
                log("Native Ad Failed: " + errorMessage);
            }
        });
    }

    // ================= HELPER =================
    private void log(String message) {
        String time = DateFormat.format("HH:mm:ss", new Date()).toString();
        String fullMsg = time + ": " + message + "\n";
        textLogger.append(fullMsg);

        // Auto scroll
        logScrollView.post(() -> logScrollView.fullScroll(View.FOCUS_DOWN));
        Log.d("SmartAdsTest", message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmartAds.getInstance().destroyBannerIn(bannerContainer);
        SmartAds.getInstance().clearNativeIn(nativeContainer);
    }
}