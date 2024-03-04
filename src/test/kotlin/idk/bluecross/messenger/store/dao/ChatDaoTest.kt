package idk.bluecross.messenger.store.dao

import idk.bluecross.messenger.dao.ChatDao
import idk.bluecross.messenger.dao.MessageDao
import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.IdRef
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask
import kotlin.jvm.optionals.getOrNull

@SpringBootTest
class ChatDaoTest(@Autowired val chatDao: ChatDao, @Autowired val messageDao: MessageDao) {

    @Test
    fun addToChat() {
//        chatDao.addToChat(ObjectId("659d27729d70bc12d350cc23"), listOf(ObjectId("65670b1236c0837b26a3cbdd")))
    }

    @Test
    fun getMembers() {
        chatDao.getMembers(ObjectId("65dadc4ce2dd2b452a34b009"))
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

    @Test
    fun lastN() {
        val t = System.currentTimeMillis()
        for (i in 0..200) println(messageDao.getLastMessages(ObjectId("65e4082f5301bb20e89d857b"), 3))
        println(System.currentTimeMillis() - t)
        println((System.currentTimeMillis() - t) / 200)
    }

    @Test
    fun newFullMessages() {
        val t = System.currentTimeMillis()
        for (i in 0..200) messageDao.getMessages(ObjectId("65e4082f5301bb20e89d857b"))
        println(System.currentTimeMillis() - t)
        println((System.currentTimeMillis() - t) / 200)
    }

    @Test
    fun inRange() {
        val t = System.currentTimeMillis()
        println(messageDao.getMessagesInRange(ObjectId("65e4082f5301bb20e89d857b"), 100, 200).map { it.text })
        println(System.currentTimeMillis() - t)
        println((System.currentTimeMillis() - t) / 200)
    }

    @Test
    fun fullMessages() {
        val t = System.currentTimeMillis()
        for (i in 0..200) chatDao.findById(ObjectId("65e4082f5301bb20e89d857b")).get().messages
        println(System.currentTimeMillis() - t)
        println((System.currentTimeMillis() - t) / 200)
    }

}
