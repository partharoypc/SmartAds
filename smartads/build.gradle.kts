plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
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
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    lint {
        abortOnError = false
        warningsAsErrors = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release")
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
    implementation("androidx.annotation:annotation:1.8.2")
}

// ✅ Important: wrap publishing block inside afterEvaluate
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.partharoypc"
                artifactId = "SmartAds"
                version = "1.0.1"

                pom {
                    name.set("SmartAds")
                    description.set("Simple ad mediation library for Android (AdMob + Facebook)")
                    url.set("https://github.com/partharoypc/SmartAds")
                }
            }
        }
    }
}
