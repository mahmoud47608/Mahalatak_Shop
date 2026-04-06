package com.mahalatk.features.employees

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.GroupAdd
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.employee_requests
import mahalatk.shared.generated.resources.employees
import mahalatk.shared.generated.resources.employees_list
import org.jetbrains.compose.resources.stringResource

private val RequestsColor = Color(0xFF4CAF50)
private val EmployeesColor = Color(0xFF5C6BC0)

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
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AnimatedListItem(0) {
                GlassCard(
                    icon = Icons.Rounded.GroupAdd,
                    title = stringResource(Res.string.employee_requests),
                    subtitle = "Manage pending join requests",
                    accentColor = RequestsColor,
                    onClick = onEmployeeRequests,
                )
            }

            AnimatedListItem(1) {
                GlassCard(
                    icon = Icons.Rounded.Groups,
                    title = stringResource(Res.string.employees_list),
                    subtitle = "View & manage your team",
                    accentColor = EmployeesColor,
                    onClick = onEmployeesList,
                )
            }
        }
    }
}

@Composable
private fun GlassCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit,
) {
    val glassShape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(glassShape)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.06f),
                        accentColor.copy(alpha = 0.12f),
                    ),
                ),
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.3f),
                        accentColor.copy(alpha = 0.08f),
                    ),
                ),
                shape = glassShape,
            )
            .noRippleClickable { onClick() }
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Glass icon container
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                accentColor.copy(alpha = 0.06f),
                                accentColor.copy(alpha = 0.14f),
                            )
                        )
                    )
                    .border(0.5.dp, accentColor.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = accentColor,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = subtitle,
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextHint,
                )
            }

            // Arrow
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                accentColor.copy(alpha = 0.06f),
                                accentColor.copy(alpha = 0.14f),
                            )
                        )
                    )
                    .border(0.5.dp, accentColor.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = accentColor,
                )
            }
        }
    }
}
