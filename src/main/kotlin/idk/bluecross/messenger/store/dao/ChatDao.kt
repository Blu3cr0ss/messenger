package idk.bluecross.messenger.store.dao

import idk.bluecross.messenger.store.entity.*
import idk.bluecross.messenger.store.repository.ChatRepository
import idk.bluecross.messenger.store.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class ChatDao(
    val chatRepository: ChatRepository,
    val mongoTemplate: MongoTemplate,
    val userRepository: UserRepository
) {
    fun save(chat: Chat) = chatRepository.save(chat)
    fun findById(id: ObjectId) = chatRepository.findById(id)

    fun saveMessage(chatId: ObjectId, message: Message) {
        mongoTemplate.updateFirst(
            Query(Criteria("_id").`is`(chatId)),
            Update().addToSet("messages", message),
            Chat::class.java
        )
    }

    fun saveChat(name: String, description: String, userNames: List<String>) = Chat(
        listOf(),
        name,
        description,
        IdRefList<User>().apply {
            addAll(userNames.map {
                IdRef(
                    mongoTemplate.findDistinct(
                        Query(Criteria("userDetails.username").`is`(it)),
                        "_id",
                        User::class.java,
                        ObjectId::class.java
                    )[0],
                    User::class.java
                )
            })
        }
    ).also {
        mongoTemplate.save(it)
        mongoTemplate.updateMulti(
            Query(Criteria("userDetails.username").`in`(userNames)),
            Update().addToSet("chats", it),
            User::class.java
        )
    }
}
