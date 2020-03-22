package kz.iitu.iituboardandroid.api

//todo check internet connection before request
class RemoteDataSource(private val api: IituApi) {
    suspend fun sendAuthRequest(login: String, password: String, email: String) =
        api.auth(AuthRequestBody(login, password, email))
}