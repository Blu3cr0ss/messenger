package idk.bluecross.messenger.service

import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ChatServiceTest(
    @Autowired val chatService: ChatService
) {
    @Test
    fun leaveChat() {
        chatService.leaveChat(
            ObjectId("65cf2d5a1e2e9e11498608ae"),
            ObjectId("659d2754105ae36c2f4f4566")
        )
    }

    @Test
    fun addToChat() {
        chatService.addToChat(
            ObjectId("65670b1236c0837b26a3cbdd"),
            ObjectId("65cf2d5a1e2e9e11498608ae"),
            ObjectId("659d2754105ae36c2f4f4566")
        )
    }

    @Test
    fun getMembers() {
        println(chatService.getMembers(ObjectId("659d2754105ae36c2f4f4566")))
    }
}