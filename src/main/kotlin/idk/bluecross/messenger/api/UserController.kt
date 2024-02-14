package idk.bluecross.messenger.api

import idk.bluecross.messenger.service.UserService
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
    fun getChats(@AuthenticatedUserDetails auth: UserDetails): Any {
        return userService.getChatsByAny("userDetails.username", auth.username)
    }

}