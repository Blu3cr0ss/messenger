package idk.bluecross.messenger.service

import idk.bluecross.messenger.store.dao.UserDao
import idk.bluecross.messenger.store.entity.User
import org.bson.types.ObjectId
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    val userDao: UserDao
) : UserDetailsService {
    fun save(user: User) = userDao.save(user)
    fun find(id: ObjectId) = userDao.findById(id)

    fun emailExists(email: String) = userDao.existsByEmail(email)
    fun usernameExists(username: String) = userDao.existsByUsername(username)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails =
        userDao.findByUsername(username).userDetails

    fun findUserByUsername(username: String) = userDao.findByUsername(username)

}