package idk.bluecross.messenger.store.dao

import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.store.entity.UserDetails
import idk.bluecross.messenger.store.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserDao(
    val userRepository: UserRepository,
    val mongoTemplate: MongoTemplate
) {
    fun save(user: User) = userRepository.save(user)
    fun findById(id: ObjectId) = userRepository.findById(id)

    fun existsByUsername(username: String) = userRepository.existsByUsername(username)
    fun existsByEmail(email: String) = userRepository.existsByEmail(email)

    fun findByUsername(username: String) =
        userRepository.findByUsername(username).orElseThrow { UsernameNotFoundException(username) }

    fun findUserDetailsByUsername(username: String) = mongoTemplate.findDistinct(
        Query(Criteria("userDetails.username").`is`(username)),
        "userDetails",
        User::class.java,
        UserDetails::class.java
    )

    fun findByEmail(email: String) =
        userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }

    fun getChats(id: Any) = mongoTemplate.findDistinct(
        Query(Criteria("_id").`is`(id)),
        "chats",
        User::class.java,
        Chat::class.java
    )
}