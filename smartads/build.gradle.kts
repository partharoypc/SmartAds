plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "com.partharoypc.smartads"
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
        buildConfig = false
    }

    lint {
        abortOnError = false
        warningsAsErrors = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        singleVariant("release")
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-ads:24.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime:2.10.0")
    implementation("androidx.lifecycle:lifecycle-process:2.10.0")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("com.google.android.material:material:1.13.0")
    // Optional: Mediation Test Suite
    compileOnly("com.google.android.ads:mediation-test-suite:3.0.0")
}

// ✅ Important: wrap publishing block inside afterEvaluate
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.partharoypc"
                artifactId = "SmartAds"
                version = "5.3.0"

                pom {
                    name.set("SmartAds")
                    description.set("Simple ad library for Android")
                    url.set("https://github.com/partharoypc/SmartAds")
                }
            }
        }
    }
}
