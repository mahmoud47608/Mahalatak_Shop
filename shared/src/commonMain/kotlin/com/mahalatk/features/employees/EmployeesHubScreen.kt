package com.mahalatk.features.employees

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import com.mahalatk.theme.SpacingDimensions
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.employee_requests
import mahalatk.shared.generated.resources.employees
import mahalatk.shared.generated.resources.employees_list
import mahalatk.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val RequestsColor = Color(0xFF4CAF50)
private val EmployeesColor = Color(0xFF42A5F5)

@Composable
fun EmployeesHubScreen(
    onBack: () -> Unit = {},
    onEmployeeRequests: () -> Unit = {},
    onEmployeesList: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        ScreenHeader(
            title = stringResource(Res.string.employees),
            onBackClick = onBack,
        )

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
                    AnimatedListItem(0) {
                        HubMenuItem(
                            icon = Res.drawable.ic_profile,
                            iconColor = RequestsColor,
                            title = stringResource(Res.string.employee_requests),
                            onClick = onEmployeeRequests,
                        )
                    }
                    AnimatedListItem(1) {
                        HubMenuItem(
                            icon = Res.drawable.ic_profile,
                            iconColor = EmployeesColor,
                            title = stringResource(Res.string.employees_list),
                            onClick = onEmployeesList,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HubMenuItem(
    icon: DrawableResource,
    iconColor: Color,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(iconColor.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painterResource(icon), null,
                tint = iconColor,
                modifier = Modifier.size(16.dp),
            )
        }

        Spacer(modifier = Modifier.width(SpacingDimensions.sp2))

        Text(
            text = title,
            style = MahalatkTheme.bodyMedium,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )

        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
            tint = AppColor.TextHint,
            modifier = Modifier.size(18.dp),
        )
    }
}
