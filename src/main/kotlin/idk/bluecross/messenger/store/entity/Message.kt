package idk.bluecross.messenger.store.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import idk.bluecross.messenger.store.entity.content.ContentTree
import idk.bluecross.messenger.util.annotation.CascadeSave
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

class Message @JsonCreator constructor(
    @DBRef
    @CascadeSave
    @JsonProperty("sender") var sender: IdRef<User>,
    @JsonProperty("reactions") var reactions: List<Reaction>,
    @JsonProperty("text") var text: String,
    @org.springframework.lang.Nullable
    var attachments: ContentTree?,
    @JsonProperty("state") var state: State,
    @JsonProperty("timestamp") var timestamp: Instant = Instant.now(),
) {

    constructor(
        // OLD TYPE CONTENT
        sender: IdRef<User>,
        reactions: List<Reaction>,
        contentTree: ContentTree,
        state: State,
        timestamp: Instant = Instant.now(),
    ) : this(
        sender,
        reactions,
        contentTree.first.getContent().toString(),
        if (contentTree.size > 1) contentTree.subList(1, contentTree.size) as ContentTree else null,
        state,
        timestamp
    )

    enum class State {
        SENDING,
        SENT,
        READ
    }
}