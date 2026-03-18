package com.aait.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.aait.base.common.component.dialog.PriorityAlertDialog
import com.aait.base.fcm.NotificationHandler
import com.aait.base.ui.navigation.AppScaffold
import com.aait.base.ui.navigation.NavigationEvent
import com.aait.base.ui.theme.BaseTheme
import com.aait.base.util.UIMessage
import com.mahalatak.R
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseTheme {
                // ==================== State Collection ====================
                val isLoading by viewModel.isLoading.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                // ==================== Message Handling ====================


                LaunchedEffect(Unit) {
                    viewModel.uiMessages.collectLatest { message ->
                        when (message) {
                            is UIMessage.Text ->
                                snackbarHostState.showSnackbar(message.message)

                            is UIMessage.Resource ->
                                snackbarHostState.showSnackbar(getString(message.messageResId))
                        }
                    }
                }

                // ==================== Auth Failure Dialog ====================
                val isAuthFailed by viewModel.isAuthFailed.collectAsState()

                if (isAuthFailed) {
                    PriorityAlertDialog(
                        title = stringResource(R.string.session_expired_title),
                        message = stringResource(R.string.session_expired_message),
                        buttonText = stringResource(R.string.ok),
                        onButtonClick = {
                            viewModel.onAuthFailed(false)
                            viewModel.onLogout()
                            // Navigate to login or visitor screen
                        }
                    )
                }

                // ==================== Account Blocked Dialog ====================
                val isBlocked by viewModel.isBlocked.collectAsState()

                if (isBlocked) {
                    PriorityAlertDialog(
                        title = stringResource(R.string.account_blocked_title),
                        message = stringResource(R.string.account_blocked_message),
                        buttonText = stringResource(R.string.ok),
                        onButtonClick = {
                            viewModel.onBlocked(false)
                            viewModel.onLogout()
                            // Navigate to visitor or login screen
                        }
                    )
                }
                // ==================== Main UI ====================
                val backStack = viewModel.navigationStack

                AppScaffold(
                    backStack = backStack,
                    isLoading = isLoading,
                    snackbarHostState = snackbarHostState
                )
            }
        }
        if (savedInstanceState == null) {
            handleNotificationIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }
    private fun handleNotificationIntent(intent: Intent?) {
        intent?.let {
            when (intent.action) {
                NotificationHandler.ACTION_NAVIGATE_TO_LOGIN -> {
                    viewModel.handleNavigationEvent(NavigationEvent.NavigateToLogin)
                }

                NotificationHandler.ACTION_NAVIGATE_TO_HOME -> {
                    viewModel.handleNavigationEvent(NavigationEvent.NavigateToHome)
                }

                NotificationHandler.ACTION_NAVIGATE_TO_CHAT -> {
                    val roomId = intent.getIntExtra(NotificationHandler.EXTRA_CHAT_ROOM_ID, 0)
                    val title = intent.getStringExtra(NotificationHandler.EXTRA_CHAT_TITLE)
                    if (roomId > 0) {
                        viewModel.handleNavigationEvent(
                            NavigationEvent.NavigateToChat(
                                roomId = roomId,
                                title = title
                            )
                        )
                    }
                }
            }
            intent.action = null
        }
    }

}
