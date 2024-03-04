package idk.bluecross.messenger.util

import idk.bluecross.messenger.store.entity.IdRef
import idk.bluecross.messenger.util.clazz.getClassFromCollectionName
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.types.ObjectId

class IdRefCodec : Codec<IdRef<*>> {
    val LOGGER = getLogger()
    override fun encode(
        writer: BsonWriter,
        value: IdRef<*>,
        encoderContext: EncoderContext?
    ) {
        writer.writeStartDocument()
        writer.writeString("\$ref", value.collection)
        writer.writeObjectId("\$id", value.id)
        if (value.databaseName != null) {
            writer.writeString("\$db", value.databaseName)
        }
        writer.writeEndDocument()
    }

    override fun getEncoderClass(): Class<IdRef<*>> {
        return IdRef::class.java
    }

    override fun decode(
        reader: BsonReader,
        decoderContext: DecoderContext
    ): IdRef<*> {
        if (LOGGER.isWarnEnabled) LOGGER.warn("IdRefCodec decode method shouldn't be used")
        reader.readStartDocument()
        val collection = reader.readString()
        val id = ObjectId(reader.readString())
        reader.readEndDocument()
        val clazz = getClassFromCollectionName(collection)
            ?: throw RuntimeException("IdRefCodec cant find class for collection $collection")
        return IdRef(id, clazz)
    }

}