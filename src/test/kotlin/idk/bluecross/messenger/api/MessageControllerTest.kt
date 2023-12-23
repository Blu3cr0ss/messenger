package idk.bluecross.messenger.api

import idk.bluecross.messenger.authenticate
import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.defaultSessionHandler
import idk.bluecross.messenger.store.dto.MessageDto
import idk.bluecross.messenger.store.entity.*
import idk.bluecross.messenger.store.entity.content.ContentTree
import idk.bluecross.messenger.store.entity.content.FileContent
import idk.bluecross.messenger.store.entity.content.TextContent
import idk.bluecross.messenger.subscription
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:test-db.properties"])
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
        val url = "ws://localhost:$port/api/ws"
        stompClient.connectAsync(
            url,
            WebSocketHttpHeaders().apply { setBearerAuth(authenticate(port)) },
            defaultSessionHandler(),
        ).get().apply {
            subscribe(
                "/topic/chat/6574ac1a5dd5341a52414029/messages",
                subscription<Message>(Message::class.java) { h, p ->
                    println("message: $p")
                })

            send(
                "/chat/6574ac1a5dd5341a52414029/messages/send",
                MessageDto(
                    ContentTree(
                        TextContent(UUID.randomUUID().toString()),
                        FileContent(IdRef<FileInDb>(ObjectId("6566fa53ee30b040e65e1a3d"), FileInDb::class.java).get()!!)
                    ),
                )
            )
            Thread.sleep(1000)
        }
    }
}