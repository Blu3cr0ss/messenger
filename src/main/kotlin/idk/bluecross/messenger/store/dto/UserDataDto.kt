package idk.bluecross.messenger.store.dto

data class SimpleUserDataDto(val displayedName: String, val avatar: ByteArray)
data class FullUserDataDto(
    val displayedName: String,
    val username: String,
    val description: String,
    val avatar: ByteArray
)