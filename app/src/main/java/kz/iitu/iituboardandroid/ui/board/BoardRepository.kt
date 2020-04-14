package kz.iitu.iituboardandroid.ui.board

import kz.iitu.iituboardandroid.api.LoginResponse
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.api.response.RecordResponse
import kz.iitu.iituboardandroid.api.response.RecordsResponse

interface BoardRepository {
    suspend fun getAllRecords(token: String): RecordsResponse
    fun getUserInfo(): LoginResponse?
    fun getCachedNews(): List<Record>?
    fun getCachedVacancies(): List<Record>?
    fun getCachedAds(): List<Record>?
    suspend fun getRecordById(token: String, id: Int): RecordResponse?
    suspend fun getNews(token: String): RecordsResponse
    suspend fun getAds(token: String): RecordsResponse
    suspend fun getVacancies(token: String): RecordsResponse
}