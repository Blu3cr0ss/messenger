package idk.bluecross.messenger.api

import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.store.dto.GetChatDto
import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.UserDetails
import idk.bluecross.messenger.util.annotation.AuthenticatedUserDetails
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
        return getChats(auth).map { GetChatDto(it.id.toHexString(), it.name) }
    }

}