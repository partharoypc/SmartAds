Smart Ads Library for AndroidA powerful, developer-friendly Android library designed to simplify ad integration, maximize revenue, and improve user experience. It provides a robust primary/backup system using AdMob and Meta, with extensive customization options.✨ FeaturesPrimary/Backup System: Automatically uses Google AdMob as the primary ad source and fails over to Meta Audience Network if AdMob fails.All Major Ad Formats:App OpenInterstitialRewardedBannerNativeGlobal Ad Control: A single master switch to enable or disable all ads at runtime.Frequency Capping: Control the minimum time interval between interstitial ads to prevent spamming users.Install-Day Gating: Prevent ads from showing for a set number of days after the app is first installed.Automatic Test Mode: A simple flag to switch all ad unit IDs to Google's and Meta's test IDs.Loading Animation: An optional, clean loading dialog to show while ads are being prepared.Centralized Configuration: A clean builder pattern to set up all your ad IDs and settings in one place.🛠️ Setup1. Add DependenciesAdd the following dependencies to your app-level build.gradle file:dependencies {
    // ... other dependencies

    // Required for Google Mobile Ads (AdMob)
    implementation("com.google.android.gms:play-services-ads:24.5.0")

    // Required for Meta Audience Network
    implementation("com.facebook.android:audience-network-sdk:6.+")

    // Required for App Open Ads and Lifecycle Management
    implementation("androidx.lifecycle:lifecycle-process:2.9.2")
}

2. Update AndroidManifest.xmlAdd your AdMob App ID inside the <application> tag.<manifest>
    <application>
        <!-- ... other tags -->

        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="YOUR_ADMOB_APP_ID_HERE"/>

    </application>
</manifest>

🚀 InitializationInitialize the library once in your custom Application class. This ensures that all settings and ad managers are ready when your app starts.MyApplication.javaimport android.app.Application;
import com.partharoy.smartads.SmartAds;
import com.partharoy.smartads.SmartAdsConfig;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SmartAds.initialize(this, new SmartAdsConfig.Builder()
                // Set all your Ad Unit IDs
                .setAdMobAppId("ca-app-pub-3940256099942544~3347511713")
                .setAdMobAppOpenId("ca-app-pub-3940256099942544/3419835294")
                // ... add other AdMob and Meta IDs

                // Configure library settings
                .enableTestMode(true) // Set true for development, false for release
                .setAdsEnabled(true)  // Master switch for all ads
                .setShowAdsAfterDays(0) // Show ads immediately
                .setAdShowIntervalMillis(30000) // 30-second gap between ads
                .build());
    }
}

Important: Remember to register your MyApplication class in your AndroidManifest.xml:<application
    android:name=".MyApplication"
    ...>
</application>

💡 Usage ExamplesOnce initialized, you can call ads from any Activity with a single line.Banner AdFrameLayout bannerContainer = findViewById(R.id.banner_ad_container);
SmartAds.getInstance().showBannerAd(this, bannerContainer, new BannerAdListener() {
    @Override
    public void onAdLoaded(View adView) {
        // Ad loaded and displayed
    }

    @Override
    public void onAdFailed(String errorMessage) {
        // Ad failed to load
    }
});

Interstitial AdFirst, pre-load the ad. Then, show it when needed.// Pre-load the ad (e.g., in onCreate)
SmartAds.getInstance().loadInterstitialAd(this);

// Show the ad (e.g., on a button click)
SmartAds.getInstance().showInterstitialAd(this, new InterstitialAdListener() {
    @Override
    public void onAdDismissed() {
        // Ad was closed, continue app flow
        // Pre-load the next one
        SmartAds.getInstance().loadInterstitialAd(MainActivity.this);
    }

    @Override
    public void onAdFailedToShow(String errorMessage) {
        // Ad failed, continue app flow
    }
});

Rewarded AdSimilar to Interstitial, you pre-load and then show the ad.// Pre-load the ad
SmartAds.getInstance().loadRewardedAd(this);

// Show the ad to the user
SmartAds.getInstance().showRewardedAd(this, new RewardedAdListener() {
    @Override
    public void onUserEarnedReward() {
        // User watched the ad completely. Grant the reward.
    }

    @Override
    public void onAdDismissed() {
        // Ad was closed
        SmartAds.getInstance().loadRewardedAd(MainActivity.this);
    }

    @Override
    public void onAdFailedToShow(String errorMessage) {
        // Ad failed to show
    }
});

Native AdFrameLayout nativeContainer = findViewById(R.id.native_ad_container);
SmartAds.getInstance().showNativeAd(this, nativeContainer, R.layout.native_ad_layout, new NativeAdListener() {
    @Override
    public void onAdLoaded(View nativeAdView) {
        // Native ad loaded and displayed successfully
    }

    @Override
    public void onAdFailed(String errorMessage) {
        // Native ad failed to load
    }
});

⚙️ Configuration OptionsAll options are available through the SmartAdsConfig.Builder():| Method | Description | Default || setAdsEnabled(boolean) | A master switch to turn all ads on/off at runtime. | true || enableTestMode(boolean) | If true, forces the use of test ad unit IDs for all networks. | false || setShowAdsAfterDays(int) | Number of days to wait after first app install before showing any ads. | 0 || setAdShowIntervalMillis(long) | Minimum time in milliseconds between showing interstitial ads. | 60000 || setShowLoadingDialog(boolean) | If true, shows a simple loading dialog while ads are being fetched. | true || setUseMetaBackup(boolean) | If true, attempts to load a Meta ad if AdMob fails. | true || setAdMob...Id(String) | Sets the Ad Unit ID for a specific AdMob ad format. | "" || setMeta...Id(String) | Sets the Placement ID for a specific Meta ad format. | "" |📄 LicenseThis library is licensed under the MIT License. See the LICENSE file for details.