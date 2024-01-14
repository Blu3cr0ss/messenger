package idk.bluecross.messenger.store.dao

import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class ChatDaoTest(@Autowired val chatDao: ChatDao, @Autowired val mongoTemplate: MongoTemplate) {
    @Test
    fun saveChat() {
        chatDao.saveChat("qwe123", "qwe", listOf(ObjectId("65670b1236c0837b26a3cbdd")))
    }

}