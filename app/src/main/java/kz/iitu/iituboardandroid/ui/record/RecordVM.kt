package kz.iitu.iituboardandroid.ui.record

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.response.Record
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
    val images = MutableLiveData<List<String>>()
    val author = MutableLiveData("")

    val callNumber = MutableLiveData<String>("")
    val writeToEmail = MutableLiveData<String>("")
    val openVK = MutableLiveData<String>("")
    val openTelegram = MutableLiveData<String>("")
    val openWhatsApp = MutableLiveData<String>("")
    val openInstagram = MutableLiveData<String>("")

    var record: Record? = null

    fun setUpRecordInfo(recordId: Int) {
        launchLoadingCoroutine(mainBlock = {
            val userInfo = repository.getUserInfo()
            val token = userInfo?.token ?: ""
            val record = repository.getRecordById(token, recordId)
            if (record?.message == "RECORD_NOT_FOUND") {
                recordNotFound.value = true
            } else {
                images.value = listOfNotNull(
                    record?.record?.image1,
                    record?.record?.image2,
                    record?.record?.image3
                )
                setUpRecordFields(record)
                this.record = record?.record
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
        record?.record?.author?.let {
            author.value = "От $it - ${record.record.getPrintableCreationDate()}"
        }
    }

    fun onPhoneClick() {
        callNumber.value = record?.phone
    }

    fun onWhatsAppClick() {
        openWhatsApp.value = record?.whatsapp
    }

    fun onEmailClick() {
        writeToEmail.value = record?.email
    }

    fun onInstagramClick() {
        openInstagram.value = record?.instagram
    }

    fun onTelegramClick() {
        openTelegram.value = record?.telegram
    }

    fun onVKClick() {
        openVK.value = record?.vk
    }
}