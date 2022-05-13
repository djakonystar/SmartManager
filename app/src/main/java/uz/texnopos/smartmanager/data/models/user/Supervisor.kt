package uz.texnopos.smartmanager.data.models.user

data class Supervisor(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val chatId: String,
    val groupChatId: String
)
