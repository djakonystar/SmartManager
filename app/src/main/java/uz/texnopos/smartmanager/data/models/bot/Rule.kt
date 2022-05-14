package uz.texnopos.smartmanager.data.models.bot

data class Rule(
    val id: Int,
    val startHour: String,
    val endHour: String,
    val startMinute: String,
    val endMinute: String,
    val chatId: String,
    val sendHour: String,
    val sendMinute: String
)
