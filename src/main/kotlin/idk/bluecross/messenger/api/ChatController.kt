package idk.bluecross.messenger.api

import idk.bluecross.messenger.service.ChatService
import idk.bluecross.messenger.store.dto.CreateChatDto
import idk.bluecross.messenger.store.dto.FullUserDataDto
import idk.bluecross.messenger.store.entity.UserDetails
import idk.bluecross.messenger.util.annotation.AuthenticatedUserDetails
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatController(
    var chatService: ChatService,
) {
    @PostMapping("/create")
    fun createNewChat(@RequestBody dto: CreateChatDto, @AuthenticatedUserDetails user: UserDetails) {
        chatService.saveWithUserNames(
            dto.name,
            dto.description,
            ArrayList(dto.users).apply { add(user.username) }.distinct()
        )
    }


    @PostMapping("/{chatId}/addMember")
    fun addMember(username: String, @PathVariable chatId: String, @AuthenticatedUserDetails user: UserDetails) =
        chatService.addToChatWithUsername(user.id, username, ObjectId(chatId))

    @PostMapping("/{chatId}/leave")
    fun leave(@PathVariable chatId: String, @AuthenticatedUserDetails user: UserDetails) =
        chatService.leaveChat(user.id, ObjectId(chatId))

    @GetMapping("/{chatId}/getMembers")
    fun getMembers(@PathVariable chatId: String) = chatService.getMembers(ObjectId(chatId))
        .map {
            FullUserDataDto(
                it.userDetails.displayedName,
                it.userDetails.username,
                it.userDetails.bio, it.avatar.byteArr
            )
        }
}