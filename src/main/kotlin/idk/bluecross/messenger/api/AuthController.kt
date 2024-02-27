package idk.bluecross.messenger.api

import idk.bluecross.messenger.service.JwtService
import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.store.dto.LoginDto
import idk.bluecross.messenger.store.dto.RegisterDto
import idk.bluecross.messenger.store.entity.IdRefList
import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.store.entity.UserDetails
import idk.bluecross.messenger.util.getLogger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    var jwtService: JwtService,
    var userService: UserService,
    var encoder: PasswordEncoder,
    var authenticationManager: AuthenticationManager
) {
    val LOGGER = getLogger()

    @PostMapping("/register")
    fun register(req: RegisterDto) = runCatching {
        userService.registerUser(
            User(
                User.DEFAULT_AVATAR_FILE,
                User.Status.OFFLINE,
                IdRefList(),
                UserDetails(req.username, req.username, req.email, encoder.encode(req.password), "")
            )
        )
    }.recover {
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it.message)
    }.getOrNull()!!


    @PostMapping("/login")
    fun login(req: LoginDto, httpServletRequest: HttpServletRequest) = runCatching {
        val auth =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(req.username, req.password))
        SecurityContextHolder.getContext().authentication = auth
        val jwt = jwtService.generateToken(auth, httpServletRequest.remoteAddr)
        ResponseEntity.status(HttpStatus.OK).body(jwt)
    }.recover {
        if (LOGGER.isDebugEnabled) LOGGER.debug("Unauthorized ${req.username} with ${req.password}")
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка авторизации")
    }.getOrNull()!!

}