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
}