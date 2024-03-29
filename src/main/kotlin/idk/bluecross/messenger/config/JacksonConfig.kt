package idk.bluecross.messenger.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import idk.bluecross.messenger.util.ObjectIdDeserializer
import idk.bluecross.messenger.util.ObjectIdSerializer
import idk.bluecross.messenger.util.conversions.JacksonContentModule
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.messaging.converter.CompositeMessageConverter
import org.springframework.messaging.converter.MappingJackson2MessageConverter


@Configuration
class JacksonConfig {
    @Bean
    @Primary
    fun objectMapper() =
        ObjectMapper().registerKotlinModule().findAndRegisterModules()
            .registerModules(
                JacksonContentModule(),
                SimpleModule()
                    .addSerializer(ObjectIdSerializer())
                    .addDeserializer(ObjectId::class.java, ObjectIdDeserializer())
            ).setSerializationInclusion(JsonInclude.Include.NON_NULL)


    @Bean
    fun defaultMessageConverter(messageConverter: CompositeMessageConverter) =
        messageConverter.converters.removeIf {
            it is MappingJackson2MessageConverter && it.objectMapper.registeredModuleIds.isEmpty()
        }


}