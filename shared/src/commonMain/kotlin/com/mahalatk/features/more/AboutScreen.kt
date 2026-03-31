package com.mahalatk.features.more

import androidx.compose.runtime.Composable
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.about_app
import mahalatk.shared.generated.resources.about_app_content
import mahalatk.shared.generated.resources.ic_about
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutScreen(
    onBack: () -> Unit = {},
) {
    InfoScreen(
        title = stringResource(Res.string.about_app),
        icon = Res.drawable.ic_about,
        content = stringResource(Res.string.about_app_content),
        onBack = onBack,
    )
}
