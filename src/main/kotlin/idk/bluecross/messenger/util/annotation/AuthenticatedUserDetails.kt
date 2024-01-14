package idk.bluecross.messenger.util.annotation

import org.springframework.security.core.annotation.CurrentSecurityContext

@CurrentSecurityContext(expression = "authentication.principal")
annotation class AuthenticatedUserDetails
