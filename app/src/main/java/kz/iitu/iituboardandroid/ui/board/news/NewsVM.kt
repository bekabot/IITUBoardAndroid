package kz.iitu.iituboardandroid.ui.board.news

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

class NewsVM(private val repository: BoardRepository) : BaseVM() {
    val news = MutableLiveData<List<Record>?>()
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
            repository.getCachedNews()
                ?.filter {
                    val keyLowered = key.toLowerCase(Locale.getDefault())
                    it.record_title.toLowerCase(Locale.getDefault()).contains(keyLowered) ||
                            it.record_body.toLowerCase(Locale.getDefault()).contains(keyLowered)
                }
        }
        .asLiveData()

    fun setUpNews() {
        news.value = repository.getCachedNews()
    }

    fun requestFreshNews() {
        val userData = repository.getUserInfo()
        userData?.let {
            launchLoadingCoroutine(mainBlock = {
                val result = repository.getNews(userData.token ?: "")
                when (result.message) {
                    null, "" -> {
                        news.value = result.records
                        clearInputFields.value = true
                        showMessage.value = "Новости обновлены"
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

    fun filterNewsByCategory(category: String) {
        val cachedNews = repository.getCachedNews()
        news.value = cachedNews?.filter { it.ads_category == category }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun searchNews(key: String) {
        launch {
            searchQueryChannel.send(key)
        }
    }
}