package idk.bluecross.messenger.store.dao

import idk.bluecross.messenger.store.entity.content.Content

data class ContentDao(
    var type: Content.Type,
    var content: Any
)