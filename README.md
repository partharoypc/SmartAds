<p align="center">
  <img src="logo/logo.png" alt="SmartAds Logo" width="200"/>
</p>

# 📱 SmartAds Android Library

**SmartAds** is a powerful, lightweight, and professional wrapper around the Google Mobile Ads SDK (AdMob). It simplifies the integration of Banner, Interstitial, Rewarded, App Open, and Native ads into your Android application with a clean, centralized configuration and lifecycle-safe implementation.

[![](https://jitpack.io/v/partharoypc/SmartAds.svg)](https://jitpack.io/#partharoypc/SmartAds)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
[![Google Mobile Ads](https://img.shields.io/badge/SDK-AdMob_24.9.0-blue.svg)]()

---

## ✨ Features

- **🚀 One-Tap Initialization**: Centralized configuration in your `Application` class.
- **🛡️ Lifecycle-Aware**: Automatically handles Activity context to prevent memory leaks and crashes.
- **🖼️ All Ad Formats Supported**: Banner, Interstitial, Rewarded, App Open, and Native Ads.
- **🔧 High-Performing Banners**: Support for adaptive and collapsible banners.
- **🎨 Native Ad Templates**: Pre-built Small, Medium, and Large templates for seamless UI integration.
- **🧪 Intelligent Test Mode**: Automatic toggle between production IDs and Google's official Test IDs.
- **⚡ Smart Pre-fetching**: Automatic caching for Interstitial, Rewarded, and App Open ads.
- **📢 Consent Management**: Seamless integration with Google User Messaging Platform (UMP).
- **🎛️ Dynamic Control**: Global master switch, per-ad-type status checks, and **Frequency Capping**.
- **🌑 Privacy Focused**: Ads are disabled by default until explicitly enabled.

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
    implementation 'com.github.partharoy:SmartAds:3.0.0' // Check JitPack for latest
    implementation 'com.google.android.gms:play-services-ads:24.9.0' 
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

        SmartAds.initialize(this, new SmartAdsConfig.Builder()
            // 🛑 Set your REAL Ad Unit IDs (The library handles Test Mode automatically)
            .setAdMobAppOpenId("ca-app-pub-3940256099942544/3419835294")
            .setAdMobBannerId("ca-app-pub-3940256099942544/6300978111")
            .setAdMobInterstitialId("ca-app-pub-3940256099942544/1033173712")
            .setAdMobRewardedId("ca-app-pub-3940256099942544/5224354917")
            .setAdMobNativeId("ca-app-pub-3940256099942544/2247696110")
            
            // ⚙️ General Configuration
            .setAdsEnabled(true)              // Master switch (DEFAULT is false)
            .enableTestMode(true)             // Set 'false' for Production release
            .enableCollapsibleBanner(true)    // Enable specialized collapsible banners
            .setUseUmpConsent(true)           // Show Google UMP Consent form at start
            .setFrequencyCap(30)              // Minimum seconds between full-screen ads
            
            // 🎨 Optional: Styling for full-screen loading dialogs
            .setLoadingDialogText("Preparing your reward...")
            .setLoadingDialogColor(Color.parseColor("#121212"), Color.WHITE)
            
            // 👶 Children's Privacy (Optional)
            .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
            
            .build());
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
Native ads allow you to match the look and feel of your app perfectly. We provide 3 standard templates.

**Show with Templates:**
```java
// Sizes: NativeAdSize.SMALL, MEDIUM, or LARGE
SmartAds.getInstance().showNativeAd(this, nativeContainer, NativeAdSize.MEDIUM, new NativeAdListener() {
    @Override
    public void onAdLoaded() {}
    @Override
    public void onAdFailed(String error) {}
});
```

**Show with Custom Layout:**
If you want 100% control, pass your own layout resource:
```java
SmartAds.getInstance().showNativeAd(this, container, R.layout.my_ad_layout, listener);
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
| `setAdsEnabled(boolean)` | Enable/Disable ads dynamically (e.g., after Pro purchase). |
| `areAdsEnabled()` | Quick check for ad status. |
| `isAnyAdShowing()` | Returns true if an Interstitial/Rewarded/AppOpen is currently on screen. |
| `isPrivacyOptionsRequired()` | Check if UMP privacy options need to be shown to the user. |
| `showPrivacyOptionsForm(Activity)`| Manually trigger the UMP privacy settings form. |
| `launchAdInspector(Activity)`| Open the Google Ad Inspector for debugging. |
| `destroyBannerIn(container)`| Recommended for `onDestroy()` to prevent leaks. |

### Configuration Options
| Builder Method | Purpose |
|----------------|---------|
| `addTestDeviceId(String)` | Add a device ID for safe testing on real hardware. |
| `setFrequencyCap(long)` | Minimum interval (seconds) between full-screen ads. |
| `setTagForChildDirectedTreatment(int)` | Compliance for COPPA. |
| `setTagForUnderAgeOfConsent(int)` | Compliance for GDPR under-age users. |
| `setLoadingDialogText(String)` | Change the message shown while loading full-screen ads. |

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
