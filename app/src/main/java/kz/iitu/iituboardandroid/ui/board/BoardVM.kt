package kz.iitu.iituboardandroid.ui.board

import kz.iitu.iituboardandroid.ui.BaseVM

class BoardVM(private val repository: BoardRepository) : BaseVM() {
    init {
        launchLoadingCoroutine(mainBlock = {
            val userInfo = repository.getUserInfo()
            val result = repository.getAllRecords(userInfo?.token ?: "")
            when (result.message) {
                null -> {
                    //todo show info
                }
                "USER_NOT_ACTIV", "USER_NOT_FOUND" -> {
                    //logout
                }
            }
        })
    }
}