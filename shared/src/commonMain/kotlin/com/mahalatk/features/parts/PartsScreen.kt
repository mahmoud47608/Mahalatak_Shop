package com.mahalatk.features.parts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.parts
import org.jetbrains.compose.resources.stringResource

@Composable
fun PartsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = stringResource(Res.string.parts))
    }
}
