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
    implementation 'com.github.partharoypc:SmartAds:5.4.0'
    implementation 'com.google.android.gms:play-services-ads:24.9.0'

    // SmartAds automatically handles other internal dependencies.
    // Note: Mediation adapters are NOT included by default.
}
```

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

## 🤝 6. Mediation Setup (Optional)

> [!IMPORTANT]
> SmartAds library does **NOT** include any mediation adapters by default. Only add the networks you actually use in AdMob mediation to keep your app size minimal.

### When to Add Mediation Adapters

Add mediation dependencies to your **app's** `build.gradle` ONLY if:
- ✅ You have configured that network in your AdMob account mediation settings
- ✅ You want to use that network for ad fill
- ❌ **Don't** add them "just in case" - each adapter adds 3-5 MB to your app size

---

### Available Mediation Networks

Always check the [Google Mediation Page](https://developers.google.com/admob/android/mediation) for the latest adapter versions.

#### 🔵 Meta (Facebook Audience Network)

**Step 1**: Add dependency to your app's `build.gradle`
```gradle
dependencies {
    implementation 'com.google.ads.mediation:facebook:6.21.0.0'
}
```

**Step 2**: Enable in SmartAds config
```java
SmartAdsConfig config = new SmartAdsConfig.Builder()
    .setFacebookMediationEnabled(true)  // Enable verification
    // ... other config
    .build();
```

**Manifest**: No extra meta-data required.

---

#### 🔴 AppLovin MAX

**Step 1**: Add dependency to your app's `build.gradle`
```gradle
dependencies {
    implementation 'com.google.ads.mediation:applovin:13.5.1.0'
}
```

**Step 2**: Add SDK key to `AndroidManifest.xml` (inside `<application>` tag)
```xml
<meta-data 
    android:name="applovin.sdk.key" 
    android:value="YOUR_APPLOVIN_SDK_KEY"/>
```

**Step 3**: Enable in SmartAds config
```java
SmartAdsConfig config = new SmartAdsConfig.Builder()
    .setAppLovinMediationEnabled(true)  // Enable verification
    // ... other config
    .build();
```

---

#### 🟢 Unity Ads

**Step 1**: Add dependency to your app's `build.gradle`
```gradle
dependencies {
    implementation 'com.google.ads.mediation:unity:4.16.5.0'
}
```

**Step 2**: Enable in SmartAds config
```java
SmartAdsConfig config = new SmartAdsConfig.Builder()
    .setUnityMediationEnabled(true)  // Enable verification
    // ... other config
    .build();
```

**Manifest**: No extra meta-data required. Configuration is handled via AdMob UI.

---

### Recommended ProGuard / R8 Rules

If you use `minifyEnabled true`, add these rules to your app's `proguard-rules.pro` **only for the networks you're using**:

```proguard
# Google Mobile Ads (Always required)
-keep public class com.google.android.gms.ads.** { *; }

# Meta (Facebook) - Only if using Facebook mediation
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**

# AppLovin - Only if using AppLovin mediation
-keep class com.applovin.** { *; }
-dontwarn com.applovin.**

# Unity Ads - Only if using Unity mediation
-keep class com.unity3d.ads.** { *; }
-keep class com.unity3d.services.** { *; }
-dontwarn com.unity3d.ads.**
-dontwarn com.unity3d.services.**
```

---

### Mediation Verification & Debugging

SmartAds provides built-in tools to ensure your mediation setup is working correctly:

#### 1. Adapter Detection
Automatically logs which adapters are found during initialization (when logging is enabled):
```java
SmartAdsConfig config = new SmartAdsConfig.Builder()
    .setLoggingEnabled(true)  // Enable to see adapter detection logs
    .setFacebookMediationEnabled(true)
    .setAppLovinMediationEnabled(true)
    .build();

// Logs will show:
// ✅ Facebook / Meta Audience Network Adapter found.
// ✅ AppLovin Adapter found.
// ❌ Unity Ads Adapter NOT found. Add dependency.
```

You can also manually verify at any time:
```java
SmartAds.getInstance().verifyMediation(activity);
```

#### 2. AdMob Ad Inspector
Launch the official AdMob inspector to see real-time ad fill status and mediation waterfall:
```java
SmartAds.getInstance().launchAdInspector(activity);
```

#### 3. Mediation Test Suite (Optional)
For comprehensive mediation testing, add the test suite to your app (debug builds only):
```gradle
dependencies {
    debugImplementation 'com.google.android.ads:mediation-test-suite:3.0.0'
}
```

Then launch it:
```java
SmartAds.getInstance().openMediationTestSuite(activity);
```

---

### Example: Complete Setup with Selective Mediation

**Scenario**: You only want to use Facebook and AppLovin mediation.

**App's `build.gradle`**:
```gradle
dependencies {
    implementation 'com.github.partharoypc:SmartAds:5.4.0'
    
    // Only the networks you use
    implementation 'com.google.ads.mediation:facebook:6.21.0.0'
    implementation 'com.google.ads.mediation:applovin:13.5.1.0'
    
    // Optional: Test suite for debugging
    debugImplementation 'com.google.android.ads:mediation-test-suite:3.0.0'
}
```

**AndroidManifest.xml**:
```xml
<application>
    <!-- AdMob App ID (Required) -->
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
    
    <!-- AppLovin SDK Key (Required for AppLovin) -->
    <meta-data 
        android:name="applovin.sdk.key" 
        android:value="YOUR_APPLOVIN_SDK_KEY"/>
</application>
```

**Application class**:
```java
SmartAdsConfig config = new SmartAdsConfig.Builder()
    .setAdsEnabled(true)
    .setTestModeEnabled(BuildConfig.DEBUG)
    .setLoggingEnabled(BuildConfig.DEBUG)
    
    // Enable only the networks you added
    .setFacebookMediationEnabled(true)
    .setAppLovinMediationEnabled(true)
    .setUnityMediationEnabled(false)  // Not using Unity
    
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
- `getVersion()`: Returns "5.0.0".
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
