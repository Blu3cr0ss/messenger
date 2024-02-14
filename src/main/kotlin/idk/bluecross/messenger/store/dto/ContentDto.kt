package idk.bluecross.messenger.store.dto

import idk.bluecross.messenger.store.entity.content.Content

data class ContentDto(
    var type: Content.Type,
    var content: Any
)