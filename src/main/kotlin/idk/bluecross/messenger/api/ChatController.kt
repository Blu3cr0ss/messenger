package idk.bluecross.messenger.api

import idk.bluecross.messenger.service.ChatService
import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.store.dto.CreateChatDto
import idk.bluecross.messenger.store.entity.UserDetails
import idk.bluecross.messenger.util.annotation.AuthenticatedUserDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatController(
    var chatService: ChatService,
) {
    @PostMapping("/create")
    fun createNewChat(@RequestBody dto: CreateChatDto, @AuthenticatedUserDetails user: UserDetails): Any {
        return runCatching {
            chatService.saveWithUserIds(
                dto.name,
                dto.description,
                ArrayList(dto.users).apply { add(user.id)}.distinct())
        }
            .recoverCatching {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it.message)
            }
            .getOrThrow()
    }
}