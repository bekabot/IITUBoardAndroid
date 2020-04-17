package kz.iitu.iituboardandroid.api

class AuthRequestBody(
    val name: String,
    val surname: String,
    val password: String,
    val email: String
)

class LoginRequestBody(val password: String, val email: String, val fcmToken: String)

class RestorePasswordRequestBody(val email: String)

class AddRecordRequestBody(
    val token: String,
    val title: String,
    val body: String,
    val phone: String,
    val email: String,
    val instagram: String,
    val whatsapp: String,
    val vk: String,
    val telegram: String,
    val record_type: String,
    val author: String,
    val authorEmail: String
)