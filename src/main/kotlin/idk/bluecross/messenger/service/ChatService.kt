package idk.bluecross.messenger.service

import idk.bluecross.messenger.dao.ChatDao
import idk.bluecross.messenger.dao.MessageDao
import idk.bluecross.messenger.dao.UserDao
import idk.bluecross.messenger.store.entity.*
import idk.bluecross.messenger.util.getLogger
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.stereotype.Service

@Service
class ChatService(
    val chatDao: ChatDao,
    val userDao: UserDao,
    val mongoOperations: MongoOperations,
    val userService: UserService,
    val messageDao: MessageDao
) {
    val LOGGER = getLogger()
    fun save(chat: Chat) = chatDao.save(chat)
    fun find(id: ObjectId) = chatDao.findById(id)
    fun saveWithUserNames(name: String, description: String, names: List<String>): Chat {
        val ids = names.filter(userDao::existsByUsername).map(userDao::getIdByUsername)
        val chat = Chat(
            listOf(),
            name,
            description,
            IdRefList(ids.map { IdRef(it, User::class.java) })
        )
        ids.forEach {
            userDao.addChatToChats(it, chat.id)
            chatDao.addUserToMembers(it, chat.id)
        }
        if (LOGGER.isDebugEnabled) {
            LOGGER.debug("saveWithUserNames(): Saved chat with names $names. Chat id is ${chat.id} and members are $ids")
        }
        return chatDao.save(chat)
    }

    fun saveMessage(chatId: ObjectId, message: Message) {
        messageDao.save(chatId, message)
    }

    fun leaveChatWithUsername(username: String, chatId: Any) = leaveChat(userService.getIdByUsername(username), chatId)

    fun addToChatWithUsername(whoAddingId: Any, username: String, chatId: Any) =
        addToChat(whoAddingId, userService.getIdByUsername(username), chatId)

    fun leaveChat(userId: Any, chatId: Any) {
        chatDao.removeUserFromMembers(userId, chatId)
        userDao.removeChatFromChats(userId, chatId)
        if (chatDao.isEmptyMembers(chatId)) chatDao.deleteById(chatId as ObjectId)
    }

    fun addToChat(whoAddingId: Any, userId: Any, chatId: Any) {
        if (userDao.isUserInChat(whoAddingId, chatId)) {
            chatDao.addUserToMembers(userId, chatId)
            userDao.addChatToChats(userId, chatId)
        }
    }

    fun getMembers(chatId: Any) = chatDao.getMembers(chatId)
    fun getLastNMessages(chatId: Any, n: Int) = messageDao.getLastMessages(chatId, n)
    fun getMessages(chatId: Any) = messageDao.getMessages(chatId)
    fun getMessagesInRange(chatId: Any, from: Int, to: Int) = messageDao.getMessagesInRange(chatId, from, to)

}