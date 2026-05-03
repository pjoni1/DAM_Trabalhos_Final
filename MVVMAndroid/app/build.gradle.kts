plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") // Requer Kotlin 2.0.0+
    // Se usares serialização no Ktor, precisas deste plugin:
    kotlin("plugin.serialization") version "2.0.0"
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "dam.a51421.mvvmweatherapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "dam.a51421.mvvmweatherapp"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }

    // Com o plugin "kotlin.plugin.compose", NÃO precisas do bloco composeOptions { kotlinCompilerExtensionVersion }
}

dependencies {
    // Bibliotecas base (via Version Catalog)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.crashlytics.buildtools)

    // Ktor e Serialização
    implementation("io.ktor:ktor-client-android:2.3.10")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.10")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Jetpack Compose - Usando o BOM mais recente para compatibilidade com SDK 35/36
    val composeBom = platform("androidx.compose:compose-bom:2024.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Lifecycle & Activity para Compose (Versões alinhadas)
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    implementation("androidx.activity:activity-ktx:1.9.3")
    // Ferramentas de Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}