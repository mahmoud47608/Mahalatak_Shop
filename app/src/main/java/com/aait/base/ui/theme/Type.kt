package com.aait.base.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mahalatak.R

// Define the custom FontFamily
val expoArabic = FontFamily(
    Font(R.font.expo_arabic_light, FontWeight.Light),
    Font(R.font.expo_arabic_book, FontWeight.Normal),
    Font(R.font.expo_arabic_medium, FontWeight.Medium),
    Font(R.font.expo_arabic_semibold, FontWeight.SemiBold),
    Font(R.font.expo_arabic_bold, FontWeight.Bold)
)

// Typography styles based on Figma VIVO design system
// Text sizes: 20sp (xl), 16sp (md), 14sp (sm), 12sp (xs)
// Line height: 20sp (standard from Figma)
// Weights: Regular (400), Medium (500), SemiBold (600)
val Typography = Typography(
    // Display
    displayLarge = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    // Headline
    headlineLarge = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    // Title - App text/Semi Bold/20 from Figma
    titleLarge = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    // Title - App text/Semi Bold/16 from Figma
    titleMedium = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    // Title - App text/Medium/14 from Figma
    titleSmall = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    // Body - App text/Semi Bold/16 from Figma
    bodyLarge = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    // Body - App text/Regular/14 from Figma
    bodyMedium = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    // Body - App text/Medium/12 from Figma
    bodySmall = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    // Label - App text/Medium/14 from Figma
    labelLarge = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    // Label - App text/Medium/12 from Figma
    labelMedium = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = expoArabic,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )
)