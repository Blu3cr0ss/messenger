package idk.bluecross.messenger.store.dao

import idk.bluecross.messenger.store.entity.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class UserDaoTest(@Autowired val userDao: UserDao, @Autowired val mongoTemplate: MongoTemplate) {
    @Test
    fun getChatsTest() {
        val q = userDao.getChats("65670b1236c0837b26a3cbdd")
        println(q)
    }

    @Test
    fun qwe() {
        println(mongoTemplate.findAll(User::class.java)[0].chats[0].get())
    }
}