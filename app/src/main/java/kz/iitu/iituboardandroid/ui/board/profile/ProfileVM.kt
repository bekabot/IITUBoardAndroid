package kz.iitu.iituboardandroid.ui.board.profile

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.BaseVM
import kz.iitu.iituboardandroid.ui.board.BoardRepository

class ProfileVM(val repository: BoardRepository) : BaseVM() {

    val userName = MutableLiveData("")
    val userEmail = MutableLiveData("")
    val userRecords = MutableLiveData<List<Record>?>(emptyList())

    fun updateProfile() {
        val userData = repository.getUserInfo()
        userData?.let {
            userName.value = "${it.name} ${it.surname}"
            userEmail.value = it.email
        }

        userRecords.value = repository.getUserRecords()
    }
}