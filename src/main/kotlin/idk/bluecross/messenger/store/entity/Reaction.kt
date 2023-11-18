package idk.bluecross.messenger.store.entity

import idk.bluecross.messenger.util.annotation.CascadeSave
import idk.bluecross.messenger.util.content.EmojiContent
import idk.bluecross.messenger.util.db.FluxDocRef
import idk.bluecross.messenger.util.db.MonoDocRef
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Reaction {
    @Id
    var id = ObjectId()

    @CascadeSave
    lateinit var reacted: FluxDocRef<User>
    lateinit var content: EmojiContent
}