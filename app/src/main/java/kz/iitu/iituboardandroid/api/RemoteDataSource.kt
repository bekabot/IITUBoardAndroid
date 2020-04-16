package kz.iitu.iituboardandroid.api

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLEncoder

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

    suspend fun getAllRecords(token: String) =
        onPerformRequest { api.getAllRecords(token) }

    suspend fun getRecordById(token: String, id: Int) =
        onPerformRequest { api.getRecordById(token, id) }

    suspend fun addRecord(
        token: String,
        title: String,
        description: String,
        file1: File?,
        fileName1: String?,
        file2: File?,
        fileName2: String?,
        file3: File?,
        fileName3: String?
    ): CommonResponse {
        return onPerformRequest {
            val fileToUpload1 = file1?.let {
                val requestBody = RequestBody.create(MediaType.parse("*/*"), file1)
                MultipartBody.Part.createFormData(
                    "file",
                    URLEncoder.encode(fileName1, "UTF-8"),
                    requestBody
                )
            }

            val fileToUpload2 = file2?.let {
                val requestBody = RequestBody.create(MediaType.parse("*/*"), file2)
                MultipartBody.Part.createFormData(
                    "file",
                    URLEncoder.encode(fileName2, "UTF-8"),
                    requestBody
                )
            }

            val fileToUpload3 = file3?.let {
                val requestBody = RequestBody.create(MediaType.parse("*/*"), file3)
                MultipartBody.Part.createFormData(
                    "file",
                    URLEncoder.encode(fileName3, "UTF-8"),
                    requestBody
                )
            }

            if (fileToUpload1 != null || fileToUpload2 != null || fileToUpload3 != null) {
                api.addRecord(
                    token,
                    title,
                    description,
                    fileToUpload1,
                    fileToUpload2,
                    fileToUpload3
                )
            } else {
                api.addRecord(
                    token,
                    title,
                    description
                )
            }
        }
    }
}