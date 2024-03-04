package idk.bluecross.messenger.dao

import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Projections
import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.Message
import idk.bluecross.messenger.util.DocumentMessageConverter
import idk.bluecross.messenger.util.getLogger
import org.bson.BsonString
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationSpELExpression
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Field
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class MessageDao(
    val mongoTemplate: MongoTemplate,
) {
    var LOGGER = getLogger()
    fun save(chatId: ObjectId, message: Message) = mongoTemplate.updateFirst(
        Query(Criteria("_id").`is`(chatId)),
        Update().addToSet("messages", message),
        Chat::class.java
    ).apply {
        if (LOGGER.isDebugEnabled) LOGGER.debug("save(): saved message with text ${message.text}. Result: $this")
    }

    val documentMessageConverter = DocumentMessageConverter()
    fun getLastMessages(chatId: Any, count: Int) = (
            mongoTemplate.aggregate(
                Aggregation.newAggregation(
                    Chat::class.java,
                    Aggregation.match(Criteria("_id").`is`(chatId)),
                    Aggregation.project("messages"),
                    Aggregation.addFields()
                        .addFieldWithValue("last", Accumulators.lastN("last", BsonString("\$messages"), count)).build(),
                    Aggregation.project("last")
                ),
                Document::class.java
            ).mappedResults[0].get("last", Document::class.java)
                .getValue("value") as List<Document>)
        .mapNotNull { document ->
            documentMessageConverter.convert(document)
        }


    fun getMessages(chatId: Any) = mongoTemplate.findDistinct(
        Query(Criteria("_id").`is`(chatId)),
        "messages",
        Chat::class.java,
        Document::class.java
    ).mapNotNull { document ->
        documentMessageConverter.convert(document)
    }

    fun getMessagesInRange(chatId: Any, start: Int, end: Int) =
        mongoTemplate.aggregate<Chat, Document>(
            Aggregation.newAggregation(
                Aggregation.match(Criteria("_id").`is`(chatId)),
                AggregationOperation { context ->
                    context.getMappedObject(
                        Document.parse(
                            "{\$project: {" +
                                    "      messages: {" +
                                    "        \$slice: [" +
                                    "          \"\$messages\"," +
                                    "          $start," +
                                    "          ${end - start+1}" +
                                    "        ]" +
                                    "      }" +
                                    "    }}"
                        )
                    )
                },
            )
        ).mappedResults[0].getList("messages", Document::class.java).mapNotNull { document ->
            documentMessageConverter.convert(document)
        }

}