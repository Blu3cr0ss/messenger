package idk.bluecross.messenger.service

import idk.bluecross.messenger.store.dao.ChatDao
import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.Message
import idk.bluecross.messenger.store.entity.User
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
    fun saveWithUserIds(name: String, description: String, ids: List<ObjectId>): Chat {
        if (ids.isEmpty()) throw Exception("Пустой список пользователей")
        val users = ids.map { id ->
            userService.find(id)
                .run {
                    if (this.isEmpty) throw Exception("Пользователь $id не найден")
                    else this.get()
                }
        }

        return save(
            Chat(
                arrayListOf(),
                name,
                description,
                users as ArrayList<User>
            )
        )
    }

    fun saveMessage(chatId: ObjectId, message: Message) = chatDao.saveMessage(chatId, message)

}