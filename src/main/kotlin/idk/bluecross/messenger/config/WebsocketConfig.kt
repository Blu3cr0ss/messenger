package idk.bluecross.messenger.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.converter.StringMessageConverter
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.messaging.WebSocketStompClient


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
        return super.configureMessageConverters(messageConverters)
    }

    @Bean
    fun websocketClient(): WebSocketStompClient {
        return WebSocketStompClient(StandardWebSocketClient()).apply { messageConverter = StringMessageConverter() }
    }
}


