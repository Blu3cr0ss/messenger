package idk.bluecross.messenger.store.entity

import idk.bluecross.messenger.store.entity.content.EmojiContent
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Reaction {
    @DBRef
    lateinit var reacted: IdRefList<User>
    lateinit var content: EmojiContent
}