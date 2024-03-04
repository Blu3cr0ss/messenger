package idk.bluecross.messenger.store.entity

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import org.bson.types.ObjectId
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.redis.core.RedisHash
import org.springframework.security.core.userdetails.UserDetails

@RedisHash
@JsonIncludeProperties("username", "displayedName", "email", "password")
data class UserDetails(
    private var username: String,
    var displayedName: String,
    var email: String,
    private var password: String,
    var bio: String = ""
) : UserDetails {
    lateinit var id: ObjectId

    @PersistenceCreator
    constructor(
        username: String,
        displayedName: String,
        email: String,
        password: String,
        id: ObjectId,
        bio: String
    ) : this(
        username,
        displayedName,
        email,
        password,
        bio
    ) {
        this.id = id
    }

    override fun getAuthorities() = null

    override fun getPassword() = password
    fun setPassword(password: String) {
        this.password = password
    }

    override fun getUsername() = username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}