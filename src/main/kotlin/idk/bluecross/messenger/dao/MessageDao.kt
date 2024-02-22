package idk.bluecross.messenger.dao

import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.Message
import idk.bluecross.messenger.util.getLogger
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class MessageDao(
    val mongoTemplate: MongoTemplate
) {
    var LOGGER = getLogger()
    fun save(chatId: ObjectId, message: Message) = mongoTemplate.updateFirst(
        Query(Criteria("_id").`is`(chatId)),
        Update().addToSet("messages", message),
        Chat::class.java
    ).apply {
        if (LOGGER.isDebugEnabled) LOGGER.debug("save(): saved message with text ${message.text}. Result: $this")
    }

}