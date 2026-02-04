# SmartAds Consumer Rules (Applied to the App using this library)

# Keep the Public API of this library
-keep public class com.partharoypc.smartads.SmartAds { 
    public *; 
}
-keep public class com.partharoypc.smartads.SmartAdsConfig { 
    public *; 
}
-keep public class com.partharoypc.smartads.SmartAdsConfig$Builder {
    public *; 
}
-keep public interface com.partharoypc.smartads.listeners.** { *; }
-keep public class com.partharoypc.smartads.AdStatus { *; }
-keep public class com.partharoypc.smartads.NativeAdSize { *; }
-keep public class com.partharoypc.smartads.house.HouseAd { *; }

# --- Mediation Verification Support ---
# Since SmartAds uses Class.forName("...") to verify mediation adapters, we must ensure
# these adapter classes are NOT renamed if the user includes them. 

# Facebook / Meta
-keep class com.google.ads.mediation.facebook.FacebookAdapter { *; }
-keep class com.facebook.ads.** { *; }

# AppLovin
-keep class com.google.ads.mediation.applovin.ApplovinAdapter { *; }
-keep class com.applovin.** { *; }

# Unity Ads
-keep class com.google.ads.mediation.unity.UnityAdapter { *; }
-keep class com.unity3d.ads.** { *; }

# Mediation Test Suite (If user includes it)
-keep class com.google.android.ads.mediationtestsuite.** { *; }

# Note: We do NOT force keep com.google.android.gms.ads.** here. 
# The 'play-services-ads' library includes its own consumer proguard rules 
# which are sufficient and more correct.
