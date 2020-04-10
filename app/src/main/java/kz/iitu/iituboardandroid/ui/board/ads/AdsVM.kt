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
}