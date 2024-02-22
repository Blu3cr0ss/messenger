package idk.bluecross.messenger.dao

import com.mongodb.DBRef
import com.mongodb.client.model.Aggregates
import idk.bluecross.messenger.repository.ChatRepository
import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.IdRef
import idk.bluecross.messenger.store.entity.User
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
class ChatDao(
    val chatRepository: ChatRepository,
    val mongoTemplate: MongoTemplate,
) : ChatRepository by chatRepository {
    val LOGGER = getLogger()

    fun removeUserFromMembers(userId: Any, chatId: Any) = mongoTemplate.updateFirst(
        Query(Criteria("_id").`is`(chatId)).apply { fields().include("members") },
        Update().pull("members", IdRef(userId as ObjectId, User::class.java)),
        Chat::class.java
    ).apply {
        if (LOGGER.isDebugEnabled) {
            LOGGER.debug("removeUserFromMembers(): Removed user with id $userId from members. Result: $this")
        }
    }

    fun addUserToMembers(userId: Any, chatId: Any) = mongoTemplate.updateFirst(
        Query(Criteria("_id").`is`(chatId)).apply { fields().include("members") },
        Update().addToSet("members", DBRef("user", userId)),
        Chat::class.java
    ).apply {
        if (LOGGER.isDebugEnabled) {
            LOGGER.debug("addUserToMembers(): Added user with id $userId to members. Result: $this")
        }
    }


    fun isEmptyMembers(chatId: Any) = mongoTemplate.execute("chat") {
        it.aggregate(
            listOf(
                Aggregates.match(Document("_id", chatId)),
                Aggregates.project(
                    Document(
                        mapOf(
                            "count" to Document("\$size", "\$members"),
                            "_id" to 0
                        )
                    )
                )
            )
        ).first().apply {
            if (LOGGER.isDebugEnabled) {
                LOGGER.debug("isEmptyMembers(): Aggregation result for chat $chatId. Result: $this")
            }
        }?.getInteger("count") == 0
    }

    fun getMembers(chatId: Any) = mongoTemplate.findDistinct(
        Query(Criteria("_id").`is`(chatId)),
        "members",
        Chat::class.java,
        User::class.java
    )
}
