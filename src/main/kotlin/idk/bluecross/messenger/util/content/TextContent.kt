package idk.bluecross.messenger.util.content

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeCreator

class TextContent(var text: String) : Content() {
    override var type = Type.TEXT
}