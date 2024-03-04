package idk.bluecross.messenger.service

import idk.bluecross.messenger.dao.RedisUserDetailsDao
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
    var jwtService: JwtService,
    var userService: UserService,
) {
    fun loginByJwt(jwt: String): UsernamePasswordAuthenticationToken {
        val username = jwtService.getName(jwt)
        val userDetails = userService.loadUserByUsernameWithRedis(username)
        return UsernamePasswordAuthenticationToken(userDetails, null)
    }
}