import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.mohsin.auth.android"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.mohsin.auth.android"
        minSdk = libs.versions.minSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        targetSdk = 35

        val date = Date()
        val formattedDate = SimpleDateFormat("dd-MMM-yyyy").format(date)
        setProperty("archivesBaseName", "AmexAuth-v-${versionName}-"+formattedDate)
        buildConfigField("String","APP_VERSION_NAME","\"${versionName}\"")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }

        getByName("debug") {
            isMinifyEnabled = true   // ✅ Enable R8/ProGuard
            isShrinkResources = true // ✅ Shrink unused resources
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.bundles.material3)
    implementation(libs.compose.activity)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui)
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0")
    implementation(libs.koin.android)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.lottie.compose)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.3")
    implementation("io.github.g00fy2.quickie:quickie-bundled:1.11.0")

}