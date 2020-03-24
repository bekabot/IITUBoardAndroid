package kz.iitu.iituboardandroid.api

import java.io.Serializable

data class CommonResponse(var error: Boolean?, var message: String?) : Serializable

data class LoginResponse(
    val id: Int?,
    val name: String?,
    val surname: String?,
    val password: String?,
    val email: String?,
    val token: String?,
    val error: String?
)