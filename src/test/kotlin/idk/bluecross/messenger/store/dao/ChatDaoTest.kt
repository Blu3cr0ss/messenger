package idk.bluecross.messenger.store.dao

import idk.bluecross.messenger.dao.ChatDao
import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.IdRef
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class ChatDaoTest(@Autowired val chatDao: ChatDao, @Autowired val mongoTemplate: MongoTemplate) {

    @Test
    fun addToChat() {
//        chatDao.addToChat(ObjectId("659d27729d70bc12d350cc23"), listOf(ObjectId("65670b1236c0837b26a3cbdd")))
    }

    @Test
    fun idRef() {
        val repeats = 10000000
        val before = System.currentTimeMillis()
        for (i in 1..repeats) {
            IdRef(ObjectId("65d06893a4f69646928e4c7d"), Chat::class.java)
        }
        val after = System.currentTimeMillis()
        val diff = after - before
        println("All: $diff")
        println("Per id: ${diff / repeats}")
        // 1749
    }

}