package kz.iitu.iituboardandroid.ui.board.news

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository

class NewsVM(private val repository: BoardRepository) : BaseVM() {
    val news = MutableLiveData<List<Record>?>()

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
}