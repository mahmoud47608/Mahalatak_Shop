package com.aait.ui.screens.general

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GeneralScreenContent(uiState: GeneralState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Logo placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Add image loading with Coil/Kamel for CMP
            Text(text = uiState.image ?: "")
        }

        // Content
        uiState.content?.let {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // TODO: Add HTML rendering for CMP (platform-specific WebView)
                Text(text = it)
            }
        }
    }
}
