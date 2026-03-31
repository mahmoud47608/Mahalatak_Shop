package com.mahalatk.features.more

import androidx.compose.runtime.Composable
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_privacy
import mahalatk.shared.generated.resources.privacy_policy
import mahalatk.shared.generated.resources.privacy_policy_content
import org.jetbrains.compose.resources.stringResource

@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit = {},
) {
    InfoScreen(
        title = stringResource(Res.string.privacy_policy),
        icon = Res.drawable.ic_privacy,
        content = stringResource(Res.string.privacy_policy_content),
        onBack = onBack,
    )
}
