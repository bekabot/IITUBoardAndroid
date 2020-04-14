package kz.iitu.iituboardandroid.ui.board.vacancies

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository

class VacanciesVM(private val repository: BoardRepository) : BaseVM() {
    val vacancies = MutableLiveData<List<Record>?>()

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
}