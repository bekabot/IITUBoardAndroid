package kz.iitu.iituboardandroid.ui.restore

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.ui.BaseVM

class RestorePasswordVM(private val repository: RemoteDataSource) : BaseVM() {
    val emailText = MutableLiveData<String>()

    fun restore() {
        if (emailText.value?.isEmpty() != false
            || emailText.value?.endsWith("@iitu.kz") == false
            || (emailText.value?.endsWith("@iitu.kz") == true && emailText.value?.length ?: 0 <= 8)
        ) {
            showMessage.value = "Введите адрес почты IITU"
            return
        }

        sendRestorePassRequest()
    }

    private fun sendRestorePassRequest() {
        launchLoadingCoroutine(mainBlock = {
            closeKeyboard.value = true
            val result =
                repository.sendRestorePasswordRequest(
                    emailText.value ?: ""
                )
            showMessage.value = result.message?.let {
                when (result.message) {
                    "USER_NOT_ACTIV" -> "Почта не подтверждена"
                    "PASSWORD_SENT" -> "Новый пароль отправлен на почту"
                    "MAIL_NOT_FOUND" -> "Пользователь с такой почтой не найден"
                    else -> "Произошла ошибка. Попробуйте еще раз."
                }

            } ?: "Произошла ошибка. Попробуйте еще раз."
        })
    }
}