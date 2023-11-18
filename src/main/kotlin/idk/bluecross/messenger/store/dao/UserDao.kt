package idk.bluecross.messenger.store.dao

import idk.bluecross.messenger.store.entity.FileInDb
import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.store.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserDao(
    val userRepository: UserRepository
) {
    fun save(user: User) = userRepository.insert(user)
    fun find(id: ObjectId) = userRepository.findById(id)
}