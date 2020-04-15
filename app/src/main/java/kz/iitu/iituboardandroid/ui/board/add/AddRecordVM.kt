package kz.iitu.iituboardandroid.ui.board.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository

class AddRecordVM(private val repository: BoardRepository) : BaseVM() {
    val title = MutableLiveData("")
    val description = MutableLiveData("")
    val whatsApp = MutableLiveData("")
    val instagram = MutableLiveData("")
    val telegram = MutableLiveData("")
    val vk = MutableLiveData("")
    val email = MutableLiveData("")
    val phoneNumber = MutableLiveData("")

    fun onAddRecordClick() {
        if (validateFields()) {
            //todo
            Log.d("AddRecordVM", "send request")
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