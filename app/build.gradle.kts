plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-kapt") // Required for Hilt
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.learningapp"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.learningapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- Firebase ---
    // Import the Firebase BoM to manage library versions automatically.
    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))
    implementation("com.google.firebase:firebase-auth")

    // --- Hilt ---
    // Hilt Android: Core library for Dependency Injection in Android
    implementation("com.google.dagger:hilt-android:2.55")
    // Hilt Compiler: Annotation processor for generating DI code
    kapt("com.google.dagger:hilt-android-compiler:2.55")
    // Hilt Navigation Compose: Integration between Hilt and Compose Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // Lifecycle ViewModel Compose: Integration of ViewModel with Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    // Navigation Compose: Essential for building navigation graphs in Compose
    implementation("androidx.navigation:navigation-compose:2.9.7")

    // --- Networking & External Services ---
    // Azure Cognitive Services Speech: SDK for speech-to-text and text-to-speech
    implementation("com.microsoft.cognitiveservices.speech:client-sdk:1.47.0")
    // OkHttp & Logging (Crucial for debugging network calls)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0") // To convert JSON to Kotlin Data Classes


    // Material Icons (The extended set)
    implementation("androidx.compose.material:material-icons-extended")

    // Preferences DataStore (SharedPreferences like APIs)
    implementation("androidx.datastore:datastore-preferences:1.2.0")

    // For the Credential Manager libraries
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.2.0")
}