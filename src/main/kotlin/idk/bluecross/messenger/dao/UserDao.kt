package idk.bluecross.messenger.dao

import com.mongodb.DBRef
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import idk.bluecross.messenger.repository.UserRepository
import idk.bluecross.messenger.store.entity.*
import idk.bluecross.messenger.util.getLogger
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserDao(
    val userRepository: UserRepository,
    val mongoTemplate: MongoTemplate
) : UserRepository by userRepository {
    val LOGGER = getLogger()

    fun isUserInChat(userId: Any, chatId: Any) = mongoTemplate.execute("chat") {
        it.aggregate(
            listOf(
                Aggregates.match(Document("_id", chatId)),
                Aggregates.project(Document(mapOf("members" to 1, "_id" to 0))),
                Aggregates.match(
                    Filters.`in`("members", IdRef(userId as ObjectId, User::class.java))
                ),
                Aggregates.count("members")
            )
        ).first().apply {
            if (LOGGER.isDebugEnabled) LOGGER.debug("isUserInChat(): Aggregation result: $this")
        }?.getInteger("members").run { this != null && this != 0 }
    }

    fun findUserDetailsByUsername(username: String) = mongoTemplate.findDistinct(
        Query(Criteria("userDetails.username").`is`(username)),
        "userDetails",
        User::class.java,
        UserDetails::class.java
    )


    fun getChats(id: Any) = mongoTemplate.findDistinct(
        Query(Criteria("_id").`is`(id)),
        "chats",
        User::class.java,
        Chat::class.java
    )

    fun getChatsByAny(fieldName: String, fieldValue: Any) = mongoTemplate.findDistinct(
        Query(Criteria(fieldName).`is`(fieldValue)),
        "chats",
        User::class.java,
        Chat::class.java
    )

    fun removeChatFromChats(userId: Any, chatId: Any) = mongoTemplate.updateFirst(
        Query(Criteria("_id").`is`(userId)).apply { fields().include("chats") },
        Update().pull("chats", DBRef("chat", chatId)),
        User::class.java
    ).apply {
        if (LOGGER.isDebugEnabled) {
            LOGGER.debug("removeChatFromChats(): Removed chat with id $chatId from chats. Result: $this")
        }
    }

    fun addChatToChats(userId: Any, chatId: Any) = mongoTemplate.updateFirst(
        Query(Criteria("_id").`is`(userId)).apply { fields().include("chats") },
        Update().addToSet("chats", DBRef("chat", chatId)),
        User::class.java
    ).apply {
        if (LOGGER.isDebugEnabled) {
            LOGGER.debug("addChatToChats(): Added chat with id $chatId to chats. Result: $this")
        }
    }

    fun getIdByUsername(username: String) = mongoTemplate.findDistinct(
        Query(Criteria("userDetails.username").`is`(username)),
        "_id",
        User::class.java,
        ObjectId::class.java
    ).getOrNull(0).apply {
        if (LOGGER.isDebugEnabled) {
            LOGGER.debug("getIdByUsername(): Id of $username is $this")
        }
    } ?: throw Exception("No user with username $username found")

    fun getAvatarByUsername(username: String) = (mongoTemplate.findDistinct(
        Query(Criteria("userDetails.username").`is`(username)),
        "avatarFile",
        User::class.java,
        IdRef::class.java
    )[0] as IdRef<FileInDb>).get()!!.byteArr
//    fun existsByUsername(username:String) = mongoTemplate.exists(
//        Query(Criteria("userDetails.username").`is`(username)),
//        User::class.java
//    )
}