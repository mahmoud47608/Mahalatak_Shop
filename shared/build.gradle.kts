plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))

            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.websockets)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Coil 3
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor)

            // Compottie (Lottie for CMP)
            implementation(libs.compottie)

            // Multiplatform Settings
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.no.arg)

            // Paging
            implementation(libs.paging.common.kmp)
        }

        androidMain.dependencies {
            // Data module
            implementation(project(":data"))

            // Paging Compose (Android-only)
            implementation(libs.paging.compose.kmp)

            // Ktor engine
            implementation(libs.ktor.client.okhttp)

            // Koin Android
            implementation(libs.koin.android)

            // Android Compose
            implementation(libs.androidx.activity.compose)

            // Android Security (EncryptedSharedPreferences)
            implementation(libs.androidx.security.crypto)

            // Socket.IO (Android-specific)
            implementation(libs.socket)

            // Navigation 3
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.androidx.material3.adaptive.navigation3)

            // Maps
            implementation(libs.maps.compose)
            implementation(libs.play.services.location)
            implementation(libs.play.services.maps)

            // Media3 (ExoPlayer)
            implementation(libs.media3.exoplayer)
            implementation(libs.media3.ui)

            // AppCompat (for locale)
            implementation(libs.androidx.appcompat)

            // EventBus
            implementation(libs.eventbus)

            // Firebase
            implementation(libs.firebase.messaging.ktx)
        }

        iosMain.dependencies {
            // Ktor engine
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.mahalatak.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
