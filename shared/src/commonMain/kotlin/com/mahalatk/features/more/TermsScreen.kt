package com.mahalatk.features.more

import androidx.compose.runtime.Composable
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_terms
import mahalatk.shared.generated.resources.terms_conditions
import mahalatk.shared.generated.resources.terms_conditions_content
import org.jetbrains.compose.resources.stringResource

@Composable
fun TermsScreen(
    onBack: () -> Unit = {},
) {
    InfoScreen(
        title = stringResource(Res.string.terms_conditions),
        icon = Res.drawable.ic_terms,
        content = stringResource(Res.string.terms_conditions_content),
        onBack = onBack,
    )
}
