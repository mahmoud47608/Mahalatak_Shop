# Component Development Guidelines

This document provides guidelines for developing components that follow the Al-Kharashi design system.

## Design Token Usage

### Always Use These Constants

1. **Colors**: Import from `com.aait.alkarashi.ui.theme.ColorLightTokens`
   ```kotlin
   import com.aait.alkarashi.ui.theme.ColorLightTokens

   // Good
   color = ColorLightTokens.Primary  // #D9831A
   color = ColorLightTokens.OnBackground  // #111111
   color = ColorLightTokens.Outline  // #EBEBEB

   // Or use MaterialTheme
   color = MaterialTheme.colorScheme.primary
   color = MaterialTheme.colorScheme.error
   ```

2. **Spacing**: Import from `com.aait.alkarashi.ui.theme.PaddingDimensions`
   ```kotlin
   import com.aait.alkarashi.ui.theme.PaddingDimensions

   // Good - use design tokens
   .padding(PaddingDimensions.high)      // 16.sdp
   .padding(horizontal = PaddingDimensions.medium)  // 8.sdp
   .padding(PaddingDimensions.low)       // 4.sdp

   // Bad - never hardcode dp
   .padding(20.dp)    // WRONG!
   .padding(8.dp)     // WRONG!
   ```

3. **Corner Radius**: Import from `com.aait.alkarashi.ui.theme.CornerDimensions`
   ```kotlin
   import com.aait.alkarashi.ui.theme.CornerDimensions

   // Good
   shape = RoundedCornerShape(CornerDimensions.high)      // 20.sdp
   shape = RoundedCornerShape(CornerDimensions.medium)    // 8.sdp
   shape = RoundedCornerShape(CornerDimensions.low)       // 4.sdp

   // Bad
   shape = RoundedCornerShape(16.dp)  // WRONG!
   ```

4. **Typography**: Use `MaterialTheme.typography.*`
   ```kotlin
   import androidx.compose.material3.MaterialTheme

   // Good - use typography tokens
   text = text,
   style = MaterialTheme.typography.titleLarge,    // 18sp, Bold
   style = MaterialTheme.typography.labelMedium,   // 16sp, Medium
   style = MaterialTheme.typography.bodyMedium,    // 14sp, Normal
   style = MaterialTheme.typography.bodySmall,     // 12sp, Normal

   // Bad - don't hardcode sizes
   style = TextStyle(fontSize = 16.sp)  // WRONG!
   ```

5. **Dimensions**: Use `.sdp` extension for all dimensions
   ```kotlin
   import ir.kaaveh.sdpcompose.sdp

   // Good - scales on all devices
   .width(300.sdp)
   .height(48.sdp)
   .size(24.sdp)

   // Bad - fixed dp sizes
   .width(300.dp)  // WRONG!
   .height(48.dp)  // WRONG!
   ```

## Component Structure

### Naming Convention
- `Default[ComponentName]` for simple reusable components
- Example: `DefaultButton`, `DefaultTextField`, `DefaultText`

### File Organization
```
common/componant/
├── button/
│   └── DefaultButton.kt       // Primary, Error, Secondary buttons
├── inputs/
│   ├── DefaultTextField.kt
│   └── DefaultTextFieldWithTitle.kt
├── text/
│   └── DefaultText.kt
├── card/
│   └── InfoCard.kt
├── toolbar/
│   └── DefaultToolBar.kt
├── bottomNavigation/
│   ├── BottomNav.kt
│   └── BottomNavItem.kt
├── image/
│   ├── DefaultImage.kt
│   └── DefaultLottie.kt
├── carousel/
│   └── CarouselBanner.kt
└── RatingBar.kt
```

## Best Practices

### 1. Use Composable Functions

```kotlin
@Composable
fun MyCustomComponent(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    // Implementation
}
```

### 2. Accept Modifier Parameter

Always accept a `modifier` parameter with default value:
```kotlin
@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    text: String
) {
    Button(modifier = modifier) {
        Text(text)
    }
}
```

### 3. Reuse Existing Components

```kotlin
// Instead of recreating Button, use DefaultButton
DefaultButton(
    text = "Click me",
    onClick = { /* action */ }
)

// Instead of creating Text, use DefaultText
DefaultText(
    text = "Hello",
    textStyle = MaterialTheme.typography.bodyMedium,
    textColor = ColorLightTokens.OnBackground
)
```

### 4. Use Preview Annotations

```kotlin
@Preview(showBackground = true)
@Composable
fun MyComponentPreview() {
    BaseTheme {
        MyCustomComponent(text = "Preview")
    }
}
```

### 5. Add Documentation

```kotlin
/**
 * A custom button component following Material Design 3.
 *
 * @param modifier Optional modifier for this component
 * @param text Display text on the button
 * @param enabled Whether the button is enabled
 * @param onClick Callback when button is clicked
 */
@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
)
```

## Color Usage Patterns

### Primary Color (Orange)
```kotlin
// Use for:
// - Primary buttons
// - Active states
// - Links and highlights
color = MaterialTheme.colorScheme.primary  // #D9831A
```

### Secondary Color (Brown)
```kotlin
// Use for:
// - Secondary buttons
// - Accent elements
color = MaterialTheme.colorScheme.secondary  // #7C6855
```

### Text Colors
```kotlin
// Main text
color = ColorLightTokens.OnBackground  // #111111

// Secondary/hint text
color = ColorLightTokens.Tertiary  // #6D6D6D

// On colored backgrounds
color = MaterialTheme.colorScheme.onPrimary  // #FFFFFF
```

### Error Color
```kotlin
// Use for:
// - Error states
// - Validation messages
// - Delete/destructive actions
color = MaterialTheme.colorScheme.error  // #D92D20
```

### Background Colors
```kotlin
// Screen background
color = ColorLightTokens.Background  // #F7F7F7

// Card/Surface
color = ColorLightTokens.Surface  // #FFFFFF

// Borders/Dividers
color = ColorLightTokens.Outline  // #EBEBEB
```

## Spacing Patterns

### Common Spacing Combinations

```kotlin
// Button padding
.padding(horizontal = PaddingDimensions.low)  // 4.sdp horizontal

// Component padding
.padding(PaddingDimensions.high)  // 16.sdp all sides

// Between elements
Spacer(modifier = Modifier.height(PaddingDimensions.high))  // 16.sdp gap

// Grid gaps
Arrangement.spacedBy(PaddingDimensions.high)  // 16.sdp between items

// Text field padding
.padding(vertical = PaddingDimensions.medium)  // 8.sdp vertical
```

## Typography Patterns

### Screen Titles
```kotlin
DefaultText(
    text = "Screen Title",
    textStyle = MaterialTheme.typography.titleLarge,  // 18sp, Bold
    textColor = ColorLightTokens.OnBackground
)
```

### Button Text
```kotlin
DefaultText(
    text = "Click Me",
    textStyle = MaterialTheme.typography.labelMedium,  // 16sp, Medium
    textColor = colorWhite
)
```

### Form Labels
```kotlin
DefaultText(
    text = "Email Address",
    textStyle = MaterialTheme.typography.labelLarge,  // 14sp, Medium
    textColor = ColorLightTokens.OnBackground
)
```

### Body Text
```kotlin
DefaultText(
    text = "This is regular body text",
    textStyle = MaterialTheme.typography.bodyMedium,  // 14sp, Normal
    textColor = ColorLightTokens.OnBackground
)
```

### Small/Hint Text
```kotlin
DefaultText(
    text = "Hint or small text",
    textStyle = MaterialTheme.typography.bodySmall,  // 12sp, Normal
    textColor = ColorLightTokens.Tertiary
)
```

## Common Component Examples

### Custom Card
```kotlin
@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(PaddingDimensions.high),
        shape = RoundedCornerShape(CornerDimensions.high),  // 20.sdp
        colors = CardDefaults.cardColors(
            containerColor = ColorLightTokens.Surface  // #FFFFFF
        )
    ) {
        Box(
            modifier = Modifier.padding(PaddingDimensions.high)
        ) {
            content()
        }
    }
}
```

### Custom Input Field
```kotlin
@Composable
fun CustomInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    Column(modifier = modifier.fillMaxWidth()) {
        DefaultText(
            text = label,
            textStyle = MaterialTheme.typography.labelLarge,
            textColor = ColorLightTokens.OnBackground,
            modifier = Modifier.padding(bottom = PaddingDimensions.low)
        )

        DefaultTextField(
            value = value,
            placeholderText = "Enter $label",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            onValueChanged = onValueChange
        )
    }
}
```

### Custom List Item
```kotlin
@Composable
fun CustomListItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .customClickable(onClick = onClick)
            .padding(PaddingDimensions.high),
        color = ColorLightTokens.Surface
    ) {
        Column {
            DefaultText(
                text = title,
                textStyle = MaterialTheme.typography.labelLarge,
                textColor = ColorLightTokens.OnBackground
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(PaddingDimensions.low))
                DefaultText(
                    text = subtitle,
                    textStyle = MaterialTheme.typography.bodySmall,
                    textColor = ColorLightTokens.Tertiary
                )
            }
        }
    }
}
```

## Testing Components

Always include a Preview for each component:

```kotlin
@Preview(showBackground = true)
@Composable
fun MyComponentPreview() {
    BaseTheme {
        Column(
            modifier = Modifier.padding(PaddingDimensions.high),
            verticalArrangement = Arrangement.spacedBy(PaddingDimensions.high)
        ) {
            MyComponent(text = "Example 1")
            MyComponent(text = "Example 2", enabled = false)
        }
    }
}
```

## Validation Checklist

Before committing a new component:

- [ ] Uses ColorLightTokens for colors (not hardcoded hex values)
- [ ] Uses PaddingDimensions for spacing
- [ ] Uses CornerDimensions for corners
- [ ] Uses MaterialTheme.typography for text styles
- [ ] All dimensions use `.sdp` extension
- [ ] Accepts `modifier` parameter
- [ ] Has proper documentation comments
- [ ] Includes `@Preview` composable
- [ ] Follows naming convention (`Default[ComponentName]`)
- [ ] Reuses existing components where applicable
- [ ] Supports RTL layout

---

**Last Updated**: November 2024
**Design System Version**: 1.0
