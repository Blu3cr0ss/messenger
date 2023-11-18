package idk.bluecross.messenger.util.content

class EmojiContent(var utf8: String) : Content() {
    override var type = Type.EMOJI
}