package kz.iitu.iituboardandroid.ui.board.ads

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository

class AdsVM(private val repository: BoardRepository) : BaseVM() {
    val ads = MutableLiveData<List<Record>?>()

    fun setUpAds() {
        ads.value = repository.getCachedAds()
    }

    fun requestFreshAds() {
        val userData = repository.getUserInfo()
        userData?.let {
            launchLoadingCoroutine(mainBlock = {
                val result = repository.getAds(userData.token ?: "")
                when (result.message) {
                    null, "" -> {
                        ads.value = result.records
                        showMessage.value = "Объявления обновлены"
                    }
                    "USER_NOT_ACTIV", "USER_NOT_FOUND" -> {
                        logout.value = true
                    }
                }
            })
        } ?: run {
            logout.value = true
        }
    }
}