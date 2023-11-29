package idk.bluecross.messenger.store.entity

import org.springframework.security.core.userdetails.UserDetails

class UserDetails(
    private var username: String,
    var displayedName: String,
    var email: String,
    private var password: String
) : UserDetails {
    override fun getAuthorities() = null

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}