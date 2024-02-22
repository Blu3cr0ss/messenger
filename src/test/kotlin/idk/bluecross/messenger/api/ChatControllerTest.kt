package idk.bluecross.messenger.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:test-db.properties"])
class ChatControllerTest(
    @Autowired var controller: ChatController
) {
//    @Test
//    fun fromChat() {
//        Assertions.assertTrue(
//            controller.createNewChat(
//                CreateChatDto("chatik", "some desc", listOf(ObjectId("6566fa53ee30b040e65e1a3e")))
//            ) is Chat,
//            "Ответ не Chat"
//        )
//    }

}