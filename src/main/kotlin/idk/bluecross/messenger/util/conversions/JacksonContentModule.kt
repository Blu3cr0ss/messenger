package idk.bluecross.messenger.util.conversions

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import idk.bluecross.messenger.store.dao.ContentDao
import idk.bluecross.messenger.store.entity.FileInDb
import idk.bluecross.messenger.store.entity.content.*
import idk.bluecross.messenger.util.spring.Beans
import org.bson.types.ObjectId

class JacksonContentModule : SimpleModule() {
    companion object {
        val objectMapper by lazy { Beans.getBean(ObjectMapper::class.java) }
    }

    init {
        addSerializer(ContentSerializer())
        addDeserializer(Content::class.java, ContentDeserializer())
    }

    class ContentSerializer : StdSerializer<Content>(Content::class.java) {
        override fun serialize(value: Content, gen: JsonGenerator?, provider: SerializerProvider?) {
            gen?.writeObject(ContentDao(value.type, value.getContent()))
        }
    }

    class ContentDeserializer : StdDeserializer<Content>(Content::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Content =
            with(p.codec.readValue(p, ContentDao::class.java)) {
                fun getFileInDb(): FileInDb {
                    val content = content as HashMap<String, String>
                    return FileInDb(
                        content["fileName"]!!,
                        content["byteArr"]!!.encodeToByteArray(),
                        ObjectId(content["id"])
                    )
                }

                when (type) {
                    Content.Type.TEXT -> {
                        return TextContent(content.toString())
                    }

                    Content.Type.EMOJI -> {
                        return TextContent(content.toString())
                    }

                    Content.Type.FILE -> {
                        return FileContent(getFileInDb())
                    }

                    Content.Type.GRAPHIC -> {
                        return GraphicContent(getFileInDb())
                    }

                    Content.Type.VIDEO -> {
                        return VideoContent(getFileInDb())
                    }

                    Content.Type.SOUND -> {
                        return SoundContent(getFileInDb())
                    }

                }
            }
    }

}