# SmartAds Library public API
-keep public class com.partharoy.smartads.* {
    public protected *;
}
-keep public class com.partharoy.smartads.listeners.* {
    public protected *;
}
-keep public class com.partharoy.smartads.managers.* {
    public protected *;
}
-keep public class com.partharoy.smartads.ui.* {
    public protected *;
}
-keep public class com.partharoy.smartads.AdStatus { *; }
-keep public class com.partharoy.smartads.NativeAdSize { *; }

# AdMob & Lifecycle
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

# Common attributes for library stability
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod,InnerClasses
