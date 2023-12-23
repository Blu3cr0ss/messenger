package idk.bluecross.messenger.store.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

interface DBEntity {
    var id: ObjectId
}