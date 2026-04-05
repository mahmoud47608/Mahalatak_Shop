package com.mahalatk.features.employees

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.empty.EmptyStatePlaceholder
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.image.UserAvatar
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.employees_list
import mahalatk.shared.generated.resources.ic_profile
import mahalatk.shared.generated.resources.no_current_employees
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmployeesListScreen(
    viewModel: EmployeesListViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onAddEmployee: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = AppColor.ScreenBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEmployee,
                containerColor = AppColor.Primary,
                contentColor = Color.White,
                shape = CircleShape,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()),
        ) {
            ScreenHeader(
                title = stringResource(Res.string.employees_list),
                onBackClick = onBack,
            )

            if (state.employees.isEmpty()) {
                EmptyStatePlaceholder(
                    icon = Res.drawable.ic_profile,
                    message = stringResource(Res.string.no_current_employees),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 80.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    itemsIndexed(
                        state.employees,
                        key = { _, e -> e.id },
                        contentType = { _, _ -> "employee" }) { index, employee ->
                        AnimatedListItem(index) {
                            CurrentEmployeeCard(
                                employee = employee,
                                onDelete = { viewModel.deleteEmployee(employee.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentEmployeeCard(
    employee: Employee,
    onDelete: () -> Unit,
) {
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        exit = fadeOut(animationSpec = tween(250)) +
                shrinkVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                ),
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 16.dp,
            contentPadding = 0.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar
                UserAvatar(
                    imageUrl = employee.imageUrl,
                    initials = employee.name,
                    size = 50.dp,
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = employee.name,
                        style = MahalatkTheme.bodyMedium,
                        color = AppColor.TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Phone, null,
                            modifier = Modifier.size(13.dp),
                            tint = AppColor.TextHint,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = employee.phone,
                            style = MahalatkTheme.labelSmall,
                            color = AppColor.TextHint,
                            fontSize = 12.sp,
                        )
                    }
                }

                // Delete button
                IconButton(
                    onClick = {
                        visible = false
                        onDelete()
                    },
                    modifier = Modifier.size(38.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = AppColor.Error.copy(alpha = 0.1f),
                    ),
                ) {
                    Icon(
                        Icons.Rounded.Close, null,
                        modifier = Modifier.size(20.dp),
                        tint = AppColor.Error,
                    )
                }
            }
        }
    }
}
