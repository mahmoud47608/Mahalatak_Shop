package com.mahalatk.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.bottomsheet.SingleSelectBottomSheet
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.dialog.DeleteAccountDialog
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.menu.MenuItemRow
import com.mahalatk.common.util.getCurrentLanguageCode
import com.mahalatk.common.util.rememberLanguageChanger
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.change_language
import mahalatk.shared.generated.resources.change_password
import mahalatk.shared.generated.resources.dark_mode
import mahalatk.shared.generated.resources.delete_account
import mahalatk.shared.generated.resources.general_settings
import mahalatk.shared.generated.resources.ic_delete
import mahalatk.shared.generated.resources.ic_language
import mahalatk.shared.generated.resources.ic_lock
import mahalatk.shared.generated.resources.ic_settings
import mahalatk.shared.generated.resources.select_language
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val ChangePasswordColor = Color(0xFF5EAAB0)   // soft teal (logo)
private val ChangeLanguageColor = Color(0xFFCE9B58)   // warm amber (logo)
private val DarkModeColor = Color(0xFF6B9BA8)          // cool slate (logo)
private val DeleteAccountColor = AppColor.Error

private data class LanguageOption(val code: String, val label: String)

@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
    viewModel: SettingsViewModel = koinViewModel(),
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    val settingsState by viewModel.uiState.collectAsState()
    val isDarkMode = settingsState.isDarkMode
    val currentLang = getCurrentLanguageCode()
    val changeLanguage = rememberLanguageChanger()

    val languages = remember {
        listOf(
            LanguageOption("en", "English"),
            LanguageOption("ar", "اللغة العربية"),
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        ScreenHeader(
            title = stringResource(Res.string.general_settings),
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp),
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                cornerRadius = 16.dp,
                contentPadding = 0.dp,
            ) {
                Column {
                    // 1 ─ Change Password
                    AnimatedListItem(0) {
                        MenuItemRow(
                            icon = Res.drawable.ic_lock,
                            iconColor = ChangePasswordColor,
                            title = stringResource(Res.string.change_password),
                            onClick = onChangePassword,
                        )
                    }

                    // 2 ─ Change Language
                    AnimatedListItem(1) {
                        MenuItemRow(
                            icon = Res.drawable.ic_language,
                            iconColor = ChangeLanguageColor,
                            title = stringResource(Res.string.change_language),
                            showTopDivider = true,
                            onClick = { showLanguageSheet = true },
                        )
                    }

                    // 3 ─ Dark Mode Toggle
                    AnimatedListItem(2) {
                        DarkModeToggleRow(
                            isDarkMode = isDarkMode,
                            onToggle = { viewModel.toggleDarkMode() },
                        )
                    }

                    // 4 ─ Delete Account
                    AnimatedListItem(3) {
                        MenuItemRow(
                            icon = Res.drawable.ic_delete,
                            iconColor = DeleteAccountColor,
                            title = stringResource(Res.string.delete_account),
                            showTopDivider = true,
                            onClick = { showDeleteDialog = true },
                        )
                    }
                }
            }
        }
    }

    DeleteAccountDialog(
        visible = showDeleteDialog,
        onConfirm = {
            showDeleteDialog = false
            onDeleteAccount()
        },
        onDismiss = { showDeleteDialog = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showLanguageSheet,
        title = stringResource(Res.string.select_language),
        items = languages,
        selectedItem = languages.find { it.code == currentLang },
        itemLabel = { it.label },
        onItemSelected = {
            showLanguageSheet = false
            changeLanguage(it.code)
        },
        onDismiss = { showLanguageSheet = false },
    )
}

@Composable
private fun DarkModeToggleRow(
    isDarkMode: Boolean,
    onToggle: () -> Unit,
) {
    Column {
        // Glass divider
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .height(0.5.dp)
                .background(
                    color = Color.LightGray.copy(alpha = if (AppColor.isDark) 0.5f else 1f)
                )
        )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            DarkModeColor.copy(alpha = 0.06f),
                            DarkModeColor.copy(alpha = 0.14f),
                        )
                    )
                )
                .border(0.5.dp, DarkModeColor.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painterResource(Res.drawable.ic_settings), null,
                tint = DarkModeColor,
                modifier = Modifier.size(16.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = stringResource(Res.string.dark_mode),
            style = MahalatkTheme.bodyMedium,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = isDarkMode,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AppColor.Primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = AppColor.TextHint.copy(alpha = 0.3f),
                uncheckedBorderColor = Color.Transparent,
            ),
        )
    }
    } // end Column
}
