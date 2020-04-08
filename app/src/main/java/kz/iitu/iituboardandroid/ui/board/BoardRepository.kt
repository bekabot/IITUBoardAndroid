package kz.iitu.iituboardandroid.ui.board

import kz.iitu.iituboardandroid.api.LoginResponse
import kz.iitu.iituboardandroid.api.response.RecordsResponse

interface BoardRepository {
    suspend fun getAllRecords(token: String): RecordsResponse
    fun getUserInfo(): LoginResponse?
}