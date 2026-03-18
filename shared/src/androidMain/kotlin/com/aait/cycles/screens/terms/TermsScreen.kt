package com.aait.cycles.screens.terms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aait.cycles.screens.general.GeneralScreenContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TermsScreen() {
    val viewModel: TermsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GeneralScreenContent(uiState)

}

