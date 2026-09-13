import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// Read Gemini API key from local.properties.
// If it is not available, use the environment variable for CI.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")

    if (file.exists()) {
        file.inputStream().use {
            load(it)
        }
    }
}

val geminiApiKey: String =
    localProperties.getProperty("GEMINI_API_KEY")
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?: System.getenv("GEMINI_API_KEY")
            ?.trim()
            .orEmpty()

android {

    namespace =
        "com.fahim.geminiApiComposeStarter"

    compileSdk {
        version = release(36)
    }

    defaultConfig {

        applicationId =
            "com.fahim.geminiApiComposeStarter"

        minSdk = 26
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"" +
                    geminiApiKey
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"") +
                    "\""
        )
    }

    buildTypes {

        release {

            // R8 code shrinking / obfuscation
            isMinifyEnabled = true

            // Remove unused resources
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }

    buildFeatures {

        compose = true

        // Required because we expose
        // GEMINI_API_KEY through BuildConfig
        buildConfig = true
    }
}

dependencies {

    implementation(
        libs.androidx.core.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    implementation(
        libs.androidx.activity.compose
    )

    implementation(
        libs.androidx.lifecycle.viewmodel.compose
    )

    /*
     * DataStore
     * Used for secure encrypted API-key storage.
     */
    implementation(
        "androidx.datastore:datastore-preferences:1.2.1"
    )

    /*
     * Room
     * Used to persist chat history.
     */
    implementation(
        libs.androidx.room.runtime
    )

    implementation(
        libs.androidx.room.ktx
    )

    ksp(
        libs.androidx.room.compiler
    )

    /*
     * Compose
     */
    implementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    implementation(
        libs.androidx.compose.ui
    )

    implementation(
        libs.androidx.compose.ui.graphics
    )

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )

    implementation(
        libs.androidx.compose.material3
    )

    /*
     * Gemini
     */
    implementation(
        libs.google.generativeai
    )

    /*
     * Unit testing
     */
    testImplementation(
        libs.junit
    )

    testImplementation(
        libs.kotlinx.coroutines.test
    )

    /*
     * Android / Compose UI tests
     */
    androidTestImplementation(
        libs.androidx.junit
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    /*
     * Debug-only Compose tools
     */
    debugImplementation(
        libs.androidx.compose.ui.tooling
    )

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )
}