package com.aait.base.common.screens.general.terms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aait.base.common.screens.general.GeneralScreenContent
import com.aait.base.ui.theme.BaseTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TermsScreen() {
    val viewModel: TermsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GeneralScreenContent(uiState)

}

@Preview(showBackground = true)
@Composable
fun AboutAppScreenPreview() {
    BaseTheme {
        TermsScreen()
    }
}