# SmartAds Library public API
-keep public class com.partharoypc.smartads.SmartAds {
    public *;
}
-keep public class com.partharoypc.smartads.SmartAdsConfig {
    public *;
}
-keep public class com.partharoypc.smartads.SmartAdsConfig$Builder {
    public *;
}

# Keep Listeners (Public Interfaces)
-keep public interface com.partharoypc.smartads.listeners.** { *; }

# Keep Enums/Data Classes (Public)
-keep public class com.partharoypc.smartads.AdStatus { *; }
-keep public class com.partharoypc.smartads.NativeAdSize { *; }

# Keep House Ads (Data Models)
-keep public class com.partharoypc.smartads.house.HouseAd { *; }

# Internal Managers / UI (Allowed to be obfuscated internally, but keep class names if necessary for debugging - optional)
# For now, we allow FULL obfuscation of managers/ui as they are not public API.

# --- Dependency Rules (Library Build) ---

# Google Mobile Ads (Required for library compilation)
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Lifecycle (Required for ProcessLifecycleOwner)
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

# User Messaging Platform (UMP)
-keep class com.google.android.ump.** { *; }
-dontwarn com.google.android.ump.**

# --- Mediation Adapters (Compile time safety) ---
-dontwarn com.facebook.ads.**
-dontwarn com.applovin.**
-dontwarn com.unity3d.ads.**
-dontwarn com.unity3d.services.**

# --- Common attributes ---
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod,InnerClasses
-keepattributes SourceFile,LineNumberTable
