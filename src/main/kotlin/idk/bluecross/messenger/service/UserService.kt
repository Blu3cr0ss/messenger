package idk.bluecross.messenger.service

import idk.bluecross.messenger.dao.ChatDao
import idk.bluecross.messenger.dao.RedisUserDetailsDao
import idk.bluecross.messenger.dao.UserDao
import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.store.entity.UserDetails
import idk.bluecross.messenger.store.exception.BadAvatarException
import idk.bluecross.messenger.store.exception.BadCredentialsException
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.imageio.ImageIO

@Service
class UserService(
    val userDao: UserDao,
    val chatDao: ChatDao,
    val mongoTemplate: MongoTemplate,
    var encoder: PasswordEncoder,
    var redisUserDetailsDao: RedisUserDetailsDao
) : UserDetailsService {
    fun isCorrectUsername(username: String) =
        Regex("^[a-zA-Z0-9._-]{4,16}$").matches(username) // Any latin letter, any number and ._-

    fun isCorrectPassword(password: String) = Regex(
        "^.{4,16}$" // can have numbers, upper and lowercase latin letters and be at least 4 symbols long
    ).matches(password)

    fun isCorrectEmail(email: String) =
        Regex(
            "^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            RegexOption.IGNORE_CASE
        ).matches(email) //RFC 5322 standard

    fun save(user: User) = userDao.save(user)
    fun find(id: ObjectId) = userDao.findById(id)
    fun registerUser(user: User): User? {
        if (!isCorrectEmail(user.userDetails.email)) throw BadCredentialsException("Invalid email")
        if (!isCorrectUsername(user.userDetails.username)) throw BadCredentialsException("Invalid username. Allowed symbols: Latin letters, numbers and ._-. Allowed length: 4-16 symbols")
        if (!isCorrectPassword(user.userDetails.password)) throw BadCredentialsException("Invalid password. Allowed length: 4-16 symbols")
        if (usernameExists(user.userDetails.username)) throw BadCredentialsException("Username exists")
        if (emailExists(user.userDetails.email)) throw BadCredentialsException("Email exists")
        return save(user.apply { userDetails.password = encoder.encode(userDetails.password) })
    }

    fun emailExists(email: String) = userDao.existsByEmail(email)
    fun usernameExists(username: String) = userDao.existsByUsername(username)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails =
        userDao.findUserDetailsByUsername(username)

    fun loadUserByUsernameWithRedis(username: String) =
        redisUserDetailsDao.get(username) ?: loadUserByUsername(username).also { redisUserDetailsDao.set(it) }


    fun findUserByUsername(username: String) = userDao.findByUsername(username)

    fun getChats(id: Any) = userDao.getChats(id)

    fun getIdByUsername(username: String) = userDao.getIdByUsername(username)
    fun getAvatarByUsername(username: String) = userDao.getAvatarByUsername(username)
    fun setAvatar(userId: Any, avatar: ByteArray) = userDao.setAvatar(userId, avatar)

    fun setUsername(userId: Any, username: String) = userDao.setUsername(userId, username)

    fun setDisplayedName(userId: Any, displayedName: String) = userDao.setDisplayedName(userId, displayedName)

    fun setBio(userId: Any, bio: String) = userDao.setBio(userId, bio)
    fun editProfile(
        userId: Any, username: String?,
        displayedName: String?,
        bio: String?,
        avatar: ByteArray?
    ) {
        if (username != null) {
            if (!usernameExists(username) && isCorrectUsername(username)) setUsername(userId, username)
        }
        if (displayedName != null) {
            setDisplayedName(userId, displayedName)
        }
        if (bio != null) {
            setBio(userId, bio)
        }
        if (avatar != null) ImageIO.read(avatar.inputStream())?.let { img ->
            val maxDimensions = 128
            if (img.width == img.height && img.width <= maxDimensions)
                setAvatar(userId, avatar)
            else throw BadAvatarException("Image height should be equal to width and equal to ${maxDimensions}px")
        }
    }
}