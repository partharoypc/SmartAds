<p align="center">
  <img src="logo/logo.png" alt="SmartAds Logo" width="200"/>
</p>

# SmartAds Android Library 🚀

**SmartAds** is a professional-grade, lightweight, and developer-friendly Android wrapper for the Google Mobile Ads SDK (AdMob). It streamlines complex ad integrations into simple, lifecycle-aware components, allowing you to focus on building features while we handle the monetization.

[![](https://jitpack.io/v/partharoypc/SmartAds.svg)](https://jitpack.io/#partharoypc/SmartAds)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
[![Google Mobile Ads](https://img.shields.io/badge/SDK-AdMob_24.0.0-blue.svg)]()

---

## 🤔 Why SmartAds?

Integrating ads can be messy—handling context, memory leaks, pre-fetching, and mediation verification often leads to boilerplate-heavy code. **SmartAds** solves this by:
- **Reducing Boilerplate**: From 150+ lines of AdMob code to just 1-2 lines.
- **Lifecycle Safety**: Built-in protection against Activity-based memory leaks.
- **Smart Pre-fetching**: Automatically caches ads so they're ready the moment you need them.
- **House Ads Fallback**: Never miss an impression—show your own promotions if the network fails.

---

## 🌟 Key Features

- ✅ **Unified Initialization**: One-time setup in your `Application` class.
- 📱 **Complete Format Support**: App Open, Banner (Adaptive/Collapsible), Interstitial, Rewarded, and Native Ads.
- 🛠️ **Seamless Mediation**: Dedicated support for Meta, AppLovin, and Unity Ads.
- 🏠 **House Ads System**: Native fallbacks for internal cross-promotion.
- 📜 **Privacy First**: Built-in Google UMP (GDPR/CCPA) consent management.
- 🧪 **Test Mode**: Automatically handles AdMob Test IDs during development.
- 🔍 **Debug Suite**: Integrated Ad Inspector, Mediation Test Suite, and detailed logging.
- 💰 **Paid Event Tracking**: Simple hook for revenue analytics (Firebase, AppsFlyer, etc.).

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
    implementation 'com.github.partharoypc:SmartAds:5.0.0'
    implementation 'com.google.android.gms:play-services-ads:24.0.0'
}
```

### Manifest (`AndroidManifest.xml`)
```xml
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
        .setAdsEnabled(true)        // Global switch to enable/disable ads
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
| `setFacebookMediationEnabled(boolean)` | Verification for Meta Audience Network. |
| `setAppLovinMediationEnabled(boolean)` | Verification for AppLovin. |
| `setUnityMediationEnabled(boolean)` | Verification for Unity Ads. |
| `setLoadingDialogText(String)` | Custom text for the full-screen ad loading dialog. |
| `setLoadingDialogColor(bg, text)` | Custom colors for the loading dialog. |
| `setMaxAdContentRating(String)` | Set content rating (G, PG, T, MA). |
| `setTagForChildDirectedTreatment(int)` | COPPA compliance. |

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

## 🤝 6. AdMob Mediation Setup Guide
Improve your fill rates and revenue by integrating 3rd-party ad networks.

### **Step 1: Add Mediation Adapters**
Add the following dependencies to your `app/build.gradle`.

```gradle
dependencies {
    // 🔵 Meta (Facebook) Audience Network
    implementation 'com.google.ads.mediation:facebook:6.17.0.0'

    // 🔴 AppLovin MAX
    implementation 'com.google.ads.mediation:applovin:12.4.3.0'

    // 🟢 Unity Ads
    implementation 'com.google.ads.mediation:unity:4.10.0.0'
}
```

### **Step 2: Network-Specific Setup**
- **AppLovin**: Add your **AppLovin SDK Key** to `AndroidManifest.xml`:
  ```xml
  <meta-data android:name="applovin.sdk.key" android:value="YOUR_SDK_KEY_HERE"/>
  ```
- **Verification**: Call `SmartAds.getInstance().verifyMediation(activity);` to check if adapters are detected.

---

## 🛡️ 7. API Reference (Public Methods)

### Core
- `initialize(Application, SmartAdsConfig)`: Setup the SDK.
- `getInstance()`: Access the singleton instance.
- `getVersion()`: Get current version ("5.0.0").
- `shutdown()`: Cleanly shut down all ad services.

### Ad Control
- `setAdsEnabled(boolean)`: Enable/Disable ads dynamically.
- `preloadAds(Context)`: Pre-fetch all configured formats.
- `isAnyAdShowing()`: Returns true if a full-screen ad is active.

### UMP Consent
- `isPrivacyOptionsRequired()`: Check if the privacy form is needed.
- `showPrivacyOptionsForm(Activity)`: Display the UMP form.

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
