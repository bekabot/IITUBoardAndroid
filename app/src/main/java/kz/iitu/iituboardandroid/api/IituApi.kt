package kz.iitu.iituboardandroid.api

import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.api.response.RecordsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IituApi {
    @POST("${Constants.HOST}/api/auth/")
    suspend fun auth(@Body body: AuthRequestBody): CommonResponse

    @POST("${Constants.HOST}/api/login/")
    suspend fun login(@Body body: LoginRequestBody): LoginResponse

    @POST("${Constants.HOST}/api/restore/")
    suspend fun restorePassword(@Body body: RestorePasswordRequestBody): CommonResponse

    @GET("${Constants.HOST}/api/board/")
    suspend fun getAllRecords(@Query("token") token: String): RecordsResponse
}