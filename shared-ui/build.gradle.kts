plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
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
            baseName = "shared-ui"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":shared-data"))

            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)
            implementation(libs.voyager.tab.navigator)

            // Coil 3
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor)

            // Compottie (Lottie for CMP)
            implementation(libs.compottie)

            // Paging
            implementation(libs.paging.common.kmp)
        }

        androidMain.dependencies {
            // Paging Compose (Android-only)
            implementation(libs.paging.compose.kmp)

            // Koin Android
            implementation(libs.koin.android)

            // Android Compose
            implementation(libs.androidx.activity.compose)

            // Google Maps
            implementation(libs.maps.compose)
            implementation(libs.play.services.location)
            implementation(libs.play.services.maps)

            // Lottie (Android-only, alongside Compottie for common)
            implementation(libs.lottie.compose)

            // Media3 ExoPlayer
            implementation(libs.media3.exoplayer)
            implementation(libs.media3.ui)

            // AppCompat (locale management)
            implementation(libs.androidx.appcompat)

            // EventBus
            implementation(libs.eventbus)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mahalatak.shared"
}

android {
    namespace = "com.mahalatak.shared.ui"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
