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

    override fun getCachedNews(): List<Record>? {
        val allRecords = localDS.getRecords()?.records
        return allRecords?.filter {
            it.record_type == "news"
        }
    }

    override fun getCachedVacancies(): List<Record>? {
        val allRecords = localDS.getRecords()?.records
        return allRecords?.filter {
            it.record_type == "vacancy"
        }
    }

    override fun getCachedAds(): List<Record>? {
        val allRecords = localDS.getRecords()?.records
        return allRecords?.filter {
            it.record_type == "ads"
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