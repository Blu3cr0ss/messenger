package idk.bluecross.messenger.store.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import idk.bluecross.messenger.store.entity.content.ContentTree
import idk.bluecross.messenger.util.annotation.CascadeSave
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import java.time.Instant

class Message @JsonCreator constructor(
    @DBRef
    @CascadeSave
    @JsonProperty("sender") var sender: IdRef<User>,
    @JsonProperty("reactions") var reactions: List<Reaction>,
    @JsonProperty("contentTree") var contentTree: ContentTree,
    @JsonProperty("state") var state: State,
    @JsonProperty("timestamp") var timestamp: Instant = Instant.now(),
) {

    enum class State {
        SENDING,
        SENT,
        READ
    }
}