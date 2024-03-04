package idk.bluecross.messenger.util

import com.mongodb.DBRef
import idk.bluecross.messenger.store.entity.IdRef
import idk.bluecross.messenger.store.entity.Message
import idk.bluecross.messenger.store.entity.Reaction
import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.store.entity.content.ContentTree
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import java.time.Instant
import java.util.Date

@WritingConverter
class DocumentMessageConverter : Converter<Document, Message> {
    override fun convert(source: Document): Message? {
        return with(source) {
            return@with Message(
                IdRef.fromDBRef(get("sender", DBRef::class.java)) as IdRef<User>,
                getList("reactions", Reaction::class.java),
                getString("text"),
                get("attachments", ContentTree::class.java),
                Message.State.valueOf(getString("state")),
                get("timestamp", Date::class.java).toInstant()
            )
        }
    }
}