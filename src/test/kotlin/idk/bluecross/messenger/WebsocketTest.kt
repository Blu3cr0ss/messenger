package idk.bluecross.messenger

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.lang.Nullable
import org.springframework.messaging.converter.StringMessageConverter
import org.springframework.messaging.simp.stomp.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.reflect.Type
import kotlin.concurrent.thread

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebsocketTest(
    @Autowired var stompClient: WebSocketStompClient
) {
    @LocalServerPort
    var port: Int = 8080


    fun subscription(block: (headers: StompHeaders, payload: Any) -> Unit): StompSessionHandlerAdapter {
        return object : StompSessionHandlerAdapter() {
            override fun handleFrame(headers: StompHeaders, payload: Any) {
                block.invoke(headers, payload)
            }
        }
    }

    fun subscription(type: Type, block: (headers: StompHeaders, payload: Any) -> Unit): StompSessionHandlerAdapter {
        return object : StompSessionHandlerAdapter() {
            override fun getPayloadType(headers: StompHeaders) = type
            override fun handleFrame(headers: StompHeaders, payload: Any) {
                block.invoke(headers, payload)
            }
        }
    }

    fun sessionHandler() =
        object : StompSessionHandlerAdapter() {
            override fun handleTransportError(session: StompSession, exception: Throwable) {
                if (exception !is ConnectionLostException) {
                    println("handleTransportError")
                    exception.printStackTrace()
                }
            }

            override fun handleException(
                session: StompSession,
                command: StompCommand?,
                headers: StompHeaders,
                payload: ByteArray,
                exception: Throwable
            ) {
                println("handleException")
                exception.printStackTrace()
            }

            override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                println("CONNECTED!")
                super.afterConnected(session, connectedHeaders)
            }
        }


    @Test
    fun connection() {
        val url = "ws://localhost:$port/api/ws"
        stompClient.connectAsync(url, sessionHandler()).get().apply {
            subscribe("/topic/hello", subscription { headers, payload ->
                println(headers)
                println("payload: $payload")
            })

            while (true) {
                send("/hello", "123")
                Thread.sleep(1000)
            }
        }
    }
}