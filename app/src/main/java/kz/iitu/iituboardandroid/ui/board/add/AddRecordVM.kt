package kz.iitu.iituboardandroid.ui.board.add

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
}