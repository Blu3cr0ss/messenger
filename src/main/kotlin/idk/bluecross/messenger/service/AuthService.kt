package idk.bluecross.messenger.service

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    var jwtService: JwtService,
    var userDetailsService: UserDetailsService
) {
    fun loginByJwt(jwt:String): UsernamePasswordAuthenticationToken {
        val username = jwtService.getName(jwt)
        val userDetails = userDetailsService.loadUserByUsername(username)
       return UsernamePasswordAuthenticationToken(userDetails, null)
    }
    fun loginByAuthorizationHeader(jwt:String): UsernamePasswordAuthenticationToken {
        return loginByJwt(jwt.substring(7))
    }
}