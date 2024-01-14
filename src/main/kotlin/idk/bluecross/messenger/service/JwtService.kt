package idk.bluecross.messenger.service

import idk.bluecross.messenger.store.entity.UserDetails
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {
    @Value("\${security.jwt.secret}")
    var secret: String = "secret"

    @Value("\${security.jwt.lifetime}")
    var lifetime: Int = 180000

    fun generateToken(auth: Authentication, ip: String) = Jwts.builder()
        .subject((auth.principal as UserDetails).username)
        .claims(mapOf("ip" to ip))
        .issuedAt(Date())
        .expiration(Date(System.currentTimeMillis() + lifetime))
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact()

    fun getName(jwt: String) = Jwts.parser().setSigningKey(secret).build().parseSignedClaims(jwt).payload.subject
}
