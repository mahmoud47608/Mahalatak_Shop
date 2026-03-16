package com.aait.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.aait.ui.screens.auth.login.LoginViewModel
import com.aait.ui.screens.splash.SplashViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

data object SplashScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<SplashViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            delay(1500)
            navigator.replace(LoginScreen)
        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Splash", style = MaterialTheme.typography.headlineLarge)
        }
    }
}

data object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val authData by viewModel.authData.collectAsState()

        LaunchedEffect(authData) {
            if (authData != null) {
                navigator.replace(HomeScreen)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.mobile,
                onValueChange = { viewModel.updateState { copy(mobile = it) } },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.updateState { copy(password = it) } },
                label = { Text("Password") },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}

data object HomeScreen : Screen {
    @Composable
    override fun Content() {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Home", style = MaterialTheme.typography.headlineLarge)
        }
    }
}

data class ChatScreen(val roomId: Int, val title: String? = null) : Screen {
    @Composable
    override fun Content() {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Chat room #$roomId${title?.let { " - $it" } ?: ""}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

data object ComponentShowcaseScreen : Screen {
    @Composable
    override fun Content() {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Component Showcase", style = MaterialTheme.typography.headlineLarge)
        }
    }
}
