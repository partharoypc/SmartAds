<p align="center">
  <img src="logo/logo.png" alt="SmartAds Logo" width="200"/>
</p>

# SmartAds Android Library 🚀

**SmartAds** is a professional-grade, lightweight, and developer-friendly Android wrapper for the Google Mobile Ads SDK (AdMob). It streamlines complex ad integrations into simple, lifecycle-aware components, allowing you to focus on building features while we handle the monetization.

[![](https://jitpack.io/v/partharoypc/SmartAds.svg)](https://jitpack.io/#partharoypc/SmartAds)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
[![Google Mobile Ads](https://img.shields.io/badge/SDK-AdMob_25.0.0-blue.svg)]()

---

## 🚀 What's New in v6.0.0 (The Perfection Update)
- **Zero Request Policy:** Absolute control. If a granular switch (e.g., `setBannerEnabled(false)`) is off, **zero** network requests are sent.
- **Strict Impression Mode:** Guaranteed 95%+ Impression Rates. Auto-caching is disabled for Interstitial/Rewarded ads in favor of **Just-In-Time (JIT) Loading** to completely eliminate wasted "ghost requests".
- **Bulletproof Policy Compliance:** 5-second hard timeouts for loading dialogs, strict click debouncing, and 100% AdMob "Site Behavior" visual compliance for House/Native ads.
- **Upgraded SDK:** Now running Google Mobile Ads SDK `25.0.0`.

---

## 🤔 Why SmartAds?

Integrating ads can be messy—handling context, memory leaks, and pre-fetching often leads to boilerplate-heavy code. **SmartAds** solves this by:
- **Reducing Boilerplate**: From 150+ lines of AdMob code to just 1-2 lines.
- **Lifecycle Safety**: Built-in protection against Activity-based memory leaks.
- **Smart Pre-fetching**: Automatically caches ads so they're ready the moment you need them.
- **House Ads Fallback**: Never miss an impression—show your own promotions if the network fails.

---

## 🌟 Key Features

- ✅ **Unified Initialization**: One-time setup in your `Application` class.
- 📱 **Complete Format Support**: App Open, Banner (Adaptive/Collapsible), Interstitial, Rewarded, and Native Ads.

- 🏠 **House Ads System**: Native fallbacks for internal cross-promotion.
- 📜 **Privacy First**: Built-in Google UMP (GDPR/CCPA) consent management.
- 🧪 **Test Mode**: Automatically handles AdMob Test IDs during development.
- 🔍 **Debug Suite**: Integrated Ad Inspector and detailed logging.
- � **Offline Smart-Check**: Automatically skips network calls when offline and switches to House Ads immediately.
- 🧹 **Optimized ProGuard**: Smart rules save users ~30MB by stripping unused AdMob code.
- �💰 **Paid Event Tracking**: Simple hook for revenue analytics (Firebase, AppsFlyer, etc.).

---

## 🚀 1. Installation

### Project Level (`settings.gradle`)
```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### App Level (`build.gradle`)
```gradle
dependencies {
    implementation 'com.github.partharoypc:SmartAds:6.0.0'
    implementation 'com.google.android.gms:play-services-ads:25.0.0'

    // SmartAds automatically handles other internal dependencies.
}
```

### 🔄 Upgrading to v6.0.0 (Important)
When you change the version number from `5.6.0` to `6.0.0` and sync:
1. **Automatic Replacement:** Gradle completely removes the old library files and downloads the new `6.0.0` version automatically.
2. **Force Clean Cache:** If you experience any weird resource issues after updating, simply navigate to **Build -> Clean Project** in Android Studio to wipe the old cache.
3. **Deprecations Removed:** Any old discarded classes or unused AdMob imports from v5.x have been permanently deleted inside the library. If your app relied on them, you will get a compile error prompting you to use the new, cleaner v6 APIs.

### Manifest (`AndroidManifest.xml`)
```xml
<!-- Ad And Internet Permission -->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>

<!-- Admob Meta Data (inside the application tag) -->
<meta-data
  android:name="com.google.android.gms.ads.APPLICATION_ID"
      android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
```

---

## ✅ Quick Integration Checklist
Before you start, make sure you've:
1. [ ] Added the JitPack repository.
2. [ ] Added the `SmartAds` and `play-services-ads` dependencies.
3. [ ] Added your **AdMob App ID** to `AndroidManifest.xml`.
4. [ ] Initialized the SDK in your `Application` class.

---

## ⚙️ 2. Configuration & Initialization

Initialize in your `Application` class using `SmartAdsConfig`.

```java
SmartAdsConfig config = new SmartAdsConfig.Builder()
        .setAdsEnabled(true)        // Global Master Switch to enable/disable all ads
        .setInterstitialEnabled(true) // Granular switch for Interstitials
        .setRewardedEnabled(true)   // Granular switch for Rewarded ads
        .setAppOpenEnabled(true)    // Granular switch for App Open
        .setBannerEnabled(true)     // Granular switch for Banners
        .setNativeEnabled(true)     // Granular switch for Native ads
        .setTestModeEnabled(true)   // Uses Google Test IDs when enabled
        .setAdMobAppOpenId("AD_UNIT_ID")
        .setAdMobBannerId("AD_UNIT_ID")
        .setAdMobInterstitialId("AD_UNIT_ID")
        .setAdMobRewardedId("AD_UNIT_ID")
        .setAdMobNativeId("AD_UNIT_ID")
        .setUseUmpConsent(true)     // Enable GDPR/UMP consent flow
        .setFrequencyCapSeconds(30) // Min time between full-screen ads
        .build();

SmartAds.initialize(this, config);
```

### Advanced Config Options
| Method | Description |
|--------|-------------|
| `setLoggingEnabled(boolean)` | Enable internal debug logs. |
| `setCollapsibleBannerEnabled(boolean)` | Enable collapsible banner feature. |

| `setLoadingDialogText(String headline, String sub)` | Custom headline and sub-text for valid loading dialog. |
| `setLoadingDialogColor(bg, text)` | Custom background and text/headline colors. |
| `setLoadingDialogSubTextColor(int)` | Custom color for the sub-text (default is secondary text color). |
| `setLoadingDialogProgressColor(int)` | Custom color for the circular progress indicator. |
| `setMaxAdContentRating(String)` | Set content rating: `G`, `PG`, `T`, `MA`. |
| `setTagForChildDirectedTreatment(int)` | COPPA compliance (`TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE`). |
| `setTagForUnderAgeOfConsent(int)` | GDPR compliance (`TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE`). |
| `addTestDeviceId(String)` | Add a specific physical device for test ads. |
| `setHouseAdsEnabled(boolean)` | Globally enable or disable internal promotions. |

---

## 📺 3. Ad Implementation

### **Banner Ads**
```java
// Just pass the activity and a FrameLayout container
SmartAds.getInstance().showBannerAd(activity, container, new BannerAdListener() {
    @Override public void onAdLoaded(View adView) {}
    @Override public void onAdFailed(String error) {}
});
```

### **Interstitial Ads**
```java
// Preload once (ideally in onCreate or after previous ad)
SmartAds.getInstance().loadInterstitialAd(context); 

// Show when the user completes an action
if (SmartAds.getInstance().isInterstitialAdAvailable()) {
    SmartAds.getInstance().showInterstitialAd(activity, new InterstitialAdListener() {
        @Override public void onAdDismissed() {
            // Proceed to the next screen
        }
    });
}
```

### **Rewarded Ads**
```java
SmartAds.getInstance().showRewardedAd(activity, new RewardedAdListener() {
    @Override public void onUserEarnedReward() { 
        // 🎁 Grant the reward! 
    }
});
```

### **Native Ads**
```java
// Standard Template (MEDIUM, SMALL, or LARGE)
SmartAds.getInstance().showNativeAd(activity, container, NativeAdSize.MEDIUM, null);

// Custom XML Layout (Must be a NativeAdView in your XML)
SmartAds.getInstance().showNativeAd(activity, container, R.layout.my_layout, null);
```

---

## 🛠️ Common Implementation Patterns

### **Showing an Ad Before Navigating**
```java
void onNextButtonClicked() {
    if (SmartAds.getInstance().isInterstitialAdAvailable()) {
        SmartAds.getInstance().showInterstitialAd(this, new InterstitialAdListener() {
            @Override
            public void onAdDismissed() {
                startNextActivity();
            }
        });
    } else {
        startNextActivity();
    }
}
```

---

## 🏠 4. House Ads (Internal Cross-Promotion)
Add your own app promotions to show when network ads are unavailable or for direct marketing.

```java
.addHouseAd(new HouseAd.Builder()
        .setId("banner_house")
        .setTitle("Try Our New App!")
        .setDescription("Download now for free.")
        .setClickUrl("GP_LINK")
        .setIconResId(R.drawable.icon)
        .setImageResId(R.drawable.banner)
        .setRating(5.0f)
        .build())
.setHouseAdsEnabled(true)
```

---

## 📊 5. Analytics & Paid Events
Track revenue with precision by hooking into the analytics listener.

```java
SmartAds.getInstance().setAnalyticsListener((adUnitId, adFormat, adNetwork, valueMicros, currencyCode, precision, extras) -> {
    // Send data to Firebase, AppsFlyer, etc.
    // revenue = valueMicros / 1000000.0;
});
```

---

## 🤝 6. Debugging & Verification

SmartAds provides built-in tools to ensure your ad setup is working correctly:

#### 1. AdMob Ad Inspector
Launch the official AdMob inspector to see real-time ad fill status and network behavior:
```java
SmartAds.getInstance().launchAdInspector(activity);
```

---

### Example: Complete Setup


**Scenario**: You want to set up Google AdMob with Banner and Interstitial Ads.

**App's `build.gradle`**:
```gradle
dependencies {
    implementation 'com.github.partharoypc:SmartAds:6.0.0'
}
```

**AndroidManifest.xml**:
```xml
<application>
    <!-- AdMob App ID (Required) -->
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
</application>
```

**Application class**:
```java
SmartAdsConfig config = new SmartAdsConfig.Builder()
    .setAdsEnabled(true)
    .setTestModeEnabled(BuildConfig.DEBUG)
    .setLoggingEnabled(BuildConfig.DEBUG)
    
    // Your ad unit IDs
    .setAdMobBannerId("ca-app-pub-xxx/banner")
    .setAdMobInterstitialId("ca-app-pub-xxx/interstitial")
    .build();

SmartAds.initialize(this, config);
```

---

## 🛡️ 7. API Reference (Public Methods)

### Core
- `initialize(Application, SmartAdsConfig)`: Setup the SDK (Call once).
- `getInstance()`: Access the singleton instance.
- `isInitialized()`: Static check for initialization status.
- `getVersion()`: Returns "6.0.0".
- `shutdown()`: Fully stop all ad services and clear memory.

### Ad Control & Status
- `setAdsEnabled(boolean)`: Enable/Disable ads dynamically at runtime.
- `areAdsEnabled()`: Check current global ad status.
- `updateConfig(SmartAdsConfig)`: Switch ad unit IDs or logic on the fly.
- `preloadAds(Context)`: Manually trigger pre-fetching for all formats.
- `isAnyAdShowing()`: Returns true if any full-screen ad is active.

### Format Availability
- `isAppOpenAdAvailable()`: Check if App Open is ready.
- `isInterstitialAdAvailable()`: Check if Interstitial is loaded.
- `isRewardedAdAvailable()`: Check if Rewarded is loaded.
- `getAppOpenAdStatus()`: Returns `LOADED`, `LOADING`, `IDLE`, etc.

### UMP Consent
- `isPrivacyOptionsRequired()`: Check if GDPR/CCPA settings are needed.
- `showPrivacyOptionsForm(Activity)`: Display the UMP settings form.

---

## 🧹 8. Best Practices & Cleanup
To avoid memory leaks, always clean up containers in your Activity's `onDestroy()`.

```java
@Override
protected void onDestroy() {
    SmartAds.getInstance().destroyBannerIn(bannerContainer);
    SmartAds.getInstance().clearNativeIn(nativeContainer);
    super.onDestroy();
}
```

---

## 📄 License
Licensed under **MIT License**. Created by [Partha Roy](https://github.com/partharoypc).
