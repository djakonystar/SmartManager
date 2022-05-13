package uz.texnopos.smartmanager.data.models

data class GenericResponse<T> (
    val message: String? = "",
    val success: Boolean,
    val data: T?
)
