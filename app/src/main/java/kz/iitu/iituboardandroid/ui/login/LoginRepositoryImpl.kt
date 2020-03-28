package kz.iitu.iituboardandroid.ui.login

import kz.iitu.iituboardandroid.api.LocalDataSource
import kz.iitu.iituboardandroid.api.LoginResponse
import kz.iitu.iituboardandroid.api.RemoteDataSource

class LoginRepositoryImpl(
    private val localDS: LocalDataSource,
    private val remoteDS: RemoteDataSource
) : LoginRepository {

    override suspend fun sendLoginRequest(password: String, email: String, fcmToken: String) =
        remoteDS.sendLoginRequest(password, email, fcmToken)

    override fun saveUserInfo(userInfo: LoginResponse) {
        localDS.saveUserInfo(userInfo)
    }

    override fun getUserInfo(): LoginResponse? =
        localDS.getUserInfo()
}