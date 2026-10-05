plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.masarifi.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.masarifi.app"
        minSdk = 23
        targetSdk = 35
        versionCode = 351
        versionName = "3.5.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

kotlin { jvmToolchain(17) }
