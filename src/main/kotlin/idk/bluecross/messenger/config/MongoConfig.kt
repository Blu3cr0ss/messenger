package idk.bluecross.messenger.config

import com.mongodb.MongoClientSettings
import idk.bluecross.messenger.util.DBRefCodec
import idk.bluecross.messenger.util.IdRefBsonConverter
import idk.bluecross.messenger.util.IdRefCodec
import idk.bluecross.messenger.util.IdRefDBRefResolver
import org.bson.UuidRepresentation
import org.bson.codecs.configuration.CodecRegistries
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext


@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {
    @Value("\${spring.data.mongodb.database}")
    lateinit var database: String
    override fun getDatabaseName(): String = database
    @Bean
    override fun mongoClientSettings(): MongoClientSettings {
        val builder = MongoClientSettings.builder()
        with(builder) {
            uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
            codecRegistry(
                CodecRegistries.fromRegistries(
                    CodecRegistries.fromCodecs(IdRefCodec(), DBRefCodec()),
                    MongoClientSettings.getDefaultCodecRegistry()
                )
            )
        }
        configureClientSettings(builder)
        return builder.build()
    }

    override fun configureConverters(converterConfigurationAdapter: MongoCustomConversions.MongoConverterConfigurationAdapter) {
        converterConfigurationAdapter.registerConverter(IdRefBsonConverter())
    }

    override fun mappingMongoConverter(
        databaseFactory: MongoDatabaseFactory,
        customConversions: MongoCustomConversions,
        mappingContext: MongoMappingContext,
    ): MappingMongoConverter {
        return MappingMongoConverter(IdRefDBRefResolver(DefaultDbRefResolver(databaseFactory)), mappingContext).apply {
            setCustomConversions(customConversions)
            setCodecRegistryProvider(databaseFactory)
        }
    }
}