package kz.iitu.iituboardandroid.api

import kz.iitu.iituboardandroid.api.response.RecordsResponse
import wiki.depasquale.mcache.give
import wiki.depasquale.mcache.obtain

class LocalDataSource {

    fun getUserInfo() = try {
        obtain<LoginResponse>().build().getNow()
    } catch (e: Exception) {
        null
    }

    fun saveUserInfo(userInfo: LoginResponse) {
        userInfo.give().build().getNow()
    }

    fun getRecords() = try {
        obtain<RecordsResponse>().build().getNow()
    } catch (e: Exception) {
        null
    }

    fun saveRecords(records: RecordsResponse) {
        records.give().build().getNow()
    }
}