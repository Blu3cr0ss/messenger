package idk.bluecross.messenger.store.dao

import com.mongodb.DBRef
import idk.bluecross.messenger.store.entity.*
import idk.bluecross.messenger.store.repository.ChatRepository
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoOperations
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
    val mongoOperations: MongoOperations
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

    fun saveChat(name: String, description: String, userIds: List<ObjectId>) = Chat(
        listOf(),
        name,
        description,
        IdRefList<User>().apply { addAll(userIds.map { IdRef(it, User::class.java) }) }
    ).also {
        mongoTemplate.save(it)
        mongoTemplate.updateMulti(
            Query(Criteria("_id").`in`(userIds)),
            Update().addToSet("chats", it),
            User::class.java
        )
    }
}
