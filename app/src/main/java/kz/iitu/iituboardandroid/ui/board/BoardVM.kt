package kz.iitu.iituboardandroid.ui.board

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.ui.BaseVM

class BoardVM(private val repository: BoardRepository) : BaseVM() {

    val updateRecords = MutableLiveData(false)

    init {

        val userData = repository.getUserInfo()
        userData?.let {
            if (!prefs.getBoolean(Constants.REMEMBER_ME, false)) {
                moveToLogin.value = true //todo handle cases when remember not ticked
            }

            launchLoadingCoroutine(mainBlock = {
                val result = repository.getAllRecords(userData.token ?: "")
                when (result.message) {
                    null -> {
                        updateRecords.value = true
                    }
                    "USER_NOT_ACTIV", "USER_NOT_FOUND" -> {
                        logout.value = true
                    }
                }
            })
        } ?: run {
            moveToLogin.value = true
        }
    }
}