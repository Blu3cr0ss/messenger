package idk.bluecross.messenger.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.bson.types.ObjectId

class ObjectIdSerializer : StdSerializer<ObjectId>(ObjectId::class.java) {
    override fun serialize(value: ObjectId?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.toHexString())
    }

}

class ObjectIdDeserializer : StdDeserializer<ObjectId>(ObjectId::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): ObjectId {
        return ObjectId()
    }
}