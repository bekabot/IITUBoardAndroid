package kz.iitu.iituboardandroid.ui.board.add

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.AddRecordRequestBody
import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository
import java.io.File

class AddRecordVM(private val repository: BoardRepository) : BaseVM() {
    val title = MutableLiveData("")
    val description = MutableLiveData("")
    val whatsApp = MutableLiveData("")
    val instagram = MutableLiveData("")
    val telegram = MutableLiveData("")
    val vk = MutableLiveData("")
    val email = MutableLiveData("")
    val phoneNumber = MutableLiveData("")
    val recordType = MutableLiveData("ads")
    val recordCreated = MutableLiveData(false)

    var imageFile1: File? = null
    var imageFile2: File? = null
    var imageFile3: File? = null

    var imageName1 = ""
    var imageName2 = ""
    var imageName3 = ""

    fun onAddRecordClick() {
        if (validateFields()) {
            val userData = repository.getUserInfo()
            userData?.let {
                launchLoadingCoroutine(mainBlock = {
                    val requestBody = AddRecordRequestBody(
                        userData.token ?: "",
                        title = title.value ?: "",
                        body = description.value ?: "",
                        phone = phoneNumber.value ?: "",
                        email = email.value ?: "",
                        instagram = instagram.value ?: "",
                        whatsapp = whatsApp.value ?: "",
                        vk = vk.value ?: "",
                        telegram = telegram.value ?: "",
                        record_type = recordType.value ?: "ads",
                        author = "${userData.name} ${userData.surname}",
                        authorEmail = userData.email ?: ""
                    )
                    val result =
                        repository.addRecord(
                            requestBody,
                            imageFile1,
                            imageName1,
                            imageFile2,
                            imageName2,
                            imageFile3,
                            imageName3
                        )
                    when (result.message) {
                        "RECORD_NOT_CREATED" -> showMessage.value =
                            "Произошла ошибка. Попробуйте еще раз"
                        "RECORD_CREATED" -> recordCreated.value = true
                        "USER_NOT_ACTIV", "USER_NOT_FOUND" -> logout.value = true
                    }
                })
            } ?: run {
                logout.value = true
            }
        }
    }

    private fun validateFields(): Boolean {
        if (title.value?.isEmpty() == true) {
            showMessage.value = "Введите заголовок"
            return false
        }

        if (description.value?.isEmpty() == true) {
            showMessage.value = "Введите текст"
            return false
        }

        return true
    }
}