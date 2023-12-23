package idk.bluecross.messenger.util

import idk.bluecross.messenger.store.entity.IdRef
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

class IdRefCodec : Codec<IdRef<*>> {
    override fun encode(writer: BsonWriter, value: IdRef<*>, encoderContext: EncoderContext) {
        writer.writeStartDocument()
        writer.writeString("\$ref",value.collectionName)
        writer.writeString("\$id",value.id.toHexString())
        if (value.databaseName != null) {
            writer.writeString("\$db", value.databaseName)
        }
        writer.writeEndDocument()

    }

    override fun getEncoderClass(): Class<IdRef<*>> {
        return IdRef::class.java
    }

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): IdRef<*> {
        throw UnsupportedOperationException("IdRefCodec does not support decoding")
    }

}