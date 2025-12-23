# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Google Mobile Ads
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Lifecycle
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

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

# Common attributes
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod,InnerClasses
