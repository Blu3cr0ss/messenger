package idk.bluecross.messenger.store.dao

import idk.bluecross.messenger.store.entity.FileInDb
import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.store.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ExistsQuery
import org.springframework.data.mongodb.repository.Query
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserDao(
    val userRepository: UserRepository
) {
    fun save(user: User) = userRepository.insert(user)
    fun findById(id: ObjectId) = userRepository.findById(id)

    fun existsByUsername(username: String) = userRepository.existsByUsername(username)
    fun existsByEmail(email: String) = userRepository.existsByEmail(email)

    fun findByUsername(username: String) =
        userRepository.findByUsername(username).orElseThrow { UsernameNotFoundException(username) }

    fun findByEmail(email: String) =
        userRepository.findByEmail(email).orElseThrow { UsernameNotFoundException(email) }
}