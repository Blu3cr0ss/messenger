package idk.bluecross.messenger.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.stereotype.Component


@Configuration
@EnableRedisRepositories
class RedisConfig {
    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory? = runCatching {
        JedisConnectionFactory(RedisStandaloneConfiguration().apply {
            hostName = "localhost"
            port = 6379
        })
    }.onFailure { it.printStackTrace() }.getOrNull()


    @Bean
    fun stringRedisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, String>? =
        runCatching {
            val template = RedisTemplate<String, String>()
            template.connectionFactory = redisConnectionFactory
            template.valueSerializer = GenericToStringSerializer(Any::class.java)
            return@runCatching template
        }.getOrNull()

    @Bean
    fun mappingJacksonRedisTemplate(
        redisConnectionFactory: RedisConnectionFactory?,
        objectMapper: ObjectMapper
    ): RedisTemplate<String, *>? = runCatching {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory
        template.valueSerializer = GenericJackson2JsonRedisSerializer(
            objectMapper.copy().enableDefaultTyping()
        )
        return@runCatching template
    }.getOrNull()

    @Bean
    fun container(
        connectionFactory: RedisConnectionFactory?,
    ): RedisMessageListenerContainer? = runCatching {
        if (connectionFactory == null ||
            !runCatching { connectionFactory.connection.ping();true }.getOrDefault(false)
        ) return@runCatching null // Check connection
        val container = RedisMessageListenerContainer()
        container.connectionFactory = connectionFactory
//        container.addMessageListener(redisFileInDbCheckDuplicatesListener, PatternTopic("FileInDbCheckDuplicates"))
        return@runCatching container
    }.getOrNull()
}

@Component
class RedisTemplateFactory(
    private val redisConnectionFactory: RedisConnectionFactory?,
    private val objectMapper: ObjectMapper
) {
    fun <T> forClass(clazz: Class<T>) = runCatching {
        val template = RedisTemplate<String, T>()
        template.connectionFactory = redisConnectionFactory
        template.valueSerializer = Jackson2JsonRedisSerializer<T>(objectMapper, clazz)
        template.afterPropertiesSet()
        return@runCatching template
    }.getOrNull()
}