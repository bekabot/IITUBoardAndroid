package kz.iitu.iituboardandroid.ui.record

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.response.RecordResponse
import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository

class RecordVM(private val repository: BoardRepository) : BaseVM() {
    val title = MutableLiveData("")
    val description = MutableLiveData("")
    val whatsApp = MutableLiveData("")
    val instagram = MutableLiveData("")
    val telegram = MutableLiveData("")
    val vk = MutableLiveData("")
    val email = MutableLiveData("")
    val phoneNumber = MutableLiveData("")
    val recordNotFound = MutableLiveData(false)

    fun setUpRecordInfo(recordId: Int) {
        launchLoadingCoroutine(mainBlock = {
            val userInfo = repository.getUserInfo()
            val token = userInfo?.token ?: ""
            val record = repository.getRecordById(token, recordId)
            if (record?.message == "RECORD_NOT_FOUND") {
                recordNotFound.value = true
            } else {
                setUpRecordFields(record)
            }
        })
    }

    private fun setUpRecordFields(record: RecordResponse?) {
        title.value = record?.record?.record_title
        description.value = record?.record?.record_body
        whatsApp.value = record?.record?.whatsapp
        instagram.value = record?.record?.instagram
        telegram.value = record?.record?.telegram
        vk.value = record?.record?.vk
        email.value = record?.record?.email
        phoneNumber.value = record?.record?.phone
    }
}