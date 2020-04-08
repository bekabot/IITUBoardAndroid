package kz.iitu.iituboardandroid.ui.board

import kz.iitu.iituboardandroid.api.LocalDataSource
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.api.response.RecordsResponse

class BoardRepositoryImpl(
    private val localDS: LocalDataSource,
    private val remoteDS: RemoteDataSource
) : BoardRepository {

    override suspend fun getAllRecords(token: String): RecordsResponse {
        return remoteDS.getAllRecords(token)
    }

    override fun getUserInfo() = localDS.getUserInfo()
}