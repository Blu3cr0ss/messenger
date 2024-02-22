package idk.bluecross.messenger.api

import idk.bluecross.messenger.authenticate
import idk.bluecross.messenger.defaultSessionHandler
import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.store.dto.MessageDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.messaging.WebSocketStompClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(locations = ["classpath:test-db.properties"])
@AutoConfigureMockMvc
class MessageControllerTest(
    @Autowired var stompClient: WebSocketStompClient,
    @Autowired var userService: UserService,
    @Autowired var messageController: MessageController,
) {
    @LocalServerPort
    var port: Int = 8080

    @Test
    fun sendMessage() {
        val chatId = "659d2754105ae36c2f4f4566"
        val url = "ws://localhost:$port/api/ws"
        stompClient.connectAsync(
            url,
            WebSocketHttpHeaders().apply { setBearerAuth(authenticate(port)) },
            defaultSessionHandler(),
        ).get().apply {
            send(
                "/chat/$chatId/messages/send",
                MessageDto(
                    "Привет Вася",
                    null
                )
            )
            Thread.sleep(1000)
        }
    }
}