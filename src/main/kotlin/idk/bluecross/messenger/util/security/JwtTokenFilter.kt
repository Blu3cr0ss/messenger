package idk.bluecross.messenger.util.security

import idk.bluecross.messenger.service.AuthService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter(
    var authService: AuthService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching {
            if (request.getHeader("Authorization")?.startsWith("Bearer ") == true) {
                SecurityContextHolder.getContext().authentication =
                    authService.loginByAuthorizationHeader(request.getHeader("Authorization"))
            }
        }.onFailure {
            it.printStackTrace()
        }
        filterChain.doFilter(request, response)
    }
}