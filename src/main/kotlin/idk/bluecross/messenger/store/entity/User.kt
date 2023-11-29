package idk.bluecross.messenger.store.entity

import idk.bluecross.messenger.util.annotation.CascadeSave
import idk.bluecross.messenger.store.entity.content.GraphicContent
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    var bio: String,
    @DBRef
    @CascadeSave
    private var avatarFile: FileInDb,
    var status: Status,
    @DBRef
    @CascadeSave
    var chats: ArrayList<Chat>,
    var userDetails: UserDetails
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

    companion object {
        val DEFAULT_AVATAR_FILE =
            FileInDb("defaultAvatar", byteArrayOf(1)).apply { id = ObjectId("6566fa53ee30b040e65e1a3d") }
    }
}