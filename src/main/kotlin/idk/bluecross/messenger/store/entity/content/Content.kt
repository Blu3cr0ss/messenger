package idk.bluecross.messenger.store.entity.content

import com.fasterxml.jackson.annotation.JsonSubTypes

@JsonSubTypes()
abstract class Content {
    enum class Type {
        FILE,
        GRAPHIC,
        VIDEO,
        SOUND,
        EMOJI,
        TEXT
    }

    abstract var type: Type
    abstract fun getContent(): Any
}