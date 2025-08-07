plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.partharoy.smartads"
    compileSdk = 36

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation("com.google.android.gms:play-services-ads:24.5.0")
    implementation("com.facebook.android:audience-network-sdk:6.+")
    implementation("androidx.lifecycle:lifecycle-runtime:2.9.2")
    implementation("androidx.lifecycle:lifecycle-process:2.9.2")
}