package idk.bluecross.messenger.util

import com.mongodb.DBRef
import idk.bluecross.messenger.store.entity.IdRef
import idk.bluecross.messenger.util.clazz.getClassFromCollectionName
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.types.ObjectId

class DBRefCodec : Codec<DBRef> {
    override fun encode(writer: BsonWriter, value: DBRef, context: EncoderContext) {
        writer.writeStartDocument()
        writer.writeString("\$ref", value.collectionName)
        if (value.id is String) writer.writeObjectId("\$id", ObjectId(value.id as String))
        else if (value.id is ObjectId) writer.writeObjectId("\$id", value.id as ObjectId)
        else writer.writeObjectId("\$id", ObjectId(value.id.toString()))
        if (value.databaseName != null) {
            writer.writeString("\$db", value.databaseName)
        }
        writer.writeEndDocument()
    }

    override fun getEncoderClass() = DBRef::class.java

    override fun decode(
        reader: BsonReader,
        decoderContext: DecoderContext
    ): DBRef {
        throw UnsupportedOperationException("DBRefCodec does not support decoding")
    }

}