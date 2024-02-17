package idk.bluecross.messenger.store.dto

import idk.bluecross.messenger.store.entity.content.ContentTree

data class MessageDto(
    var text: String,
    var attachments: ContentTree?,
)