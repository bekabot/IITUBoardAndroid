package kz.iitu.iituboardandroid.ui.login

import kz.iitu.iituboardandroid.api.LoginResponse

interface LoginRepository {
    suspend fun sendLoginRequest(password: String, email: String, fcmToken: String): LoginResponse
    fun saveUserInfo(userInfo: LoginResponse)
    fun getUserInfo(): LoginResponse?
}