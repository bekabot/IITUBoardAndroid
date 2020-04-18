package kz.iitu.iituboardandroid.ui.board

import kz.iitu.iituboardandroid.api.AddRecordRequestBody
import kz.iitu.iituboardandroid.api.CommonResponse
import kz.iitu.iituboardandroid.api.LoginResponse
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.api.response.RecordResponse
import kz.iitu.iituboardandroid.api.response.RecordsResponse
import java.io.File

interface BoardRepository {
    suspend fun getAllRecords(token: String): RecordsResponse
    fun getUserInfo(): LoginResponse?
    fun getCachedNews(): List<Record>?
    fun getCachedVacancies(): List<Record>?
    fun getCachedAds(): List<Record>?
    fun getUserRecords(): List<Record>?
    suspend fun getRecordById(token: String, id: Int): RecordResponse?
    suspend fun getNews(token: String): RecordsResponse
    suspend fun getAds(token: String): RecordsResponse
    suspend fun getVacancies(token: String): RecordsResponse
    suspend fun addRecord(
        body: AddRecordRequestBody,
        file1: File?,
        fileName1: String?,
        file2: File?,
        fileName2: String?,
        file3: File?,
        fileName3: String?
    ): CommonResponse
}