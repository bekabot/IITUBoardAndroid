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
}