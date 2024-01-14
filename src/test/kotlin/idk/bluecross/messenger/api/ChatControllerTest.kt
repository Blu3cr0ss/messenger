package idk.bluecross.messenger.api

import idk.bluecross.messenger.store.dto.CreateChatDto
import idk.bluecross.messenger.store.entity.Chat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:test-db.properties"])
class ChatControllerTest(
    @Autowired var controller: ChatController
) {
    @Test
    fun fromChat() {
        Assertions.assertTrue(
            controller.createNewChat(
                CreateChatDto("chatik", "some desc", listOf(ObjectId("6566fa53ee30b040e65e1a3e")))
            ) is Chat,
            "Ответ не Chat"
        )
    }

    @Test
    fun fromChatError() {
        runCatching {
            controller.createNewChat(CreateChatDto("chatik", "", listOf(ObjectId("6567123236c0837b26a3cbdd"))))
        }.getOrNull()!!.also {
            Assertions.assertTrue(
                (it as? ResponseEntity<*>)!!.body.equals("Пользователь 6567123236c0837b26a3cbdd не найден"),
                "ResponseEntity body не правильное. Объект: $it" +
                        "Ожидаемый body: Пользователь 6567123236c0837b26a3cbdd не найден"
            )
        }
    }
}