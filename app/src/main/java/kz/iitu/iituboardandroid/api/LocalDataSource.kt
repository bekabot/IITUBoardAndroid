package kz.iitu.iituboardandroid.api

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
}