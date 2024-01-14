package idk.bluecross.messenger.store.entity

import org.bson.types.ObjectId

interface DBEntity {
    var id: ObjectId
}