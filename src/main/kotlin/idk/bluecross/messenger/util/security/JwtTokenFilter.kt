package idk.bluecross.messenger.util.security

import idk.bluecross.messenger.dao.RedisUserDetailsDao
import idk.bluecross.messenger.service.AuthService
import idk.bluecross.messenger.service.JwtService
import idk.bluecross.messenger.util.getLogger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter(
    var authService: AuthService,
    var jwtService: JwtService,
) : OncePerRequestFilter() {
    val LOGGER = getLogger()
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching {
            if (request.getHeader("Authorization")?.startsWith("Bearer ") == true) {
                val jwt = request.getHeader("Authorization")!!.substring(7)
                if (jwtService.getIp(jwt) == request.remoteAddr)
                    SecurityContextHolder.getContext().authentication =
                        authService.loginByJwt(jwt)
                else throw BadCredentialsException("Bad ip")
            } else throw AuthenticationCredentialsNotFoundException("No Authorization or Bearer found")
        }.onFailure {
            if (LOGGER.isDebugEnabled) LOGGER.debug(
                it.message
            )
        }
        filterChain.doFilter(request, response)
    }
}