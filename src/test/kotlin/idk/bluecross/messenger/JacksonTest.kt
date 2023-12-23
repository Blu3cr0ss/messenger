package idk.bluecross.messenger

import com.fasterxml.jackson.databind.ObjectMapper
import idk.bluecross.messenger.store.entity.content.TextContent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.converter.CompositeMessageConverter
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import kotlin.reflect.full.isSubclassOf

@SpringBootTest
class JacksonTest {
    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var converter: MessageConverter

    @Test
    fun entityToMessage() {
        println(mapper.writeValueAsString(TextContent("some text")))
    }

    @Test
    fun messageConverter() {
        val converter = converter as CompositeMessageConverter
        converter.converters.filter { it::class.isSubclassOf(MappingJackson2MessageConverter::class) }
            .map { it as MappingJackson2MessageConverter }
            .forEach {
                println("objectMapper: ${it.objectMapper} modules: ${it.objectMapper.registeredModuleIds}")
            }
    }
}