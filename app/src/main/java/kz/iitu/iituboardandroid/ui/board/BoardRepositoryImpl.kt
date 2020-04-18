package kz.iitu.iituboardandroid.ui.board

import kz.iitu.iituboardandroid.api.AddRecordRequestBody
import kz.iitu.iituboardandroid.api.CommonResponse
import kz.iitu.iituboardandroid.api.LocalDataSource
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.api.response.RecordResponse
import kz.iitu.iituboardandroid.api.response.RecordsResponse
import java.io.File

class BoardRepositoryImpl(
    private val localDS: LocalDataSource,
    private val remoteDS: RemoteDataSource
) : BoardRepository {

    override suspend fun getAllRecords(token: String): RecordsResponse {
        val recordsResponse = remoteDS.getAllRecords(token)
        if (recordsResponse.records?.isNotEmpty() == true) {
            localDS.saveRecords(recordsResponse)
        }
        return recordsResponse
    }

    override fun getUserInfo() = localDS.getUserInfo()

    override fun getCachedNews(): List<Record>? {
        val filteredRecords = localDS.getRecords()?.records?.filter {
            it.record_type == "news"
        }
        return getSortedRecords(filteredRecords)
    }

    override fun getCachedVacancies(): List<Record>? {
        val filteredRecords = localDS.getRecords()?.records?.filter {
            it.record_type == "vacancy"
        }
        return getSortedRecords(filteredRecords)
    }

    override fun getCachedAds(): List<Record>? {
        val filteredRecords = localDS.getRecords()?.records?.filter {
            it.record_type == "ads"
        }
        return getSortedRecords(filteredRecords)
    }

    override fun getUserRecords(): List<Record>? {
        val userEmail = getUserInfo()?.email
        val records = localDS.getRecords()?.records?.filter { it.authorEmail == userEmail }
        return getSortedRecords(records)
    }

    private fun getSortedRecords(records: List<Record>?) =
        records?.let {
            val mutableList = records.toMutableList()
            mutableList.sortWith(compareByDescending { it.getFormattedCreationDate() })
            mutableList
        }

    override suspend fun getRecordById(token: String, id: Int): RecordResponse? {
        val cachedRecord = localDS.getRecords()?.records?.firstOrNull {
            it.id == id
        }
        return cachedRecord?.let {
            RecordResponse("", it)
        } ?: remoteDS.getRecordById(token, id)
    }

    override suspend fun getNews(token: String) = getRecordsByType("news", token)

    override suspend fun getAds(token: String) = getRecordsByType("ads", token)

    override suspend fun getVacancies(token: String) = getRecordsByType("vacancy", token)

    private suspend fun getRecordsByType(recordType: String, token: String): RecordsResponse {
        val recordsResponse = remoteDS.getAllRecords(token)
        recordsResponse.message?.let {
            return recordsResponse
        } ?: run {
            val records = recordsResponse.records
            return if (records?.isNotEmpty() == true) {
                localDS.saveRecords(recordsResponse)
                val recordsByType = records.filter { it.record_type == recordType }
                val sortedRecords = getSortedRecords(recordsByType)
                RecordsResponse("", sortedRecords)
            } else {
                RecordsResponse("", emptyList())
            }
        }
    }

    override suspend fun addRecord(
        body: AddRecordRequestBody,
        file1: File?,
        fileName1: String?,
        file2: File?,
        fileName2: String?,
        file3: File?,
        fileName3: String?
    ): CommonResponse {
        return remoteDS.addRecord(
            body, file1, fileName1, file2, fileName2, file3, fileName3
        )
    }
}