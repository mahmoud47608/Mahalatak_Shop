package com.aait.ui.screens.showcase

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aait.ui.picker.video.rememberVideoPicker
import com.aait.ui.component.banner.CarouselBanner
import com.aait.ui.component.button.ButtonStyle
import com.aait.ui.component.button.DefaultButton
import com.aait.ui.component.button.RadioOption
import com.aait.ui.component.inputs.DefaultDropDown
import com.aait.ui.component.inputs.DefaultTextField
import com.aait.ui.component.inputs.DefaultTextFieldPhoneWithCode
import com.aait.ui.component.inputs.GenderDropDown
import com.aait.ui.component.inputs.LocationPickerField
import com.aait.ui.component.inputs.MaritalStatusDropDown
import com.aait.ui.component.inputs.date.DatePickerBottomSheet
import com.aait.ui.component.inputs.time.TimePickerBottomSheet
import com.aait.ui.component.text.DefaultText
import com.aait.ui.picker.file.rememberFilePicker
import com.aait.ui.picker.image.rememberImagePicker
import com.aait.ui.picker.image.rememberMultiImagePicker
import com.aait.ui.language.AppLocaleManager
import com.aait.ui.language.LanguageSheet
import com.aait.ui.picker.media.rememberMediaPicker
import com.aait.ui.picker.media.rememberMultiMediaPicker
import com.aait.ui.picker.video.rememberMultiVideoPicker
import com.aait.ui.component.text.PriceWithCurrency
import com.aait.ui.component.sheet.MultiSelectionPickerSheet
import com.aait.ui.component.sheet.SinglePickerSheet
import com.aait.ui.component.inputs.SelectorField
import com.aait.ui.component.attachment.AttachFileComponent
import com.aait.ui.component.attachment.AttachImageComponent
import com.aait.ui.component.attachment.AttachVideoComponent
import com.aait.ui.theme.BaseTheme
import com.aait.ui.theme.PaddingDimensions
import com.aait.domain.entity.general.DataItem
import com.aait.domain.entity.general.LatLngModel
import com.aait.helool.util.navigation.ResultKeys
import com.aait.helool.util.navigation.ResultStore
import com.aait.helool.util.navigation.rememberResultStore
import androidx.compose.material.icons.Icons
import com.aait.ui.component.empty_state.EmptyComponent
import com.aait.ui.component.status.StatusBadge
import com.aait.ui.component.status.StatusType
import com.aait.ui.component.utilis.ShimmerRectangle
import com.aait.ui.component.utilis.ShimmerCircle
import com.aait.ui.component.stepper.DefaultStepper
import com.aait.ui.component.inputs.QuantitySelector
import com.aait.ui.util.NetworkConnectivityObserver
import com.aait.ui.util.ConnectivityStatus
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import com.aait.ui.theme.ExtendedTheme

@Composable
fun ComponentShowcaseScreen(resultStore: ResultStore, onPickLocation: () -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(PaddingDimensions.high),
        verticalArrangement = Arrangement.spacedBy(PaddingDimensions.high)
    ) {
        CarouselBanner(
            images = listOf(
                "https://picsum.photos/600/400?random=1",
                "https://picsum.photos/600/400?random=2",
                "https://picsum.photos/600/400?random=3"
            )
        )
        DefaultText(
            text = "Component Library Showcase",
            textStyle = MaterialTheme.typography.headlineMedium,
            textColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        ComponentSection(title = "Buttons") {
            ButtonShowcase()
        }

        ComponentSection(title = "Text Components") {
            TextShowcase()
        }

        ComponentSection(title = "Input Fields") {
            InputFieldsShowcase()
        }

        ComponentSection(title = "Dropdown Components") {
            DropdownShowcase()
        }

        ComponentSection(title = "Radio Buttons") {
            RadioButtonsShowcase()
        }

        ComponentSection(title = "Pickers & Media") {
            PickersShowcase(resultStore, onPickLocation = onPickLocation)
        }

        ComponentSection(title = "New Components & Helpers") {
            NewComponentsShowcase()
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ComponentSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingDimensions.high),
            verticalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)
        ) {
            DefaultText(
                text = title,
                textStyle = MaterialTheme.typography.titleLarge,
                textColor = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider()

            content()
        }
    }
}

@Composable
private fun ButtonShowcase() {
    var buttonEnabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)) {
        DefaultText(
            text = "Button Styles:",
            textStyle = MaterialTheme.typography.labelMedium
        )

        DefaultButton(
            text = "Primary Button",
            onClick = { buttonEnabled = !buttonEnabled },
            style = ButtonStyle.PRIMARY
        )
        DefaultButton(text = "Success Button", onClick = { }, style = ButtonStyle.SUCCESS)
        DefaultButton(text = "OUTLINED Button", onClick = { }, style = ButtonStyle.OUTLINED)
        DefaultButton(text = "Error Button", onClick = { }, style = ButtonStyle.ERROR)
        DefaultButton(text = "Secondary Button", onClick = { }, style = ButtonStyle.SECONDARY)
        DefaultButton(
            text = "Disabled Button",
            onClick = { },
            enabled = false,
            style = ButtonStyle.PRIMARY
        )

        DefaultText(
            text = "Toggle button state by clicking Primary Button",
            textStyle = MaterialTheme.typography.bodySmall,
            textColor = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
private fun TextShowcase() {
    Column(verticalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)) {
        DefaultText(text = "Headline Large", textStyle = MaterialTheme.typography.headlineLarge)
        DefaultText(text = "Headline Medium", textStyle = MaterialTheme.typography.headlineMedium)
        DefaultText(text = "Title Large", textStyle = MaterialTheme.typography.titleLarge)
        DefaultText(
            text = "Body Large - This is a body text example showing how the DefaultText component renders longer content with proper styling.",
            textStyle = MaterialTheme.typography.bodyLarge
        )
        DefaultText(text = "Body Medium", textStyle = MaterialTheme.typography.bodyMedium)
        DefaultText(text = "Body Small", textStyle = MaterialTheme.typography.bodySmall)
        DefaultText(
            text = "This text is centered",
            textStyle = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        DefaultText(
            text = "This is a very long text that demonstrates the maxLines property. It will be truncated with an ellipsis if it exceeds 2 lines. Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            textStyle = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun InputFieldsShowcase() {
    var textValue by remember { mutableStateOf("") }
    var phoneValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)) {
        DefaultText(text = "Text Input Fields:", textStyle = MaterialTheme.typography.labelMedium)
        DefaultTextField(
            value = textValue,
            onValueChanged = { textValue = it },
            placeholderText = "Enter text here",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
        DefaultTextField(
            value = emailValue,
            onValueChanged = { emailValue = it },
            placeholderText = "Enter email",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        )
        DefaultTextField(
            value = "",
            onValueChanged = { },
            placeholderText = "This field has an error",
            errorText = "This is an error message",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
        DefaultTextField(
            value = "Disabled field",
            onValueChanged = { },
            isEnabled = false,
            placeholderText = "Disabled",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
        DefaultText(
            text = "Phone Number Fields:",
            textStyle = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        DefaultTextFieldPhoneWithCode(
            phoneValue = phoneValue,
            onPhoneChange = { phoneValue = it },
            title = "Saudi Phone Number",
            countryCode = "+966",
            isRequired = true
        )
        DefaultTextFieldPhoneWithCode(
            phoneValue = "",
            onPhoneChange = { },
            title = "US Phone (No Flag)",
            countryCode = "+1",
            countryFlagIcon = null
        )
    }
}

@Composable
private fun DropdownShowcase() {
    var selectedCity by remember { mutableStateOf<DataItem?>(null) }
    var selectedGender by remember { mutableStateOf<DataItem?>(null) }
    var selectedMaritalStatus by remember { mutableStateOf<DataItem?>(null) }

    val cities = listOf(
        DataItem(1, "Riyadh", null, "riyadh"),
        DataItem(2, "Jeddah", null, "jeddah"),
        DataItem(3, "Dammam", null, "dammam"),
        DataItem(4, "Mecca", null, "mecca"),
        DataItem(5, "Medina", null, "medina")
    )

    Column(verticalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)) {
        DefaultDropDown(
            title = "Select City",
            placeholderText = "Choose a city",
            selectedItem = selectedCity,
            items = cities,
            onItemSelected = { selectedCity = it },
            isRequired = true
        )
        GenderDropDown(
            title = "Gender",
            placeholderText = "Select gender",
            selectedItem = selectedGender,
            onItemSelected = { selectedGender = it })
        MaritalStatusDropDown(
            title = "Marital Status",
            placeholderText = "Select marital status",
            selectedItem = selectedMaritalStatus,
            gender = selectedGender,
            onItemSelected = { selectedMaritalStatus = it })
        DefaultDropDown(
            title = "Disabled Dropdown",
            placeholderText = "This is disabled",
            selectedItem = null,
            items = cities,
            isEnabled = false,
            onItemSelected = { })
    }
}

@Composable
private fun RadioButtonsShowcase() {
    var selectedOption by remember { mutableStateOf("option1") }

    Column(verticalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)) {
        DefaultText(text = "Select an option:", textStyle = MaterialTheme.typography.labelMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingDimensions.high)
        ) {
            RadioOption(
                selected = selectedOption == "option1",
                label = "Option 1",
                onSelect = { selectedOption = "option1" })
            RadioOption(
                selected = selectedOption == "option2",
                label = "Option 2",
                onSelect = { selectedOption = "option2" })
            RadioOption(
                selected = selectedOption == "option3",
                label = "Option 3",
                onSelect = { selectedOption = "option3" })
        }
        DefaultText(
            text = "Selected: $selectedOption",
            textStyle = MaterialTheme.typography.bodySmall,
            textColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun PickersShowcase(resultStore: ResultStore, onPickLocation: () -> Unit) {
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var selectedLanguage by remember { mutableStateOf("en") }
    var lastAction by remember { mutableStateOf("Ready") }

    val pickImage = rememberImagePicker { path -> lastAction = "Image selected: $path" }
    val pickMultiImages = rememberMultiImagePicker { paths ->
        lastAction = "Multi Media selected: ${paths.size} files"
    }
    val pickFile = rememberFilePicker { path -> lastAction = "File selected: $path" }
    val pickMedia = rememberMediaPicker(
        onMediaSelected = { path -> lastAction = "Media selected: $path" },
        allowVideo = true,
        currentMediaPath = null
    )
    val pickMultiMedia = rememberMultiMediaPicker(onMediaSelected = { paths ->
        lastAction = "Multi Media selected: ${paths.size} files"
    }, allowVideo = true, currentMediaPaths = null)
    val pickVideo = rememberVideoPicker(
        onVideoSelected = { path -> lastAction = "Video selected: $path" },
        currentVideoPath = null
    )
    val pickMultiVideo = rememberMultiVideoPicker(onVideosSelected = { paths ->
        lastAction = "Multi Video selected: ${paths.size} files"
    }, currentVideoPaths = emptyList())

    val context = LocalContext.current
    val appLocaleManager = remember { AppLocaleManager(context) }

    var showDatePickerSheet by remember { mutableStateOf(false) }
    var showTimePickerSheet by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showSingleSheet by remember { mutableStateOf(false) }
    var showMultiSheet by remember { mutableStateOf(false) }

    var selectedLocation by remember { mutableStateOf<String?>(null) }
    var selectedSingleItem by remember { mutableStateOf<DataItem?>(null) }
    var selectedMultiItems by remember { mutableStateOf<List<DataItem>>(emptyList()) }

    val exampleItems = remember {
        listOf(
            DataItem(1, "Option A"),
            DataItem(2, "Option B"),
            DataItem(3, "Option C"),
            DataItem(4, "Option D"),
            DataItem(5, "Option E")
        )
    }

    val locationResult = resultStore.getResult<LatLngModel>(ResultKeys.LOCATION.name)

    LaunchedEffect(locationResult) {
        if (locationResult != null) {
            selectedLocation = locationResult.mapDesc
            lastAction = "Location picked: ${locationResult.mapDesc}"
            resultStore.removeResult(ResultKeys.LOCATION.name)
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)) {
        SelectorField(
            value = selectedDate ?: "",
            title = "Date Selection",
            placeholderText = "Select a date",
            onClick = { showDatePickerSheet = true },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            })
        if (showDatePickerSheet) {
            DatePickerBottomSheet(onConfirm = {
                selectedDate = it; lastAction = "Date selected: $it"
            }, onDismiss = { showDatePickerSheet = false })
        }

        SelectorField(
            value = selectedTime ?: "",
            title = "Time Selection",
            placeholderText = "Select a time",
            onClick = { showTimePickerSheet = true },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            })
        if (showTimePickerSheet) {
            TimePickerBottomSheet(onConfirm = {
                selectedTime = it; lastAction = "Time selected: $it"
            }, onDismiss = { showTimePickerSheet = false })
        }

        SelectorField(
            value = if (appLocaleManager.getCurrentLocale() == "ar") "العربية" else "English",
            title = "Language",
            placeholderText = "Select language",
            onClick = { showLanguageSheet = true },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            })
        if (showLanguageSheet) {
            LanguageSheet(
                onDismiss = { showLanguageSheet = false },
                onLanguageSelected = { appLocaleManager.setLocale(it); showLanguageSheet = false })
        }

        LocationPickerField(
            value = selectedLocation,
            title = "Location Picker",
            placeholderText = "Click to pick location",
            onClick = onPickLocation
        )

        SelectorField(
            value = selectedSingleItem?.name ?: "",
            title = "Single Selection",
            placeholderText = "Choose one option",
            onClick = { showSingleSheet = true })
        if (showSingleSheet) {
            SinglePickerSheet(
                title = "Choose One Option",
                list = exampleItems,
                selectedItem = selectedSingleItem,
                onItemSelected = { selectedSingleItem = it },
                onDismiss = { showSingleSheet = false })
        }

        SelectorField(
            value = selectedMultiItems.joinToString { it.name ?: "" },
            title = "Multi Selection",
            placeholderText = "Choose multiple options",
            onClick = { showMultiSheet = true })
        if (showMultiSheet) {
            MultiSelectionPickerSheet(
                title = "Choose Multiple Options",
                list = exampleItems,
                selectedItems = selectedMultiItems,
                onItemsSelected = { selectedMultiItems = it },
                onDismiss = { showMultiSheet = false })
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        DefaultText(
            text = "Attachment Components:",
            textStyle = MaterialTheme.typography.labelMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AttachImageComponent(onPickImageClicked = pickImage)
            AttachVideoComponent(onPickVideoClicked = pickVideo)
            AttachFileComponent(onPickFileClicked = pickFile)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        DefaultText(text = "Multi Media Pickers:", textStyle = MaterialTheme.typography.labelMedium)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DefaultButton(
                text = "Multi Image",
                onClick = pickMultiImages,
                modifier = Modifier.weight(1f)
            )
            DefaultButton(text = "Media", onClick = pickMedia, modifier = Modifier.weight(1f))
            DefaultButton(
                text = "Multi Media",
                onClick = pickMultiMedia,
                modifier = Modifier.weight(1f)
            )
            DefaultButton(
                text = "Multi Video",
                onClick = pickMultiVideo,
                modifier = Modifier.weight(1f)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            )
        ) {
            DefaultText(
                text = "Last Action Output:",
                textStyle = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
            DefaultText(
                text = lastAction,
                textStyle = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun NewComponentsShowcase() {
    val context = LocalContext.current
    val networkObserver = remember { NetworkConnectivityObserver(context) }
    val networkStatus by networkObserver.observe()
        .collectAsState(initial = ConnectivityStatus.Available)
    var quantity by remember { mutableStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(PaddingDimensions.high)) {
        DefaultText(text = "Shimmer Loading:", textStyle = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)) {
            ShimmerCircle(modifier = Modifier.size(50.dp))
            Column(verticalArrangement = Arrangement.spacedBy(PaddingDimensions.low)) {
                ShimmerRectangle(modifier = Modifier
                    .width(150.dp)
                    .height(20.dp))
                ShimmerRectangle(modifier = Modifier
                    .width(100.dp)
                    .height(15.dp))
            }
        }

        HorizontalDivider()
        DefaultText(text = "Status Badges:", textStyle = MaterialTheme.typography.labelMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)
        ) {
            StatusBadge(text = "Delivered", statusType = StatusType.SUCCESS)
            StatusBadge(text = "Canceled", statusType = StatusType.ERROR)
            StatusBadge(text = "Pending", statusType = StatusType.WARNING)
        }

        HorizontalDivider()
        DefaultText(text = "Stepper:", textStyle = MaterialTheme.typography.labelMedium)
        DefaultStepper(steps = listOf("Cart", "Address", "Payment"), currentStep = 1)

        HorizontalDivider()
        DefaultText(
            text = "Quantity Selector (Max: 5):",
            textStyle = MaterialTheme.typography.labelMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            QuantitySelector(
                quantity = quantity,
                onQuantityChange = { quantity = it },
                maxQuantity = 5
            )
            Spacer(modifier = Modifier.width(PaddingDimensions.high))
            PriceWithCurrency(price = (quantity * 150).toString())
        }

        HorizontalDivider()
        DefaultText(text = "Helpers:", textStyle = MaterialTheme.typography.labelMedium)
        DefaultText(
            text = "Network: $networkStatus",
            textColor = if (networkStatus == ConnectivityStatus.Available) ExtendedTheme.colors.success else MaterialTheme.colorScheme.error
        )

        HorizontalDivider()
        DefaultText(
            text = "Empty State (Default):",
            textStyle = MaterialTheme.typography.labelMedium
        )
        Box(modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()) {
            EmptyComponent(
                iconPainter = androidx.compose.ui.graphics.vector.rememberVectorPainter(
                    image = Icons.Default.Close
                )
            )
        }
    }
}
