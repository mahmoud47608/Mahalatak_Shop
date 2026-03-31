package com.mahalatk.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.dialog.DeleteAccountDialog
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.menu.MenuItemRow
import com.mahalatk.theme.AppColor
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.change_language
import mahalatk.shared.generated.resources.change_password
import mahalatk.shared.generated.resources.change_phone_number
import mahalatk.shared.generated.resources.delete_account
import mahalatk.shared.generated.resources.edit_profile_data
import mahalatk.shared.generated.resources.general_settings
import mahalatk.shared.generated.resources.ic_delete
import mahalatk.shared.generated.resources.ic_language
import mahalatk.shared.generated.resources.ic_lock
import mahalatk.shared.generated.resources.ic_phone
import mahalatk.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.stringResource

// ─── Color constants matching the design ─────────────────
private val EditProfileColor = Color(0xFF4A90D9)   // Blue
private val ChangePhoneColor = Color(0xFF4CAF50)    // Green
private val ChangeLanguageColor = Color(0xFFE91E63) // Pink/Magenta
private val ChangePasswordColor = Color(0xFF42A5F5) // Light Blue
private val DeleteAccountColor = Color(0xFFF44336)  // Red

@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onChangePhoneNumber: () -> Unit = {},
    onChangeLanguage: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        // ── ScreenHeader ──
        ScreenHeader(
            title = stringResource(Res.string.general_settings),
            onBackClick = onBack,
        )

        // ── Content ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column {
                    // 1 ─ Edit Profile Data
                    AnimatedListItem(0) {
                        MenuItemRow(
                            icon = Res.drawable.ic_profile,
                            iconColor = EditProfileColor,
                            title = stringResource(Res.string.edit_profile_data),
                            onClick = onEditProfile,
                        )
                    }

                    // 2 ─ Change Phone Number
                    AnimatedListItem(1) {
                        MenuItemRow(
                            icon = Res.drawable.ic_phone,
                            iconColor = ChangePhoneColor,
                            title = stringResource(Res.string.change_phone_number),
                            onClick = onChangePhoneNumber,
                        )
                    }

                    // 3 ─ Change Password
                    AnimatedListItem(2) {
                        MenuItemRow(
                            icon = Res.drawable.ic_lock,
                            iconColor = ChangePasswordColor,
                            title = stringResource(Res.string.change_password),
                            onClick = onChangePassword,
                        )
                    }

                    // 4 ─ Change Language
                    AnimatedListItem(3) {
                        MenuItemRow(
                            icon = Res.drawable.ic_language,
                            iconColor = ChangeLanguageColor,
                            title = stringResource(Res.string.change_language),
                            onClick = onChangeLanguage,
                        )
                    }

                    // 5 ─ Delete Account
                    AnimatedListItem(4) {
                        MenuItemRow(
                            icon = Res.drawable.ic_delete,
                            iconColor = DeleteAccountColor,
                            title = stringResource(Res.string.delete_account),
                            onClick = { showDeleteDialog = true },
                        )
                    }
                }
            }
        }
    }

    // Delete Account Dialog
    DeleteAccountDialog(
        visible = showDeleteDialog,
        onConfirm = {
            showDeleteDialog = false
            onDeleteAccount()
        },
        onDismiss = { showDeleteDialog = false },
    )
}


