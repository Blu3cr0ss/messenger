package idk.bluecross.messenger.util

import idk.bluecross.messenger.store.entity.IdRef
import idk.bluecross.messenger.util.clazz.getClassFromCollectionName
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

class IdRefCodec : Codec<IdRef<*>> {
    val LOGGER = getLogger()
    override fun encode(
        writer: BsonWriter,
        value: idk.bluecross.messenger.store.entity.IdRef<*>,
        encoderContext: EncoderContext?
    ) {
        writer.writeStartDocument()
        writer.writeString("\$ref", value.collection)
        writer.writeString("\$id", value.id.toHexString())
        if (value.databaseName != null) {
            writer.writeString("\$db", value.databaseName)
        }
        writer.writeEndDocument()
    }

    override fun getEncoderClass(): Class<idk.bluecross.messenger.store.entity.IdRef<*>> {
        return idk.bluecross.messenger.store.entity.IdRef::class.java
    }

    override fun decode(
        reader: BsonReader,
        decoderContext: DecoderContext
    ): IdRef<*> {
//        throw UnsupportedOperationException("IdRefCodec does not support decoding")
//        if (LOGGER.isWarnEnabled) LOGGER.warn("Poorly tested code in IdRefCodec.decode()")
        reader.readStartDocument()
        val collection = reader.readString()
        val id = reader.readObjectId()
        reader.readEndDocument()
        val clazz = getClassFromCollectionName(collection)
            ?: throw RuntimeException("IdRefCodec cant find class for collection $collection")
        return IdRef(id, clazz)
    }

}