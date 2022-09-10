package uz.texnopos.smartmanager.data.models

data class GenericResponse<T> (
    val success: Boolean,
    val code: Int,
    val message: String? = "",
    val payload: T?
)
