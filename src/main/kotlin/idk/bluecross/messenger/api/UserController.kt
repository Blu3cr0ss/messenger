package idk.bluecross.messenger.api

import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.store.dto.FullUserDataDto
import idk.bluecross.messenger.store.dto.GetChatDto
import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.UserDetails
import idk.bluecross.messenger.util.annotation.AuthenticatedUserDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@MessageMapping("/user")
class UserController(val userService: UserService) {
    @GetMapping("/getChats")
    fun getChats(@AuthenticatedUserDetails auth: UserDetails): List<Chat> {
        return userService.getChats(auth.id).filterNotNull()
    }


    @GetMapping("/getChatsSimple")
    fun getChatsSimple(@AuthenticatedUserDetails auth: UserDetails): Any {
        return getChats(auth).map { GetChatDto(it.id.toHexString(), it.name, it.description) }
    }

    @GetMapping("/getAvatarByUsername")
    fun getAvatarByUsername(username: String) = userService.getAvatarByUsername(username)

    @GetMapping("/getProfileByUsername")
    fun getProfileByUsername(username: String): FullUserDataDto {
        val details = userService.loadUserByUsername(username)
        val avatar = userService.getAvatarByUsername(username)
        return FullUserDataDto(details.displayedName, details.username, details.bio, avatar)
    }

    @PostMapping("/editProfile")
    fun editProfile(
        @RequestParam(required = false) username: String?,
        @RequestParam(required = false) displayedName: String?,
        @RequestParam(required = false) bio: String?,
        @RequestBody(required = false) avatar: ByteArray?,
        @AuthenticatedUserDetails userDetails: UserDetails,
    ) = runCatching {
        userService.editProfile(userDetails.id, username, displayedName, bio, avatar)
    }.recover { ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it.message) }.getOrNull()!!
}