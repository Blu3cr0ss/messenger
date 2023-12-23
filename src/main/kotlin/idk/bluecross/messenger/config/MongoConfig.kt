package idk.bluecross.messenger.config

import com.mongodb.MongoClientSettings
import idk.bluecross.messenger.util.IdRefCodec
import org.bson.UuidRepresentation
import org.bson.codecs.configuration.CodecRegistries
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration


@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {
    @Value("\${spring.data.mongodb.database}")
    lateinit var database: String
    override fun getDatabaseName(): String = database
    override fun mongoClientSettings(): MongoClientSettings {
        val builder = MongoClientSettings.builder()
        with(builder) {
            uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
            codecRegistry(
                CodecRegistries.fromRegistries(
                    CodecRegistries.fromCodecs(IdRefCodec()),
                    MongoClientSettings.getDefaultCodecRegistry()
                )
            )
        }
        configureClientSettings(builder)
        return builder.build()
    }
}