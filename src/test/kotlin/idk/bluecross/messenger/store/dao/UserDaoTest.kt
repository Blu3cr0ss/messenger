package idk.bluecross.messenger.store.dao

import idk.bluecross.messenger.dao.UserDao
import idk.bluecross.messenger.store.entity.FileInDb
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.MongoTemplate
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@SpringBootTest
class UserDaoTest(@Autowired val userDao: UserDao, @Autowired val mongoTemplate: MongoTemplate) {
    @Test
    fun getChatsTest() {
        val q = userDao.getChats("65670b1236c0837b26a3cbdd")
        println(q)
    }

    @Test
    fun isUserInChat() {
        println(userDao.isUserInChat(ObjectId("65670b1236c0837b26a3cbdd"), ObjectId("659d2754105ae36c2f4f4566")))
    }

    @Test
    fun qwe() {
        mongoTemplate.save(FileInDb("defaultAvatar", run {
            val os = ByteArrayOutputStream()
            ImageIO.write(ImageIO.read(ClassPathResource("defaultAvatar.png").inputStream), "png", os)
            return@run os.toByteArray()
        }).apply { id = ObjectId("6566fa53ee30b040e65e1a3d") })
    }
}