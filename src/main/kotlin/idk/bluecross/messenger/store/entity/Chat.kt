package idk.bluecross.messenger.store.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Chat(
    var messages: ArrayList<Message>,
    var name: String,
    var description: String,
    @DBRef
    var members: IdRefList<User>
) : DBEntity {
    @Id
    override var id = ObjectId()
}