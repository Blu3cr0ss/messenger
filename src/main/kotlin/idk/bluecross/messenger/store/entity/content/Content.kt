package idk.bluecross.messenger.store.entity.content

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
}