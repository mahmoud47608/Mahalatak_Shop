# ── Kotlinx Serialization ──────────────────────────
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.mahalatk.**$$serializer { *; }
-keepclassmembers class com.mahalatk.** {
    *** Companion;
}
-keepclasseswithmembers class com.mahalatk.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ── Ktor ───────────────────────────────────────────
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# ── OkHttp ─────────────────────────────────────────
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

# ── Compose ────────────────────────────────────────
-dontwarn androidx.compose.**

# ── Koin ───────────────────────────────────────────
-keep class org.koin.** { *; }

# ── Compottie (Lottie) ─────────────────────────────
-keep class io.github.alexzhirkevich.compottie.** { *; }

# ── Firebase ───────────────────────────────────────
-keep class com.google.firebase.** { *; }

# ── Stack traces ───────────────────────────────────
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
