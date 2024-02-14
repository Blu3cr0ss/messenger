package idk.bluecross.messenger.service

import idk.bluecross.messenger.store.dao.ChatDao
import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.Message
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.stereotype.Service

@Service
class ChatService(
    val chatDao: ChatDao,
    val mongoOperations: MongoOperations,
    val userService: UserService
) {
    fun save(chat: Chat) = chatDao.save(chat)
    fun find(id: ObjectId) = chatDao.findById(id)
    fun saveWithUserIds(name: String, description: String, ids: List<ObjectId>) =
        chatDao.saveChat(name, description, ids)

    fun saveMessage(chatId: ObjectId, message: Message) {
        chatDao.saveMessage(chatId, message)
    }

}