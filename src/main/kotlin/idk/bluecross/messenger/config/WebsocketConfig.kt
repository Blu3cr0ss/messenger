package idk.bluecross.messenger.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import idk.bluecross.messenger.util.ObjectIdSerializer
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.messaging.converter.CompositeMessageConverter
import org.springframework.messaging.converter.GsonMessageConverter
import org.springframework.messaging.converter.JsonbMessageConverter
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.converter.SimpleMessageConverter
import org.springframework.messaging.converter.StringMessageConverter
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.beans.Beans


@Configuration
@EnableWebSocketMessageBroker
class WebsocketConfig : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/api/ws")
    }

    override fun configureMessageConverters(messageConverters: MutableList<MessageConverter>): Boolean {
        messageConverters.add(StringMessageConverter())
        messageConverters.add(MappingJackson2MessageConverter())
        return super.configureMessageConverters(messageConverters)
    }

    @Bean
    fun websocketClient(messageConverter: MessageConverter) =
        WebSocketStompClient(StandardWebSocketClient())
            .apply {
                this.messageConverter = messageConverter
            }

}


