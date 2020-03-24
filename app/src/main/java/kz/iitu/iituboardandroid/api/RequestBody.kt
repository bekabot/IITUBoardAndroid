package kz.iitu.iituboardandroid.api

class AuthRequestBody(
    val name: String,
    val surname: String,
    val password: String,
    val email: String
)

class LoginRequestBody(val password: String, val email: String)

class RestorePasswordRequestBody(val email: String)