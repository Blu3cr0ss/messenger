package idk.bluecross.messenger.util

import idk.bluecross.messenger.store.entity.IdRef
import org.bson.BsonDocumentWriter
import org.bson.Document
import org.bson.conversions.Bson
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class IdRefBsonConverter : Converter<IdRef<*>, Bson> {
    override fun convert(source: IdRef<*>): Bson {
        val writer = BsonDocumentWriter(Document().toBsonDocument())
        writer.writeStartDocument()
        writer.writeString("\$ref", source.collection)
        writer.writeString("\$id", source.id.toHexString())
        if (source.databaseName != null) {
            writer.writeString("\$db", source.databaseName)
        }
        writer.writeEndDocument()

        return writer.document
    }
}