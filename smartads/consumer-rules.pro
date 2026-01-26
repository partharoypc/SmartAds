# SmartAds Library public API
-keep public class com.partharoypc.smartads.* {
    public protected *;
}
-keep public class com.partharoypc.smartads.listeners.* {
    public protected *;
}
-keep public class com.partharoypc.smartads.managers.* {
    public protected *;
}
-keep public class com.partharoypc.smartads.ui.* {
    public protected *;
}
-keep public class com.partharoypc.smartads.AdStatus { *; }
-keep public class com.partharoypc.smartads.NativeAdSize { *; }

# AdMob & Lifecycle
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

# User Messaging Platform (UMP)
-keep class com.google.android.ump.** { *; }
-dontwarn com.google.android.ump.**

# Common attributes for library stability
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod,InnerClasses
