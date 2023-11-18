package idk.bluecross.messenger.store.entity

import idk.bluecross.messenger.util.annotation.CascadeSave
import idk.bluecross.messenger.util.content.GraphicContent
import idk.bluecross.messenger.util.db.FluxDocRef
import idk.bluecross.messenger.util.db.MonoDocRef
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    var userName: String,
    var displayedName: String,
    var bio: String,
    @CascadeSave var avatar: MonoDocRef<GraphicContent>,
    var status: Status,
    @CascadeSave
    var chats: FluxDocRef<Chat>,
    var email: String,
    var password: String
) {
    @Id
    var id = ObjectId()


    enum class Status {
        ONLINE,
        OFFLINE,
        DND
    }
}