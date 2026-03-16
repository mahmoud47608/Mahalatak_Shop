# Component Showcase - Quick Start Guide

## 🎯 What is it?

A comprehensive testing screen that demonstrates all refactored components in one place.

**Location**: `app/src/main/java/com/aait/base/common/screens/ComponentShowcaseScreen.kt`

## 🚀 Quick Start (3 Ways)

### Method 1: Android Studio Preview (Easiest)

1. Open `ComponentShowcaseScreen.kt` in Android Studio
2. Click "Split" view (or press `Ctrl+Shift+F12` on Windows, `Cmd+Shift+F12` on Mac)
3. See the live preview on the right side

### Method 2: Temporary Launch Screen

In your `MainActivity.kt`:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
        BaseTheme {
            ComponentShowcaseScreen()  // <-- Add this
        }
    }
}
```

Run the app to see the showcase screen.

### Method 3: Add to Navigation

If using Navigation Compose:

```kotlin
// Define route
@Serializable
object ShowcaseRoute

// In your NavHost
composable<ShowcaseRoute> {
    ComponentShowcaseScreen()
}

// Navigate to it
navController.navigate(ShowcaseRoute)
```

## 📋 What's Included

### ✅ Buttons
- PRIMARY style (default blue)
- SUCCESS style (green)
- ERROR style (red)
- SECONDARY style (black)
- Disabled state

### ✅ Text Components
- All typography variants (headline, title, body)
- Text alignment
- Max lines with ellipsis

### ✅ Input Fields
- Basic text input
- Email input
- Error states
- Disabled states
- Phone number with country code
  - Saudi Arabia (+966) with flag
  - US (+1) without flag

### ✅ Dropdowns
- Generic dropdown with cities
- Gender dropdown
- Marital status (gender-aware)
- Disabled dropdown

### ✅ Radio Buttons
- Single selection
- Interactive state

## 🎨 Features

- ✅ Fully scrollable
- ✅ Organized in sections with cards
- ✅ Interactive components
- ✅ Shows both enabled and disabled states
- ✅ Demonstrates error handling
- ✅ Uses proper theming (BaseTheme)
- ✅ Follows Material Design 3

## 📸 Screenshot Components

The screen is divided into visual sections:

```
┌─────────────────────────────┐
│  Component Library Showcase │
├─────────────────────────────┤
│  🔘 Buttons                 │
│   - Primary Button          │
│   - Success Button          │
│   - Error Button            │
│   - Secondary Button        │
│   - Disabled Button         │
├─────────────────────────────┤
│  📝 Text Components         │
│   - Various typography      │
│   - Alignment examples      │
├─────────────────────────────┤
│  ✏️ Input Fields            │
│   - Text inputs             │
│   - Phone inputs            │
│   - Error states            │
├─────────────────────────────┤
│  📋 Dropdown Components     │
│   - City selector           │
│   - Gender selector         │
│   - Marital status          │
├─────────────────────────────┤
│  ⭕ Radio Buttons           │
│   - Option selection        │
└─────────────────────────────┘
```

## 🔧 Customization

Add more sections by copying this pattern:

```kotlin
ComponentSection(title = "Your Section Name") {
    // Your component demonstrations here
    YourComponent()
}
```

## 🐛 Testing Checklist

Use this screen to verify:

- [ ] All buttons render and respond to clicks
- [ ] Text displays with proper styling
- [ ] Input fields accept text
- [ ] Dropdowns open and close
- [ ] Phone field accepts numbers
- [ ] Error states show in red
- [ ] Disabled components are grayed out
- [ ] Gender selection affects marital status options
- [ ] Radio buttons select correctly

## 📝 Notes

- All state is managed locally with `remember` and `mutableStateOf`
- Components use the refactored base library
- No hardcoded values - all use theme and resources
- Backward compatible with deprecated button variants

## 🎓 Learning Resource

This screen serves as:
1. **Live documentation** - See how to use each component
2. **API reference** - Copy-paste working examples
3. **Visual regression testing** - Verify components look correct
4. **QA tool** - Test all components quickly

---

**Pro Tip**: Keep this screen in your project during development for quick visual testing after making changes!
