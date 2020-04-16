package kz.iitu.iituboardandroid.api

import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.api.response.RecordResponse
import kz.iitu.iituboardandroid.api.response.RecordsResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface IituApi {
    @POST("${Constants.HOST}/api/auth/")
    suspend fun auth(@Body body: AuthRequestBody): CommonResponse

    @POST("${Constants.HOST}/api/login/")
    suspend fun login(@Body body: LoginRequestBody): LoginResponse

    @POST("${Constants.HOST}/api/restore/")
    suspend fun restorePassword(@Body body: RestorePasswordRequestBody): CommonResponse

    @GET("${Constants.HOST}/api/board/")
    suspend fun getAllRecords(@Query("token") token: String): RecordsResponse

    @GET("${Constants.HOST}/api/board/")
    suspend fun getRecordById(
        @Query("token") token: String,
        @Query("id") recordId: Int
    ): RecordResponse

    @Multipart
    @POST("${Constants.HOST}/api/board/")
    suspend fun addRecord(
        @Query("token") token: String,
        @Query("title") title: String,
        @Query("body") description: String,
        @Part file1: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?,
        @Part file3: MultipartBody.Part?
    ): CommonResponse

    @POST("${Constants.HOST}/api/board/")
    suspend fun addRecord(
        @Query("token") token: String,
        @Query("title") title: String,
        @Query("body") description: String
    ): CommonResponse
}