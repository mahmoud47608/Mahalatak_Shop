package com.mahalatk.features.chat

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ChatTab { Orders, Inquiries }

@Immutable
data class ChatConversation(
    val id: String,
    val customerName: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val isInquiry: Boolean = false,
    val imageUrl: String = "",
)

@Immutable
data class ChatMessage(
    val id: String,
    val text: String,
    val time: String,
    val isMe: Boolean,
)

@Immutable
data class ChatState(
    val isLoading: Boolean = true,
    val selectedTab: ChatTab = ChatTab.Orders,
    val conversations: ImmutableList<ChatConversation> = persistentListOf(
        ChatConversation(
            "1",
            "Hader Al-Alawi",
            "I want to change the delivery address please",
            "02:30 PM",
            2,
            true,
        ),
        ChatConversation(
            "2",
            "Fahd Al-Shehri",
            "Is the order ready for pickup?",
            "01:45 PM",
            0,
            true,
        ),
        ChatConversation(
            "3",
            "Mohamed Ali",
            "Thank you for the fast delivery!",
            "12:00 PM",
            1,
            false,
        ),
        ChatConversation("4", "Sara Ahmed", "Can I add one more item?", "11:30 AM", 0, false),
        ChatConversation(
            "5",
            "Khalid Omar",
            "What products do you have?",
            "Yesterday",
            1,
            false,
            isInquiry = true,
        ),
        ChatConversation(
            "6",
            "Nour Hassan",
            "Do you deliver to Jeddah?",
            "Mar 25",
            0,
            true,
            isInquiry = true,
        ),
    ),
)

@Immutable
data class ChatDetailState(
    val customerName: String = "",
    val isOnline: Boolean = true,
    val messageText: String = "",
    val messages: ImmutableList<ChatMessage> = persistentListOf(
        ChatMessage("1", "Hello, I need help with my order", "02:25 PM", false),
        ChatMessage("2", "Sure! How can I help you?", "02:26 PM", true),
        ChatMessage("3", "I want to change the delivery address please", "02:28 PM", false),
        ChatMessage("4", "No problem, please send me the new address", "02:29 PM", true),
        ChatMessage("5", "123 Main Street, Building 5, Apt 12", "02:30 PM", false),
        ChatMessage("6", "Got it! I've updated the address", "02:31 PM", true),
        ChatMessage("7", "Thank you so much!", "02:32 PM", false),
    ),
)

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatState())

    init {
        viewModelScope.launch {
            delay(1000)
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    val uiState: StateFlow<ChatState> = _uiState.asStateFlow()

    val filteredConversations: StateFlow<ImmutableList<ChatConversation>> = _uiState.map { state ->
        when (state.selectedTab) {
            ChatTab.Orders -> state.conversations.filter { !it.isInquiry }.toImmutableList()
            ChatTab.Inquiries -> state.conversations.filter { it.isInquiry }.toImmutableList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), persistentListOf())

    fun selectTab(tab: ChatTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}

class ChatDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatDetailState())
    val uiState: StateFlow<ChatDetailState> = _uiState.asStateFlow()

    fun setCustomerName(name: String) {
        _uiState.update { it.copy(customerName = name) }
    }

    fun onMessageTextChanged(text: String) {
        _uiState.update { it.copy(messageText = text) }
    }

    private var messageIdCounter = 100

    fun sendMessage() {
        val text = _uiState.value.messageText.trim()
        if (text.isEmpty()) return
        val newMessage = ChatMessage(
            id = (++messageIdCounter).toString(),
            text = text,
            time = "Now",
            isMe = true,
        )
        _uiState.update {
            it.copy(
                messages = (it.messages + newMessage).toImmutableList(),
                messageText = "",
            )
        }
    }
}
