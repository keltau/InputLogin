plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.inputstick.apps.inputlogin"
    compileSdk {
        version = release(32)
    }

    defaultConfig {
        applicationId = "com.inputstick.apps.inputlogin"
        minSdk = 32
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    lint {
        disable += "ExpiredTargetSdkVersion"
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.core)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
