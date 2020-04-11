package kz.iitu.iituboardandroid.ui.record

import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository

class RecordVM(private val repository: BoardRepository) : BaseVM() {
    fun setUpRecordInfo(recordId: Int) {
        launchLoadingCoroutine(mainBlock = {
            val userInfo = repository.getUserInfo()
            val token = userInfo?.token ?: ""
            val record = repository.getRecordById(token, recordId)
            if (record?.message == "RECORD_NOT_FOUND") {
                //todo handle record not found
            } else {
                //todo show record data
            }
        })
    }
}