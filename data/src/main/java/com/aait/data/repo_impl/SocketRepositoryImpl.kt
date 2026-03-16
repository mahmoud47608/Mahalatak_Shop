package com.aait.data.repo_impl

import android.util.Log
import com.aait.data.di.StringsModule.SocketBaseUrl
import com.aait.domain.entity.base.SocketData
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.repository.SocketRepository
import com.aait.domain.util.fromJson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class SocketRepositoryImpl @Inject constructor(
    @SocketBaseUrl private val socketBaseUrl: String,
    private val preferenceRepository: PreferenceRepository
) : SocketRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var socket: Socket? = null
    private val connectFlow = MutableStateFlow(false)
    private val messagesFlow =
        MutableSharedFlow<Pair<String, List<String>>>(replay = 1, extraBufferCapacity = 1)

    private suspend fun createSocket(): Socket {
        val authData = getAuthData()
        val opts = IO.Options().apply {
            transports = arrayOf(WebSocket.NAME)
            query = buildQuery(authData)
            secure = socketBaseUrl.startsWith("https://") || socketBaseUrl.startsWith("wss://")
        }
        return IO.socket(socketBaseUrl, opts)
    }

    private fun buildQuery(authData: SocketData?): String {
        fun encode(value: Any?): String {
            return URLEncoder.encode(value?.toString().orEmpty(), StandardCharsets.UTF_8.toString())
        }

        return "sender_id=${encode(authData?.id)}" +
                "&sender_name=${encode(authData?.name)}" +
                "&sender_type=${encode(authData?.type)}" +
                "&avatar=${encode(authData?.avatar)}"
    }

    private suspend fun getAuthData(): SocketData? {
        return preferenceRepository.getSocketData()
            .first()
            .takeIf { it.isNotEmpty() }
            ?.fromJson()
    }

    private suspend fun ensureSocketConnected(): Socket {
        socket?.let { existing ->
            if (!existing.connected()) existing.connect()
            return existing
        }

        return createSocket().also { created ->
            setupSocketListeners(created)
            socket = created
            created.connect()
        }
    }

    private fun setupSocketListeners(target: Socket) {
        target.on(Socket.EVENT_CONNECT) {
            Log.e("SOCKET", "EVENT_CONNECT : ")
            connectFlow.value = true
        }
        target.on(Socket.EVENT_DISCONNECT) {
            Log.e("SOCKET", "EVENT_DISCONNECT : ")
            connectFlow.value = false
        }
        target.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e("SOCKET", "EVENT_CONNECT_ERROR : ")
            connectFlow.value = false
        }
    }

    override fun connectToSocket(): Flow<Boolean> = flow {
        scope.launch {
            ensureSocketConnected()
        }
        emitAll(connectFlow.asStateFlow())
    }

    override fun disconnectSocket() {
        socket?.let {
            if (it.connected()) it.disconnect()
            it.off()
            messagesFlow.tryEmit(Pair("", emptyList()))
            connectFlow.value = false
            socket = null
        }
    }

    override fun openChannel(channel: String) = flow {
        val activeSocket = ensureSocketConnected()
        activeSocket.off(channel)
        activeSocket.on(channel) { payload ->
            Log.e("SOCKET", "CHANNEL = $channel\nDATA = ${payload.firstOrNull()}")
            messagesFlow.tryEmit(Pair(channel, payload.map { it.toString() }))
        }
        emitAll(
            messagesFlow
                .filter { it.first == channel }
        )
    }

    override fun emit(type: String, data: MutableMap<Any, Any>) {
        scope.launch {
            val activeSocket = ensureSocketConnected()
            if (!activeSocket.connected()) {
                connectFlow.filter { it }.first()
            }
            activeSocket.emit(type, JSONObject(data.toMap()))
        }
    }
}
