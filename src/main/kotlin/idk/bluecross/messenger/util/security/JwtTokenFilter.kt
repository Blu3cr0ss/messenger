package idk.bluecross.messenger.util.security

import idk.bluecross.messenger.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter(
    var jwtService: JwtService,
    var userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching {
            if (request.getHeader("Authorization")?.startsWith("Bearer ") == true) {
                val jwt = request.getHeader("Authorization").substring(7)
                val username = jwtService.getName(jwt)
                val userDetails = userDetailsService.loadUserByUsername(username)
                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(userDetails, null)
            }
        }.onFailure {
            it.printStackTrace()
        }
        filterChain.doFilter(request, response)
    }
}