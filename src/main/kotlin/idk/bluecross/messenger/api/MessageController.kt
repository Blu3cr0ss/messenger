package idk.bluecross.messenger.api

import idk.bluecross.messenger.service.ChatService
import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.store.dto.MessageDto
import idk.bluecross.messenger.store.entity.*
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/chat/{id}/messages")
@MessageMapping("/chat/{id}/messages")
class MessageController(
    var chatService: ChatService,
    var userService: UserService
) {

    @MessageMapping("/send")
    @SendTo("/topic/chat/{id}/messages")
    fun send(
        message: MessageDto,
        @DestinationVariable("id") chatId: String,
        @AuthenticationPrincipal principal: UsernamePasswordAuthenticationToken
    ): Message {
        val msg = Message(
            IdRef.fromEntity(userService.findUserByUsername(principal.name)),
            listOf(),
            message.contentTree,
            Message.State.SENT
        )
        chatService.saveMessage(ObjectId(chatId), msg)
        return msg
    }


    @GetMapping("/get") // `/messages`
    fun messages(@PathVariable("id") chatId: String) =
        chatService.find(ObjectId(chatId)).getOrNull()?.messages ?: ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Chat id not found")

}