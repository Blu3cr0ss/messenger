package idk.bluecross.messenger.service

import idk.bluecross.messenger.dao.ChatDao
import idk.bluecross.messenger.dao.UserDao
import idk.bluecross.messenger.store.entity.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    val userDao: UserDao,
    val chatDao: ChatDao,
    val mongoTemplate: MongoTemplate
) : UserDetailsService {
    fun save(user: User) = userDao.save(user)
    fun find(id: ObjectId) = userDao.findById(id)

    fun emailExists(email: String) = userDao.existsByEmail(email)
    fun usernameExists(username: String) = userDao.existsByUsername(username)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails =
        userDao.findUserDetailsByUsername(username)[0]

    fun findUserByUsername(username: String) = userDao.findByUsername(username)

    fun getChats(id: Any) = userDao.getChats(id)

    fun getIdByUsername(username: String) = userDao.getIdByUsername(username)
    fun getAvatarByUsername(username: String) = userDao.getAvatarByUsername(username)
}