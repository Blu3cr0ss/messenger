package idk.bluecross.messenger.store.dto

import org.bson.types.ObjectId

data class CreateChatDto(
    val name: String,
    val description: String,
    val users: List<ObjectId>
)