package kz.iitu.iituboardandroid.ui.board

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.api.LoginResponse
import kz.iitu.iituboardandroid.ui.BaseVM
import wiki.depasquale.mcache.obtain

class BoardVM(private val repository: BoardRepository) : BaseVM() {

    val updateRecords = MutableLiveData(false)

    init {

        val userData = repository.getUserInfo()
        userData?.let {
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

    override fun onCleared() {
        super.onCleared()
        if (!prefs.getBoolean(Constants.REMEMBER_ME, false)) {
            obtain<LoginResponse>().build().delete()
        }
    }
}