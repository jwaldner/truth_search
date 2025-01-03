plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt") // Required for Room's annotation processor
}

android {
    namespace = "com.wfs.truthsearch"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wfs.truthsearch"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"


        android.buildFeatures.buildConfig = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val keystorePassword = project.findProperty("keystore.password") as String?
        buildConfigField("String", "KEYSTORE_PASSWORD", "\"$keystorePassword\"")
        // println("Assigned password: $keystorePassword")
        // println("Keystore Password: ${BuildConfig.KEYSTORE_PASSWORD}")


        buildConfigField("String", "KEYSTORE_PASSWORD", "\"${project.findProperty("keystore.password")}\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    // Jetpack Compose Material library
    implementation(libs.androidx.material)
    // Core Compose UI (optional but useful for layouts like Box, Column, Row)
    implementation(libs.ui)

    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Core Compose dependencies (using Compose BOM for version alignment)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
   //implementation(libs.androidx.material3)

    // Compose-specific dependencies
    implementation(libs.androidx.foundation) // For LazyColumn and other foundational components
    implementation(libs.androidx.runtime) // State management
    implementation(libs.androidx.lifecycle.runtime.compose) // Compose + ViewModel integration


            // Annotation processor for Room
    implementation(libs.androidx.room.runtime) // Room runtime
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.material3.android)
    kapt("androidx.room:room-compiler:2.6.1")

    // JSON Parsing
    implementation(libs.gson)

    // NanoHTTPD for server
    implementation(libs.nanohttpd)
    implementation(libs.bouncycastle)
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
