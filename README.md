<p align="center">
  <img src="logo/logo.png" alt="SmartAds Logo" width="200"/>
</p>

# 📱 SmartAds Android Library v5.0.0

**SmartAds** is a powerful, lightweight, and professional wrapper around the Google Mobile Ads SDK (AdMob). It simplifies the integration of Banner, Interstitial, Rewarded, App Open, and Native ads into your Android application with a clean, centralized configuration and lifecycle-safe implementation.

[![](https://jitpack.io/v/partharoypc/SmartAds.svg)](https://jitpack.io/#partharoypc/SmartAds)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
[![Google Mobile Ads](https://img.shields.io/badge/SDK-AdMob_24.0.0-blue.svg)]()

---

## ✨ Features

- **🚀 One-Line Initialization**: Centralized configuration in your `Application` class.
- **🛡️ Lifecycle-Aware**: Automatically handles Activity context to prevent memory leaks and crashes.
- **🖼️ All Ad Formats Supported**: Banner, Interstitial, Rewarded, App Open, and Native Ads.
- **🎨 Custom Native Layouts**: Use standard templates or your own completely custom XML layouts.
- **� House Ads System**: Native support for cross-promotion with rich UI (Icon, Title, Rating, CTA) and weighted random selection.
- **🔧 High-Performing Banners**: Support for adaptive and collapsible banners.
- **🧪 Intelligent Test Mode**: Automatic toggle between production IDs and Google's official Test IDs.
- **⚡ Smart Pre-fetching**: Automatic caching for Interstitial, Rewarded, and App Open ads.
- **📢 Consent Management**: Seamless integration with Google User Messaging Platform (UMP).
- **🎛️ "Defaults Off" Philosophy**: All intrusive features are opt-in for maximum safety and performance.
- **🕵️ Ad Inspector**: Easy access to AdMob debug tools.

---

## 🛠️ Installation

### 1. Add JitPack Repository
Add the JitPack maven repository to your `settings.gradle` file:

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. Add Dependencies
Add the SmartAds library and the Google Mobile Ads SDK to your app-level `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.partharoypc:SmartAds:5.0.0' // Check JitPack for latest
    implementation 'com.google.android.gms:play-services-ads:24.0.0'
}
```

### 3. Configure AndroidManifest.xml
Add your AdMob App ID inside the `<application>` tag. Failure to do this will result in a crash on startup.

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/> 
```

---

## 🚀 Step 1: Initialization

Initialize the library in your `Application` class. This setup ensures that your ad units are correctly registered and pre-fetching begins immediately.

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SmartAdsConfig config = new SmartAdsConfig.Builder()
            // 🛑 Set your REAL Ad Unit IDs (The library handles Test Mode automatically)
            .setAdMobAppOpenId("ca-app-pub-xxxxxxxx/yyyyyyyy")
            .setAdMobBannerId("ca-app-pub-xxxxxxxx/yyyyyyyy")
            .setAdMobInterstitialId("ca-app-pub-xxxxxxxx/yyyyyyyy")
            .setAdMobRewardedId("ca-app-pub-xxxxxxxx/yyyyyyyy")
            .setAdMobNativeId("ca-app-pub-xxxxxxxx/yyyyyyyy")
            
            // ⚙️ General Configuration (Defaults are FALSE/OFF for safety)
            .setAdsEnabled(true)              // Master switch
            .setTestModeEnabled(true)         // Set 'false' for Production release
            .setLoggingEnabled(true)          // Enable detailed logs for debugging
            .setCollapsibleBannerEnabled(true)// Enable specialized collapsible banners
            .setUseUmpConsent(true)           // Show Google UMP Consent form at start
            .setFrequencyCapSeconds(30)       // Minimum seconds between full-screen ads
            
            // 🏠 House Ads Configuration (Optional)
            .setHouseAdsEnabled(true)         // Enable House Ads fallback
            .addHouseAd(new HouseAd.Builder()
                .setId("promo_app_1")
                .setTitle("Check out My Other App!")
                .setDescription("The best app for...")
                .setRating(4.8f)
                .setCtaText("Install Now")
                .setIconResId(R.drawable.my_app_icon)
                .setImageResId(R.drawable.promo_banner)
                .setClickUrl("https://play.google.com/store/apps/details?id=com.example.otherapp")
                .build())
            
            // 🎨 Optional: Styling for full-screen loading dialogs
            .setLoadingDialogText("Preparing amazing content...")
            .setLoadingDialogColor(Color.parseColor("#121212"), Color.WHITE)
            
            .build();

        SmartAds.initialize(this, config);
    }
}
```

---

## 💡 Step 2: Implementation Guide

### 1. Banner Ads
Banners are easy to integrate. Simply provide a container (e.g., `FrameLayout`) in your Layout XML:

```xml
<FrameLayout
    android:id="@+id/banner_container"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

**Show Banner:**
```java
SmartAds.getInstance().showBannerAd(this, bannerContainer, new BannerAdListener() {
    @Override
    public void onAdLoaded(View adView) {}

    @Override
    public void onAdFailed(String error) {}
});
```

### 2. Interstitial Ads
Interstitial ads should be loaded once and shown when a user reaches a transition point.

**Load:** `SmartAds.getInstance().loadInterstitialAd(this);`

**Check Status:** `if (SmartAds.getInstance().isInterstitialAdAvailable()) { ... }`

**Show:**
```java
SmartAds.getInstance().showInterstitialAd(this, new InterstitialAdListener() {
    @Override
    public void onAdDismissed() {
        // Continue to the next screen
    }
    @Override
    public void onAdFailedToShow(String error) {
        // Move on even if ad fails
    }
});
```

### 3. Rewarded Ads
Perfect for incentivizing users. SmartAds handles the loading dialog for you automatically.

**Load:** `SmartAds.getInstance().loadRewardedAd(this);`

**Show:**
```java
SmartAds.getInstance().showRewardedAd(this, new RewardedAdListener() {
    @Override
    public void onUserEarnedReward() {
        // 💰 Grant user points/currency here
    }
    @Override
    public void onAdDismissed() {}
});
```

### 4. Native Ads
Native ads allow you to match the look and feel of your app perfectly.

**Show with Standard Template:**
```java
// Sizes: NativeAdSize.SMALL, MEDIUM, or LARGE
SmartAds.getInstance().showNativeAd(this, nativeContainer, NativeAdSize.MEDIUM, new NativeAdListener() {
    @Override
    public void onAdLoaded(View adView) {}
    @Override
    public void onAdFailed(String error) {}
});
```

**Show with Custom XML Layout:**
If you want 100% control, design your own XML layout (must be a `NativeAdView`) and pass the resource ID:
```java
SmartAds.getInstance().showNativeAd(this, container, R.layout.my_custom_native_ad, listener);
```

### 5. App Open Ads
App Open ads are automatically prefetched and shown. You can also trigger them manually:
```java
SmartAds.getInstance().showAppOpenAd(this);
```

---

## ⚙️ Advanced Configuration

### SmartAds Methods
| Method | Description |
|--------|-------------|
| `getInstance()` | Returns the singleton instance. |
| `getVersion()` | Returns the current library version (e.g., "5.0.0"). |
| `setAdsEnabled(boolean)` | Enable/Disable ads dynamically (e.g., after Pro purchase). |
| `areAdsEnabled()` | Quick check for ad status. |
| `isInterstitialAdAvailable()` | Returns true if an Interstitial ad is loaded and ready. |
| `isRewardedAdAvailable()` | Returns true if a Rewarded ad is loaded and ready. |
| `isPrivacyOptionsRequired()` | Check if UMP privacy options need to be shown to the user. |
| `showPrivacyOptionsForm(Activity)`| Manually trigger the UMP privacy settings form. |
| `launchAdInspector(Activity)`| Open the Google Ad Inspector for debugging. |
| `verifyMediation(Context)` | Logs status of configured mediation adapters. |
| `destroyBannerIn(container)`| Recommended for `onDestroy()` to prevent leaks. |
| `clearNativeIn(container)`| Clears native ads from container to prevent leaks. |

### Configuration Options (Builder)
| Method | Purpose |
|--------|---------|
| `setAdMob...Id(String)` | Set your specific ad unit IDs. |
| `addTestDeviceId(String)` | Add a device ID for safe testing on real hardware. |
| `setFrequencyCapSeconds(long)` | Minimum interval (seconds) between full-screen ads. |
| `setLoggingEnabled(boolean)` | Enable internal debug logs. |
| `setFacebookMediationEnabled(boolean)` | Enable Facebook Audience Network checks. |
| `setAppLovinMediationEnabled(boolean)` | Enable AppLovin MAX checks. |
| `setUnityMediationEnabled(boolean)` | Enable Unity Ads checks. |
| `setHouseAdsEnabled(boolean)` | Enable or disable the House Ads fallback system. |
| `addHouseAd(HouseAd)` | Add a custom internal ad to show when networks fail. |
| `setTagForChildDirectedTreatment(...)` | Compliance for COPPA. |
| `setTagForUnderAgeOfConsent(...)` | Compliance for GDPR under-age users. |

---

## 🧹 Lifecycle Management
To ensure a clean application state, call these helpers in your `onDestroy()`:

```java
@Override
protected void onDestroy() {
    SmartAds.getInstance().destroyBannerIn(bannerContainer);
    SmartAds.getInstance().clearNativeIn(nativeContainer);
    super.onDestroy();
}
```

---

## 🤝 Contributing & Support
If you find a bug or have a feature request, please open an issue on the GitHub repository.

## 📄 License
This library is licensed under the **MIT License**. Feel free to use it in any project.
