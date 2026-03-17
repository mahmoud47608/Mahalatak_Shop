package com.aait.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

data object SplashScreen : Screen {
    @Composable
    override fun Content() {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Splash", style = MaterialTheme.typography.headlineLarge)
        }
    }
}

data object LoginScreen : Screen {
    @Composable
    override fun Content() {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Login", style = MaterialTheme.typography.headlineLarge)
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

