# 📱 Smart Ads Library for Android

A powerful, developer-friendly Android library designed to **simplify ad integration**, **maximize revenue**, and **improve user experience**.

SmartAds provides a **robust primary/backup ad system** using Google AdMob and Meta Audience Network, with extensive customization options and a centralized configuration approach.

[![](https://jitpack.io/v/partharoypc/SmartAds.svg)](https://jitpack.io/#partharoypc/SmartAds)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## ✨ Features

- ✅ **Primary/Backup System**: Uses AdMob by default and fails over to Meta if needed.
- 📱 **All Major Ad Formats**: App Open, Interstitial, Rewarded, Banner, and Native.
- 🌐 **Global Ad Control**: A single master switch to enable/disable all ads.
- ⏱ **Frequency Capping**: Set minimum interval between interstitial ads.
- 📅 **Install-Day Gating**: Prevent ads for a number of days after install.
- 🧪 **Automatic Test Mode**: Easily toggle test mode with a flag.
- 🔄 **Loading Animation**: Optional clean dialog while ads are prepared.
- 🛠 **Centralized Configuration**: Builder pattern setup.

---

## 🛠️ Setup & Integration

### 1. Include the Library

In your `settings.gradle`:

```groovy
include ':app', ':smartads'
```

### 2. Add Dependencies

In your **`app/build.gradle`**:

```groovy
dependencies {
    implementation project(':smartads')
    implementation("com.google.android.gms:play-services-ads:24.5.0")
    implementation("com.facebook.android:audience-network-sdk:6.+")
    implementation("androidx.lifecycle:lifecycle-process:2.9.2")
}
```

### 3. Update `AndroidManifest.xml`

Inside the `<application>` tag:

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="YOUR_ADMOB_APP_ID_HERE"/>
```

---

## 🚀 Initialization

In your custom `Application` class:

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SmartAds.initialize(this, new SmartAdsConfig.Builder()
            .setAdMobAppId("ca-app-pub-3940256099942544~3347511713")
            .setAdMobAppOpenId("ca-app-pub-3940256099942544/3419835294")
            .enableTestMode(true)
            .setAdsEnabled(true)
            .setShowAdsAfterDays(0)
            .setAdShowIntervalMillis(30000)
            .build());
    }
}
```

And in `AndroidManifest.xml`:

```xml
<application
    android:name=".MyApplication"
    ... >
</application>
```

---

## 💡 Usage Examples

### ✅ Banner Ad

```java
SmartAds.getInstance().showBannerAd(this, bannerContainer, new BannerAdListener() {
    @Override public void onAdLoaded(View adView) {}
    @Override public void onAdFailed(String errorMessage) {}
});
```

### ✅ Interstitial Ad

```java
SmartAds.getInstance().loadInterstitialAd(this);

SmartAds.getInstance().showInterstitialAd(this, new InterstitialAdListener() {
    @Override public void onAdDismissed() {
        SmartAds.getInstance().loadInterstitialAd(MainActivity.this);
    }
    @Override public void onAdFailedToShow(String errorMessage) {}
});
```

### ✅ Rewarded Ad

```java
SmartAds.getInstance().loadRewardedAd(this);

SmartAds.getInstance().showRewardedAd(this, new RewardedAdListener() {
    @Override public void onUserEarnedReward() {}
    @Override public void onAdDismissed() {
        SmartAds.getInstance().loadRewardedAd(MainActivity.this);
    }
    @Override public void onAdFailedToShow(String errorMessage) {}
});
```

### ✅ Native Ad

```java
SmartAds.getInstance().showNativeAd(this, nativeContainer, R.layout.native_ad_layout, new NativeAdListener() {
    @Override public void onAdLoaded(View nativeAdView) {}
    @Override public void onAdFailed(String errorMessage) {}
});
```

---

## ⚙️ Configuration Options

| Method | Description | Default |
|--------|-------------|---------|
| `setAdsEnabled(boolean)` | Master switch to enable/disable ads | `true` |
| `enableTestMode(boolean)` | Use test ad unit IDs | `false` |
| `setShowAdsAfterDays(int)` | Delay ad display after install | `0` |
| `setAdShowIntervalMillis(long)` | Interval between interstitials (ms) | `60000` |
| `setShowLoadingDialog(boolean)` | Show loading dialog | `true` |
| `setUseMetaBackup(boolean)` | Use Meta as backup ad source | `true` |
| `setAdMob...Id(String)` | Set AdMob ad unit ID | `""` |
| `setMeta...Id(String)` | Set Meta placement ID | `""` |

---

## 📄 License

This library is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.
