package kz.iitu.iituboardandroid.ui.auth

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.ui.BaseVM

class AuthVM(private val repository: RemoteDataSource) : BaseVM() {
    val loginText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()

    fun login() {

        if (loginText.value?.isEmpty() == true) {
            showMessage.value = "Введите логин"
            return
        }

        if (passwordText.value?.isEmpty() == true) {
            showMessage.value = "Введите пароль"
            return
        }

        if (passwordText.value?.length ?: 0 < 7) {
            showMessage.value = "Пароль слишком слабый"
            return
        }

        if (emailText.value?.isEmpty() == true
            || emailText.value?.endsWith("@iitu.kz") == false
            || (emailText.value?.endsWith("@iitu.kz") == true && emailText.value?.length ?: 0 <= 8)
        ) {
            showMessage.value = "Введите адрес почты IITU"
            return
        }

        sendAuthRequest()
    }

    private fun sendAuthRequest() {
        launchLoadingCoroutine(mainBlock = {
            closeKeyboard.value = true
            val result =
                repository.sendAuthRequest(
                    loginText.value ?: "",
                    passwordText.value ?: "",
                    emailText.value ?: ""
                )
            showMessage.value = result.message?.let {
                when (result.message) {
                    "MAIL_SENT" -> "Запрос принят. Ожидайте письмо на почту"
                    "WRONG_EMAIL" -> "Неверный формат почты"
                    "USER_ALREADY_EXISTS" -> "Пользователь уже существует"
                    else -> "Произошла ошибка. Попробуйте еще раз."
                }

            } ?: "Произошла ошибка. Попробуйте еще раз."
        })
    }
}