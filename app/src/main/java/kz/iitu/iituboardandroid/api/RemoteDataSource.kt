package kz.iitu.iituboardandroid.api

class RemoteDataSource(private val api: IituApi, private val networkManager: NetworkManager) {

    private suspend fun <T> onPerformRequest(mainBlock: suspend () -> T) =
        if (networkManager.isConnected) {
            mainBlock.invoke()
        } else {
            throw NoInternetException()
        }

    suspend fun sendAuthRequest(name: String, surname: String, password: String, email: String) =
        onPerformRequest { api.auth(AuthRequestBody(name, surname, password, email)) }

    suspend fun sendLoginRequest(password: String, email: String, fcmToken: String) =
        onPerformRequest { api.login(LoginRequestBody(password, email, fcmToken)) }

    suspend fun sendRestorePasswordRequest(email: String) =
        onPerformRequest { api.restorePassword(RestorePasswordRequestBody(email)) }
}