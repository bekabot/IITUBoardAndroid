package kz.iitu.iituboardandroid.ui.board.add

import android.util.Log
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

    fun onAddRecordClick() {
        if (validateFields()) {
            Log.d("AddRecordVM", "imageFile1 is " + imageFile1?.name)
            Log.d("AddRecordVM", "imageFile2 is " + imageFile2?.name)
            Log.d("AddRecordVM", "imageFile3 is " + imageFile3?.name)
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