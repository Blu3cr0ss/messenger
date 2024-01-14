package idk.bluecross.messenger.store.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.security.core.userdetails.UserDetails

data class UserDetails(
    private var username: String,
    var displayedName: String,
    var email: String,
    private var password: String
) : UserDetails {
    lateinit var id: ObjectId

    @PersistenceCreator
    constructor(
        username: String,
        displayedName: String,
        email: String,
        password: String,
        id: ObjectId
    ) : this(username, displayedName, email, password) {
        this.id = id
    }

    override fun getAuthorities() = null

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}