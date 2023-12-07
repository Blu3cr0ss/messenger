package idk.bluecross.messenger.api

import idk.bluecross.messenger.sessionHandler
import idk.bluecross.messenger.subscription
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.RequestMatcher
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.socket.messaging.WebSocketStompClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RestAndWsControllerTest(
    @Autowired var mock: MockMvc,
    @Autowired var stompClient: WebSocketStompClient
) {

    @LocalServerPort
    var port: Int = 8080

    @Test
    fun `rest test`() {
        mock.perform(get("/api/rest"))
            .andExpect(content().string("rest"))
    }

    @Test
    fun `ws test`() {
        val url = "ws://localhost:$port/api/ws"
        stompClient.connectAsync(url, sessionHandler()).get().apply {
            subscribe("/topic/ws", subscription { _, payload ->
                println("payload: $payload")
            })

            send("/ws", "123")
            Thread.sleep(100)
        }
    }
}