package kz.iitu.iituboardandroid.ui.board.vacancies

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

class VacanciesVM(private val repository: BoardRepository) : BaseVM() {
    val vacancies = MutableLiveData<List<Record>?>()
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
            repository.getCachedVacancies()
                ?.filter {
                    val keyLowered = key.toLowerCase(Locale.getDefault())
                    it.record_title.toLowerCase(Locale.getDefault()).contains(keyLowered) ||
                            it.record_body.toLowerCase(Locale.getDefault()).contains(keyLowered)
                }
        }
        .asLiveData()

    fun setUpVacancies() {
        vacancies.value = repository.getCachedVacancies()
    }

    fun requestFreshVacancies() {
        val userData = repository.getUserInfo()
        userData?.let {
            launchLoadingCoroutine(mainBlock = {
                val result = repository.getVacancies(userData.token ?: "")
                when (result.message) {
                    null, "" -> {
                        vacancies.value = result.records
                        clearInputFields.value = true
                        showMessage.value = "Вакансии обновлены"
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

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun searchVacancies(key: String) {
        launch {
            searchQueryChannel.send(key)
        }
    }
}