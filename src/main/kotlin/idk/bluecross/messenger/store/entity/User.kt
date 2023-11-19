package idk.bluecross.messenger.store.entity

import idk.bluecross.messenger.util.annotation.CascadeSave
import idk.bluecross.messenger.util.content.GraphicContent
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    var userName: String,
    var displayedName: String,
    var bio: String,
    @DBRef
    @CascadeSave
    private var avatarFile: FileInDb,
    var status: Status,
    @DBRef
    @CascadeSave
    var chats: ArrayList<Chat>,
    var email: String,
    var password: String
) {
    @Id
    var id = ObjectId()

    @Transient
    var avatar = GraphicContent(avatarFile)

    enum class Status {
        ONLINE,
        OFFLINE,
        DND
    }
}