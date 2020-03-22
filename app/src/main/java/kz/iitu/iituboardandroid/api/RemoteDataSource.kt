package kz.iitu.iituboardandroid.api

class RemoteDataSource(private val api: IituApi, private val networkManager: NetworkManager) {

    private suspend fun <T> onPerformRequest(mainBlock: suspend () -> T) =
        if (networkManager.isConnected) {
            mainBlock.invoke()
        } else {
            throw NoInternetException()
        }

    suspend fun sendAuthRequest(login: String, password: String, email: String) =
        onPerformRequest { (api.auth(AuthRequestBody(login, password, email))) }
}