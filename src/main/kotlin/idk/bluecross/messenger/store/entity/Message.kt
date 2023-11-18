package idk.bluecross.messenger.store.entity

import idk.bluecross.messenger.util.annotation.CascadeSave
import idk.bluecross.messenger.util.content.ContentTree
import idk.bluecross.messenger.util.db.FluxDocRef
import idk.bluecross.messenger.util.db.MonoDocRef
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.Instant

@Document
class Message(
    var sender: MonoDocRef<User>,
    @DocumentReference
    @CascadeSave
    var reactions: FluxDocRef<Reaction>,
    var contentTree: ContentTree,
    var state: Message.State
) {
    @Id
    var id = ObjectId()
    var timestamp = Instant.now()


    enum class State {
        SENDING,
        SENT,
        READ
    }
}