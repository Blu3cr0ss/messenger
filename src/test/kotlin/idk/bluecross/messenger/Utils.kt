package idk.bluecross.messenger

import idk.bluecross.messenger.util.spring.Beans
import org.springframework.lang.Nullable
import org.springframework.messaging.simp.stomp.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.lang.reflect.Type

fun <T : Any> subscription(type: Type, block: (headers: StompHeaders, payload: T?) -> Unit): StompSessionHandlerAdapter {
    return object : StompSessionHandlerAdapter() {
        override fun getPayloadType(headers: StompHeaders) = type
        override fun handleFrame(headers: StompHeaders, @Nullable payload: Any?) {
            block.invoke(headers, payload as? T?)
        }
    }
}

fun defaultSessionHandler() =
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
            println("CONNECTED to session ${session.sessionId}")
            super.afterConnected(session, connectedHeaders)
        }
    }

fun authenticate(
    port: Int
): String = Beans.getBean(MockMvc::class.java).post(
    "http://localhost:$port/api/auth/login?username=bluecross&password=QQQqqq999",
).andReturn().response.run {
    if (status !in 200..299) throw RuntimeException("Bad response status")
    else this.contentAsString
}
