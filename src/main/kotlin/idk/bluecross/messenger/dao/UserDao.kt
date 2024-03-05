package idk.bluecross.messenger.dao

import com.mongodb.DBRef
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.result.UpdateResult
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
    val mongoTemplate: MongoTemplate,
    val fileDao: FileDao,
    val redisUserDetailsDao: RedisUserDetailsDao
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
    )[0]


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
        "avatar",
        User::class.java,
        IdRef::class.java
    )[0] as IdRef<FileInDb>).get()!!.byteArr

    fun setUsername(userId: Any, username: String) = changeUserDetails(userId) {
        mongoTemplate.updateFirst(
            Query(Criteria("_id").`is`(userId)),
            Update().set("userDetails.username", username),
            User::class.java
        )
    }


    fun setDisplayedName(userId: Any, displayedName: String) = changeUserDetails(userId) {
        mongoTemplate.updateFirst(
            Query(Criteria("_id").`is`(userId)),
            Update().set("userDetails.displayedName", displayedName),
            User::class.java
        )
    }

    fun setBio(userId: Any, bio: String) = changeUserDetails(userId) {
        mongoTemplate.updateFirst(
            Query(Criteria("_id").`is`(userId)),
            Update().set("userDetails.bio", bio),
            User::class.java
        )
    }

    fun setAvatar(userId: Any, avatar: ByteArray) = changeUserDetails(userId) {
        val avatar = fileDao.save(FileInDb((userId as ObjectId).toHexString() + "_avatar", avatar))
        mongoTemplate.updateFirst(
            Query(Criteria("_id").`is`(userId)),
            Update().set("avatar", avatar),
            User::class.java
        )
    }


    private fun <T> changeUserDetails(userId: Any, task: () -> T): T {
        return task.invoke().also {
            redisUserDetailsDao.set(
                mongoTemplate.findDistinct(
                    Query(Criteria("_id").`is`(userId)),
                    "userDetails",
                    User::class.java,
                    UserDetails::class.java
                )[0]
            )
        }
    }

    override fun <S : User?> save(entity: S): S = save(entity).also { changeUserDetails(entity!!.id) {} }

}