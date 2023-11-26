package idk.bluecross.messenger.store.entity.content

class EmojiContent(var utf8: String) : Content() {
    override var type = Type.EMOJI
}