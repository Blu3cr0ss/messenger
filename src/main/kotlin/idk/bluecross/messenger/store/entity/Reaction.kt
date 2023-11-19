package idk.bluecross.messenger.store.entity

import idk.bluecross.messenger.util.annotation.CascadeSave
import idk.bluecross.messenger.util.content.EmojiContent
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Reaction {
    @Id
    var id = ObjectId()

    @DBRef
    lateinit var reacted: List<User>
    lateinit var content: EmojiContent
}