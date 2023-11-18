package idk.bluecross.messenger.store.entity

import org.bson.types.ObjectId
import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
//import org.springframework.data.relational.core.mapping.Column
//import org.springframework.data.relational.core.mapping.Table
import java.io.File

@Document
class FileInDb(var fileName: String, var byteArr: ByteArray) {
    @Id
    var id = ObjectId()
}