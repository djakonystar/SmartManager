package uz.texnopos.smartmanager.data.models.report

import uz.texnopos.smartmanager.data.models.user.Supervisor

data class Report(
    val id: Int,
    val url: String,
    val date: String,
    val supervisor: Supervisor?,
)
