package kz.iitu.iituboardandroid.ui.board.add

import androidx.lifecycle.MutableLiveData
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
                    val result =
                        repository.addRecord(
                            userData.token!!,
                            "test_title",
                            "test_description",
                            imageFile1,
                            imageName1,
                            imageFile2,
                            imageName2,
                            imageFile3,
                            imageName3
                        )
                    when (result.message) {
                        null, "" -> {
                            //todo show success message
                            //todo add refresh board if added successfully
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