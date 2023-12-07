package idk.bluecross.messenger

import org.springframework.messaging.simp.stomp.*
import java.lang.reflect.Type

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