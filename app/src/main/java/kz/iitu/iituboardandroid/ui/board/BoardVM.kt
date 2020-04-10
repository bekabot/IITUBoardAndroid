package kz.iitu.iituboardandroid.ui.board

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.ui.BaseVM

class BoardVM(private val repository: BoardRepository) : BaseVM() {

    val updateRecords = MutableLiveData(false)

    init {
        launchLoadingCoroutine(mainBlock = {
            val userInfo = repository.getUserInfo()
            val result = repository.getAllRecords(userInfo?.token ?: "")
            when (result.message) {
                null -> {
                    updateRecords.value = true
                }
                "USER_NOT_ACTIV", "USER_NOT_FOUND" -> {
                    logout.value = true
                }
            }
        })
    }
}