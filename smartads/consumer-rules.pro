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



# Note: We do NOT force keep com.google.android.gms.ads.** here. 
# The 'play-services-ads' library includes its own consumer proguard rules 
# which are sufficient and more correct.
