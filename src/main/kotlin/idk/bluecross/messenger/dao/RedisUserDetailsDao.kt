package idk.bluecross.messenger.dao

import idk.bluecross.messenger.config.RedisTemplateFactory
import idk.bluecross.messenger.store.entity.UserDetails
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Component
@Transactional
class RedisUserDetailsDao(
    redisTemplateFactory: RedisTemplateFactory
) {
    val redisTemplate = redisTemplateFactory.forClass(UserDetails::class.java)
    fun set(details: UserDetails) {
        redisTemplate?.opsForValue()?.set(details.username, details, Duration.ofDays(1))
    }

    fun get(username: String) = redisTemplate?.opsForValue()?.get(username)
}