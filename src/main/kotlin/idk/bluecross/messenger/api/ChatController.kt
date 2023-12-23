package idk.bluecross.messenger.api

import idk.bluecross.messenger.service.ChatService
import idk.bluecross.messenger.store.dto.CreateChatDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatController(
    var chatService: ChatService
) {
    @PostMapping("/create")
    fun createNewChat(@RequestBody dto: CreateChatDto): Any {
        return runCatching {
            chatService.saveWithUserIds(dto.name, dto.description, dto.users)
        }
            .recoverCatching {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it.message)
            }
            .getOrThrow()
    }


}