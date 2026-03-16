package com.aait.base.fcm.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aait.base.fcm.NotificationKey
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FCMTestScreen(
    viewModel: FCMTestViewModel = koinViewModel()
) {
    var orderId by remember { mutableStateOf("123") }
    var roomId by remember { mutableStateOf("1") }
    var chatTitle by remember { mutableStateOf("Test Chat") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text("FCM Test Console", style = MaterialTheme.typography.titleMedium)

                // Inputs for Order Data
                Text("Order Data (for order events)", style = MaterialTheme.typography.labelMedium)
                OutlinedTextField(
                    value = orderId,
                    onValueChange = { orderId = it },
                    label = { Text("ID") },
                    modifier = Modifier.weight(1f)
                )

                // Inputs for Chat Data
                Text("Chat Data (for chat events)", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    OutlinedTextField(
                        value = roomId,
                        onValueChange = { roomId = it },
                        label = { Text("Room ID") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = chatTitle,
                        onValueChange = { chatTitle = it },
                        label = { Text("Chat Title") },
                        modifier = Modifier.weight(2f)
                    )
                }

                // Buttons
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val onTestNotification = viewModel::testNotification

                    // Auth/Home
                    TestBtn("Block", NotificationKey.ACCOUNT_BLOCK, onTestNotification)
                    TestBtn("Deleted", NotificationKey.ACCOUNT_DELETED, onTestNotification)
                    TestBtn("Join Req", NotificationKey.ACCEPT_JOIN_REQUEST, onTestNotification)

                    // Chat
                    val chatData = mapOf(
                        "room_id" to roomId,
                        "title_en" to chatTitle
                    )
                    TestBtn(
                        "New Message",
                        NotificationKey.NEW_MESSAGE,
                        onTestNotification,
                        chatData
                    )

                    // Order Related
                    mapOf("order_id" to orderId)

                    /*
                    TestBtn("User Accepted", NotificationKey.USER_ACCEPTED_ORDER_OFFER, onTestNotification, orderData)
                    */
                    // Default/Unhandled
                    TestBtn("Unhandled", "unknown_key", onTestNotification)
                    TestBtn("Empty Item", "", onTestNotification)
                }
            }
        }
    }
}

@Composable
fun TestBtn(
    label: String,
    key: String,
    onClick: (String, Map<String, String>) -> Unit,
    data: Map<String, String> = emptyMap()
) {
    Button(onClick = { onClick(key, data) }) {
        Text(label, color = Color.White)
    }
}
