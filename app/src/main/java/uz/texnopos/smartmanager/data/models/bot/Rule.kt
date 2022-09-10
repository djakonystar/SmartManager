package uz.texnopos.smartmanager.data.models.bot

import com.google.gson.annotations.SerializedName

data class Rule(
    val startHour: String,
    val endHour: String,
    val startMinute: String,
    val endMinute: String,
    @SerializedName("chat_id")
    val chatId: String,
    @SerializedName("ChecksendMinut")
    val sendMinute: String,
    @SerializedName("ChecksendHour")
    val sendHour: String
)
