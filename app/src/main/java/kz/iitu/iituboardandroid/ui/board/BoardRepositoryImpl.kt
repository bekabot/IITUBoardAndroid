package kz.iitu.iituboardandroid.ui.board

import kz.iitu.iituboardandroid.api.LocalDataSource
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.api.response.RecordResponse
import kz.iitu.iituboardandroid.api.response.RecordsResponse

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

    override fun getCachedNews() = getRecordsByType("news")

    override fun getCachedVacancies() = getRecordsByType("vacancy")

    override fun getCachedAds() = getRecordsByType("ads")

    private fun getRecordsByType(recordType: String): List<Record>? {
        val filteredRecords = localDS.getRecords()?.records?.filter {
            it.record_type == recordType
        }
        return filteredRecords?.let {
            val mutableList = filteredRecords.toMutableList()
            mutableList.sortWith(compareByDescending { it.getFormattedCreationDate() })
            mutableList
        }
    }

    override suspend fun getRecordById(token: String, id: Int): RecordResponse? {
        val cachedRecord = localDS.getRecords()?.records?.firstOrNull {
            it.id == id
        }
        return cachedRecord?.let {
            RecordResponse("", it)
        } ?: remoteDS.getRecordById(token, id)
    }
}