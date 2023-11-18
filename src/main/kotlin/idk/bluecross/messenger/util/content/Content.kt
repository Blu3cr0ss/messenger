package idk.bluecross.messenger.util.content

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