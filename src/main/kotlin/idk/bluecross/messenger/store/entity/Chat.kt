package idk.bluecross.messenger.store.entity

import idk.bluecross.messenger.util.annotation.CascadeSave
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Chat(
    @DBRef
    var messages: ArrayList<Message>,
    var name: String,
    var description: String,
    @DBRef
    var members: ArrayList<User>
) {
    @Id
    var id = ObjectId()
}