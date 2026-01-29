package com.partharoypc.smartads.demo;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    private com.google.android.material.switchmaterial.SwitchMaterial switchEnableAds;
    private com.google.android.material.switchmaterial.SwitchMaterial switchMediationFacebook;
    private com.google.android.material.switchmaterial.SwitchMaterial switchMediationAppLovin;
    private com.google.android.material.switchmaterial.SwitchMaterial switchMediationUnity;
    private Button btnVerifyMediation;

    // Banner
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
    private Button btnAdInspector, btnPrivacyOptions, btnShutdownSdk;

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
        updateSdkStatus();

        // Setup Analytics Listener for Demo
        SmartAds.getInstance()
                .setAnalyticsListener((adUnitId, adFormat, adNetwork, valueMicros, currencyCode, precision, extras) -> {
                    double revenue = valueMicros / 1000000.0;
                    log("💰 Paid: " + revenue + " " + currencyCode + " [" + adNetwork + "] (" + adFormat + ")");
                });

        log("App Started. Ready to test ads. Mediation toggles active.");
    }

    private void initViews() {
        // Implementation Header
        textSdkStatus = findViewById(R.id.text_sdk_status);
        switchEnableAds = findViewById(R.id.switch_enable_ads);
        switchMediationFacebook = findViewById(R.id.switch_mediation_facebook);
        switchMediationAppLovin = findViewById(R.id.switch_mediation_applovin);
        switchMediationUnity = findViewById(R.id.switch_mediation_unity);
        btnVerifyMediation = findViewById(R.id.btn_verify_mediation);

        // Banner
        bannerContainer = findViewById(R.id.banner_container);
        btnLoadBanner = findViewById(R.id.btn_load_banner);
        btnLoadCollapsible = findViewById(R.id.btn_load_collapsible);

        // Interstitial
        statusInterstitial = findViewById(R.id.status_interstitial);
        btnLoadInterstitial = findViewById(R.id.btn_load_interstitial);
        btnShowInterstitial = findViewById(R.id.btn_show_interstitial);

        // Rewarded
        statusRewarded = findViewById(R.id.status_rewarded);
        btnLoadRewarded = findViewById(R.id.btn_load_rewarded);
        btnShowRewarded = findViewById(R.id.btn_show_rewarded);

        // App Open
        statusAppOpen = findViewById(R.id.status_app_open);
        btnShowAppOpen = findViewById(R.id.btn_show_app_open);

        // Native
        radioGroupNativeSize = findViewById(R.id.radio_group_native_size);
        btnLoadNative = findViewById(R.id.btn_load_native);
        nativeContainer = findViewById(R.id.native_container);

        // Debug
        btnAdInspector = findViewById(R.id.btn_ad_inspector);
        btnPrivacyOptions = findViewById(R.id.btn_privacy_options);
        btnShutdownSdk = findViewById(R.id.btn_shutdown_sdk);

        // Logs
        textLogger = findViewById(R.id.text_logger);
        btnClearLog = findViewById(R.id.btn_clear_log);
        logScrollView = (ScrollView) textLogger.getParent();
    }

    private void setupListeners() {
        // --- General Settings Switches ---

        switchEnableAds.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleAds(isChecked);
        });

        switchMediationFacebook.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateMediationConfig(isChecked, switchMediationAppLovin.isChecked(), switchMediationUnity.isChecked());
        });

        switchMediationAppLovin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateMediationConfig(switchMediationFacebook.isChecked(), isChecked, switchMediationUnity.isChecked());
        });

        switchMediationUnity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateMediationConfig(switchMediationFacebook.isChecked(), switchMediationAppLovin.isChecked(), isChecked);
        });

        btnVerifyMediation.setOnClickListener(v -> {
            log("Verifying Mediation Adapters based on CURRENT switches...");
            SmartAds.getInstance().verifyMediation(this);
        });

        findViewById(R.id.btn_test_suite).setOnClickListener(v -> {
            SmartAds.getInstance().openMediationTestSuite(this);
        });

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
        btnClearLog.setOnClickListener(v -> textLogger.setText("> Logs cleared...\n"));

        // Debug Utilities
        btnAdInspector.setOnClickListener(v -> {
            log("Opening Ad Inspector...");
            SmartAds.getInstance().launchAdInspector(this);
        });

        // Mediation Verification Shortcut (Long Click)
        btnAdInspector.setOnLongClickListener(v -> {
            log("Verifying Mediation Adapters...");
            SmartAds.getInstance().verifyMediation(this);
            return true;
        });

        btnPrivacyOptions.setOnClickListener(v -> {
            if (SmartAds.getInstance().isPrivacyOptionsRequired()) {
                log("Opening Privacy Options Form...");
                SmartAds.getInstance().showPrivacyOptionsForm(this);
            } else {
                log("Privacy Options Form not required at this time.");
                showSnackbar("Privacy Options not required");
            }
        });

        btnShutdownSdk.setOnClickListener(v -> {
            log("🛑 Shutting down SmartAds SDK...");
            try {
                SmartAds.getInstance().shutdown();
                log("SDK Shutdown complete. Resources cleared.");
                updateSdkStatus();
                showSnackbar("SmartAds Shutdown");

                // Disable all ad buttons
                disableAllAdButtons();
            } catch (Exception e) {
                log("Shutdown Error: " + e.getMessage());
            }
        });
    }

    private void disableAllAdButtons() {
        btnLoadBanner.setEnabled(false);
        btnLoadCollapsible.setEnabled(false);
        btnLoadInterstitial.setEnabled(false);
        btnShowInterstitial.setEnabled(false);
        btnLoadRewarded.setEnabled(false);
        btnShowRewarded.setEnabled(false);
        btnShowAppOpen.setEnabled(false);
        btnLoadNative.setEnabled(false);
        btnVerifyMediation.setEnabled(false);
        btnAdInspector.setEnabled(false);
        btnPrivacyOptions.setEnabled(false);
        btnShutdownSdk.setEnabled(false);

        textSdkStatus.setText(R.string.sdk_shutdown_message);
        textSdkStatus.setTextColor(ContextCompat.getColor(this, R.color.red_error));
    }

    private void updateSdkStatus() {
        if (!SmartAds.isInitialized()) {
            textSdkStatus.setText(R.string.sdk_not_initialized);
            textSdkStatus.setTextColor(ContextCompat.getColor(this, R.color.red_error));
            return;
        }
        com.partharoypc.smartads.SmartAdsConfig config = SmartAds.getInstance().getConfig();
        boolean isTestMode = config.isTestMode();
        boolean areAdsEnabled = config.isAdsEnabled();
        String version = SmartAds.getVersion();

        String mode = isTestMode ? getString(R.string.mode_test) : getString(R.string.mode_prod);
        textSdkStatus.setText(getString(R.string.sdk_status_format, version, mode));

        if (!areAdsEnabled) {
            textSdkStatus.append("\n⛔ Ads Disabled");
            textSdkStatus.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        } else {
            textSdkStatus.setTextColor(0xFFF0F0F0);
        }

        // Sync Switches without triggering listeners loops (simple check)
        if (switchEnableAds.isChecked() != areAdsEnabled) {
            switchEnableAds.setChecked(areAdsEnabled);
        }

        if (switchMediationFacebook.isChecked() != config.isFacebookMediationEnabled())
            switchMediationFacebook.setChecked(config.isFacebookMediationEnabled());

        if (switchMediationAppLovin.isChecked() != config.isAppLovinMediationEnabled())
            switchMediationAppLovin.setChecked(config.isAppLovinMediationEnabled());

        if (switchMediationUnity.isChecked() != config.isUnityMediationEnabled())
            switchMediationUnity.setChecked(config.isUnityMediationEnabled());
    }

    private void toggleAds(boolean enabled) {
        SmartAds.getInstance().setAdsEnabled(enabled);
        updateSdkStatus();
        showSnackbar("Ads " + (enabled ? "Enabled" : "Disabled"));
    }

    private void updateMediationConfig(boolean fb, boolean applovin, boolean unity) {
        if (!SmartAds.isInitialized())
            return;
        // Create new config based on current one
        com.partharoypc.smartads.SmartAdsConfig current = SmartAds.getInstance().getConfig();
        com.partharoypc.smartads.SmartAdsConfig newConfig = current.toBuilder()
                .setFacebookMediationEnabled(fb)
                .setAppLovinMediationEnabled(applovin)
                .setUnityMediationEnabled(unity)
                .build();

        SmartAds.getInstance().updateConfig(newConfig);
        log("Updated Mediation Config: FB=" + fb + ", AL=" + applovin + ", Unity=" + unity);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            updateAdStatuses();
        }
    }

    private void updateAdStatuses() {
        if (!SmartAds.isInitialized())
            return;

        // App Open Ad Status Update
        if (SmartAds.getInstance().isAppOpenAdAvailable()) {
            setStatus(statusAppOpen, "Available", R.color.green_success);
            btnShowAppOpen.setEnabled(true);
        } else {
            setStatus(statusAppOpen, SmartAds.getInstance().getAppOpenAdStatus().name(), R.color.text_secondary);
        }

        // Interstitial Status
        if (SmartAds.getInstance().isInterstitialAdAvailable()) {
            setStatus(statusInterstitial, "READY", R.color.green_success);
            btnShowInterstitial.setEnabled(true);
        } else {
            // Show actual status (LOADING, FAILED, IDLE, etc.)
            String status = SmartAds.getInstance().getInterstitialAdStatus().name();
            setStatus(statusInterstitial, status, R.color.text_secondary);
            btnShowInterstitial.setEnabled(false);
        }

        // Rewarded Status
        if (SmartAds.getInstance().isRewardedAdAvailable()) {
            setStatus(statusRewarded, "READY", R.color.green_success);
            btnShowRewarded.setEnabled(true);
        } else {
            String status = SmartAds.getInstance().getRewardedAdStatus().name();
            setStatus(statusRewarded, status, R.color.text_secondary);
            btnShowRewarded.setEnabled(false);
        }
    }

    // Helper to set status with color
    private void setStatus(TextView view, String text, int colorResId) {
        view.setText(text);
        view.setTextColor(ContextCompat.getColor(this, colorResId));
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.surface_card))
                .setTextColor(ContextCompat.getColor(this, R.color.text_primary))
                .show();
    }

    // ================= BANNER =================
    private void loadBanner(boolean collapsible) {
        log("Loading Banner (Collapsible: " + collapsible + ")...");

        // Update config to enable/disable collapsible banner for this request
        com.partharoypc.smartads.SmartAdsConfig current = SmartAds.getInstance().getConfig();
        SmartAds.getInstance().updateConfig(current.toBuilder()
                .setCollapsibleBannerEnabled(collapsible)
                .build());

        // Ensure container is visible
        bannerContainer.setVisibility(View.VISIBLE);

        SmartAds.getInstance().showBannerAd(this, bannerContainer, new BannerAdListener() {
            @Override
            public void onAdLoaded(View adView) {
                log("Banner Loaded");
            }

            @Override
            public void onAdFailed(String errorMessage) {
                log("Banner Failed: " + errorMessage);
                showSnackbar("Banner Failed to load");
            }
        });
    }

    // ================= INTERSTITIAL =================
    private void loadInterstitial() {
        log("Loading Interstitial...");
        setStatus(statusInterstitial, "Loading...", R.color.text_primary);
        btnShowInterstitial.setEnabled(false); // Disable until loaded

        SmartAds.getInstance().loadInterstitialAd(this);

        // Check availability after a short delay to simulate "polling"
        // In a real scenario, you might have a listener or check in onResume
        statusInterstitial.postDelayed(this::updateAdStatuses, 2000);
    }

    private void showInterstitial() {
        if (SmartAds.getInstance().isInterstitialAdAvailable()) {
            SmartAds.getInstance().showInterstitialAd(this, new InterstitialAdListener() {
                @Override
                public void onAdDismissed() {
                    log("Interstitial Dismissed");
                    setStatus(statusInterstitial, "Dismissed", R.color.text_secondary);
                    btnShowInterstitial.setEnabled(false);
                }

                @Override
                public void onAdFailedToShow(String errorMessage) {
                    log("Interstitial Failed to Show: " + errorMessage);
                    setStatus(statusInterstitial, "Failed", R.color.red_error);
                }
            });
        } else {
            log("Interstitial not ready yet.");
            showSnackbar("Interstitial ad not ready");
        }
    }

    // ================= REWARDED =================
    private void loadRewarded() {
        log("Loading Rewarded Ad...");
        setStatus(statusRewarded, "Loading...", R.color.text_primary);
        btnShowRewarded.setEnabled(false);

        SmartAds.getInstance().loadRewardedAd(this);

        statusRewarded.postDelayed(this::updateAdStatuses, 2000);
    }

    private void showRewarded() {
        if (SmartAds.getInstance().isRewardedAdAvailable()) {
            SmartAds.getInstance().showRewardedAd(this, new RewardedAdListener() {
                @Override
                public void onUserEarnedReward() {
                    log("User Earned Reward!");
                    showSnackbar("🎁 Reward Earned!");
                }

                @Override
                public void onAdDismissed() {
                    log("Rewarded Ad Dismissed");
                    setStatus(statusRewarded, "Dismissed", R.color.text_secondary);
                    btnShowRewarded.setEnabled(false);
                }

                @Override
                public void onAdFailedToShow(String errorMessage) {
                    log("Rewarded Failed: " + errorMessage);
                    setStatus(statusRewarded, "Failed", R.color.red_error);
                }
            });
        } else {
            log("Rewarded Ad not ready yet.");
            showSnackbar("Rewarded ad not ready");
        }
    }

    // ================= NATIVE =================
    private void loadNativeAd() {
        int selectedId = radioGroupNativeSize.getCheckedRadioButtonId();

        log("Loading Native Ad...");

        // Clear previous
        SmartAds.getInstance().clearNativeIn(nativeContainer);

        if (selectedId == R.id.rb_custom) {
            // Load Custom Layout
            log("Loading Custom Native Layout...");
            SmartAds.getInstance().showNativeAd(this, nativeContainer, R.layout.layout_custom_native_ad,
                    new NativeAdListener() {
                        @Override
                        public void onAdLoaded(View nativeAdView) {
                            log("Custom Native Ad Loaded");
                        }

                        @Override
                        public void onAdFailed(String errorMessage) {
                            log("Custom Native Ad Failed: " + errorMessage);
                            showSnackbar("Native ad failed to load");
                        }
                    });
        } else {
            // Load Standard Template
            NativeAdSize size = NativeAdSize.MEDIUM;
            if (selectedId == R.id.rb_small)
                size = NativeAdSize.SMALL;
            else if (selectedId == R.id.rb_large)
                size = NativeAdSize.LARGE;

            log("Loading Standard Native Ad (" + size.name() + ")...");
            SmartAds.getInstance().showNativeAd(this, nativeContainer, size, new NativeAdListener() {

                @Override
                public void onAdLoaded(View nativeAdView) {
                    log("Native Ad Loaded");
                }

                @Override
                public void onAdFailed(String errorMessage) {
                    log("Native Ad Failed: " + errorMessage);
                    showSnackbar("Native ad failed to load");
                }
            });
        }
    }

    // ================= HELPER =================
    private void log(String message) {
        String time = DateFormat.format("HH:mm:ss", new Date()).toString();
        // Use green color in log view (handled by setup but we just append text here)
        String fullMsg = time + ": " + message + "\n";
        textLogger.append(fullMsg);

        // Auto scroll
        logScrollView.post(() -> logScrollView.fullScroll(View.FOCUS_DOWN));
        Log.d("SmartAdsTest", message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (SmartAds.isInitialized()) {
            SmartAds.getInstance().destroyBannerIn(bannerContainer);
            SmartAds.getInstance().clearNativeIn(nativeContainer);
        }
    }
}
