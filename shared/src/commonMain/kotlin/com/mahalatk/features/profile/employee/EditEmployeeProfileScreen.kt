package com.mahalatk.features.profile.employee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.bottomsheet.SingleSelectBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.imagepicker.rememberImagePickerLauncher
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.features.profile.component.ProfileImagePicker
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.edit_profile
import mahalatk.shared.generated.resources.employee_name
import mahalatk.shared.generated.resources.ic_user
import mahalatk.shared.generated.resources.save
import mahalatk.shared.generated.resources.select_shop
import mahalatk.shared.generated.resources.upload_personal_photo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditEmployeeProfileScreen(
    viewModel: EmployeeProfileViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    var showShopSheet by remember { mutableStateOf(false) }

    // Image picker
    val pickImage = rememberImagePickerLauncher { bytes ->
        viewModel.updateState { copy(employeeImage = bytes, imageError = null) }
    }

    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        ScreenHeader(
            title = stringResource(Res.string.edit_profile),
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 24.dp),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AppColor.Surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Employee Photo (editable)
                    ProfileImagePicker(
                        imageUrl = state.employeeImageUrl,
                        imageBytes = state.employeeImage,
                        label = stringResource(Res.string.upload_personal_photo),
                        errorText = state.imageError?.let { stringResource(it) },
                        editable = true,
                        onPickImage = pickImage,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // 1. Employee Name
                        DefaultTextField(
                            value = state.employeeName,
                            onValueChanged = {
                                viewModel.updateState {
                                    copy(
                                        employeeName = it,
                                        employeeNameError = null
                                    )
                                }
                            },
                            placeholderText = stringResource(Res.string.employee_name),
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            errorText = state.employeeNameError?.let { stringResource(it) },
                            leadingIcon = {
                                Icon(
                                    painterResource(Res.drawable.ic_user), null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MahalatkTheme.primary,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 2. Select Shop
                        DefaultTextField(
                            value = state.selectedShop ?: "",
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.select_shop),
                            isEnabled = false,
                            onClick = { showShopSheet = true },
                            errorText = state.selectedShopError?.let { stringResource(it) },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.KeyboardArrowDown,
                                    null,
                                    tint = MahalatkTheme.primary
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
            DefaultButton(
                text = stringResource(Res.string.save),
                onClick = { viewModel.saveProfile() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }

    // ─── Bottom Sheet ─────────────────────────────────────

    SingleSelectBottomSheet(
        showBottomSheet = showShopSheet,
        title = stringResource(Res.string.select_shop),
        items = state.availableShops,
        selectedItem = state.selectedShop,
        itemLabel = { it },
        onItemSelected = { viewModel.selectShop(it) },
        onDismiss = { showShopSheet = false },
    )
}
