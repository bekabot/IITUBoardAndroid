package kz.iitu.iituboardandroid.ui.board.my_records

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository

class MyRecordsVM(private val repository: BoardRepository) : BaseVM() {
    val records = MutableLiveData<List<Record>?>(emptyList())

    init {
        records.value = repository.getUserRecords()
    }
}