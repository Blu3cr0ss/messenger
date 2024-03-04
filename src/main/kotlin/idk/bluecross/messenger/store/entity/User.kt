package idk.bluecross.messenger.store.entity

import idk.bluecross.messenger.dao.FileDao
import idk.bluecross.messenger.repository.FileRepository
import idk.bluecross.messenger.util.annotation.CascadeSave
import idk.bluecross.messenger.util.spring.Beans
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    @DBRef
    @CascadeSave
    var avatar: FileInDb,
    var status: Status,
    @DBRef
    @CascadeSave
    var chats: IdRefList<Chat>,
    @CascadeSave
    var userDetails: UserDetails
) : DBEntity {
    @Id
    override var id = ObjectId()

    init {
        userDetails.id = id
    }

    enum class Status {
        ONLINE,
        OFFLINE,
        DND
    }

    companion object {
        val DEFAULT_AVATAR_FILE by lazy {
            Beans.getBean(FileDao::class.java).findById(ObjectId("6566fa53ee30b040e65e1a3d")).get()
        }
    }
}