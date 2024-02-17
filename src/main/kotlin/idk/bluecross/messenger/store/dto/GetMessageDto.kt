package idk.bluecross.messenger.store.dto

import idk.bluecross.messenger.store.entity.content.ContentTree

data class GetMessageDto(
    val senderId: String,
    val senderDisplayedName: String,
    val text:String,
    val attachments: ContentTree?
)