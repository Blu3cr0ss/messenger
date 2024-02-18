package idk.bluecross.messenger.store.dto

data class CreateChatDto(
    val name: String,
    val description: String,
    val users: List<String>
)