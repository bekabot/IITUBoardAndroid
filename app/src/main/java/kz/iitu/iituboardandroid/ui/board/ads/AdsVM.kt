package kz.iitu.iituboardandroid.ui.board.ads

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository
import java.util.*

class AdsVM(private val repository: BoardRepository) : BaseVM() {
    val ads = MutableLiveData<List<Record>?>()
    val clearInputFields = MutableLiveData(false)

    @ExperimentalCoroutinesApi
    private val searchQueryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResult = searchQueryChannel
        .asFlow()
        .debounce(500)
        .mapLatest {
            val key = it
            repository.getCachedAds()
                ?.filter {
                    val keyLowered = key.toLowerCase(Locale.getDefault())
                    it.record_title.toLowerCase(Locale.getDefault()).contains(keyLowered) ||
                            it.record_body.toLowerCase(Locale.getDefault()).contains(keyLowered)
                }
        }
        .asLiveData()

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
                        clearInputFields.value = true
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

    fun filterAdsByCategory(category: String) {
        val cachedAds = repository.getCachedAds()
        ads.value = if (category.isEmpty()) {
            cachedAds
        } else {
            cachedAds?.filter { it.adsCategory == category }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun searchAds(key: String) {
        launch {
            searchQueryChannel.send(key)
        }
    }
}