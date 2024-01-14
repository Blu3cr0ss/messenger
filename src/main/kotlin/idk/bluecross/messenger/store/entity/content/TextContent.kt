package idk.bluecross.messenger.store.entity.content

class TextContent(var text: String) : Content() {
    override var type = Type.TEXT
    override fun getContent() = text
}