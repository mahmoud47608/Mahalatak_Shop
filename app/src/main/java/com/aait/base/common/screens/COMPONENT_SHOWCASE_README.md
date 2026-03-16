# Component Showcase Screen

## Overview

The `ComponentShowcaseScreen` is a comprehensive testing and demonstration screen that showcases all refactored components in the base component library.

## Purpose

1. **Visual Testing** - See all components rendered with various states and configurations
2. **Documentation** - Live examples of how to use each component
3. **QA Testing** - Quick way to verify all components work correctly after changes
4. **Developer Reference** - See component API usage examples

## Components Demonstrated

### 1. Button Components
- `DefaultButton` with all styles (PRIMARY, SUCCESS, ERROR, SECONDARY)
- Enabled and disabled states
- Interactive state changes

### 2. Text Components
- Various typography styles (headline, title, body)
- Text alignment options
- Max lines and overflow handling

### 3. Input Fields
- `DefaultTextField` - Basic text input
- `DefaultTextFieldPhoneWithCode` - Phone number with country code
- `SearchTextField` - Search input
- Error states and disabled states
- Required field indicators

### 4. Dropdown Components
- `DefaultDropDown` - Generic dropdown
- `GenderDropDown` - Gender selection
- `MaritalStatusDropDown` - Marital status with gender awareness
- Disabled state

### 5. Radio Buttons
- `RadioOption` - Single selection radio buttons
- Interactive selection

## How to Use

### Option 1: Direct Preview (Recommended for Development)

The easiest way to view the showcase is through Android Studio's preview:

1. Open `ComponentShowcaseScreen.kt` in Android Studio
2. Enable "Split" or "Design" view
3. The `@Preview` annotation will render the entire showcase

### Option 2: Add to Navigation

To add as a navigable screen in your app:

```kotlin
// In your navigation graph or MainActivity

// Create a navigation destination
@Serializable
object ComponentShowcaseRoute

// Add to your NavHost
composable<ComponentShowcaseRoute> {
    ComponentShowcaseScreen()
}

// Navigate to it
navController.navigate(ComponentShowcaseRoute)
```

### Option 3: Temporary Launch Screen

For quick testing, temporarily set as your app's entry point:

```kotlin
// In MainActivity.kt
setContent {
    BaseTheme {
        ComponentShowcaseScreen()
    }
}
```

## Usage Examples

### Example 1: Testing Button Styles

The showcase demonstrates all button styles:

```kotlin
DefaultButton(
    text = "Primary Button",
    onClick = { /* action */ },
    style = ButtonStyle.PRIMARY
)

DefaultButton(
    text = "Success Button",
    onClick = { /* action */ },
    style = ButtonStyle.SUCCESS
)
```

### Example 2: Phone Input with Country Code

```kotlin
DefaultTextFieldPhoneWithCode(
    phoneValue = phoneValue,
    onPhoneChange = { phoneValue = it },
    title = "Phone Number",
    countryCode = "+966",  // Saudi Arabia
    isRequired = true
)

// Or without flag icon
DefaultTextFieldPhoneWithCode(
    phoneValue = phoneValue,
    onPhoneChange = { phoneValue = it },
    countryCode = "+1",  // US
    countryFlagIcon = null  // No flag shown
)
```

### Example 3: Gender-Aware Marital Status

```kotlin
// First select gender
GenderDropDown(
    title = "Gender",
    selectedItem = selectedGender,
    onItemSelected = { selectedGender = it }
)

// Then marital status adjusts based on gender
MaritalStatusDropDown(
    title = "Marital Status",
    selectedItem = selectedMaritalStatus,
    gender = selectedGender,  // Pass selected gender
    onItemSelected = { selectedMaritalStatus = it }
)
```

## Customization

You can extend the showcase by adding more sections:

```kotlin
ComponentSection(title = "Your New Section") {
    // Add your component demonstrations here
}
```

## Testing Checklist

Use this screen to verify:

- ✅ All button styles render correctly
- ✅ Button states (enabled/disabled) work
- ✅ Text components use proper typography
- ✅ Input fields accept text input
- ✅ Error states display correctly
- ✅ Dropdowns open and close properly
- ✅ Radio buttons are selectable
- ✅ Phone field accepts country codes
- ✅ Gender affects marital status options
- ✅ Disabled states are non-interactive
- ✅ Required field indicators show

## Notes

- The showcase uses `BaseTheme` to ensure proper theming
- All component states are managed with `remember` and `mutableStateOf`
- The screen is scrollable to accommodate all components
- Components are organized in cards for better visual separation

## Future Enhancements

Consider adding:
- Image components showcase
- Dialog components
- Bottom sheet components
- Date picker components
- File picker components
- Loading states and progress indicators
- Lottie animations (when raw resources are available)
