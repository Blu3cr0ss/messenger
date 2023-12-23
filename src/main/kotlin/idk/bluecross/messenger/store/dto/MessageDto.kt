package idk.bluecross.messenger.store.dto

import com.fasterxml.jackson.annotation.JsonProperty
import idk.bluecross.messenger.store.entity.IdRef
import idk.bluecross.messenger.store.entity.Message
import idk.bluecross.messenger.store.entity.Reaction
import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.store.entity.content.ContentTree
import idk.bluecross.messenger.util.annotation.CascadeSave
import org.springframework.data.mongodb.core.mapping.DBRef
import java.time.Instant

data class MessageDto(
    var contentTree: ContentTree,
)