package kz.iitu.iituboardandroid.ui.auth

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.sha256
import kz.iitu.iituboardandroid.ui.BaseVM

class AuthVM(private val repository: RemoteDataSource) : BaseVM() {
    val nameText = MutableLiveData<String>()
    val surnameText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val confirmationPasswordText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()

    fun auth() {

        if (nameText.value?.isEmpty() != false) {
            showMessage.value = "Введите имя"
            return
        }

        if (surnameText.value?.isEmpty() != false) {
            showMessage.value = "Введите фамилию"
            return
        }

        if (emailText.value?.isEmpty() != false
            || emailText.value?.endsWith("@iitu.kz") == false
            || (emailText.value?.endsWith("@iitu.kz") == true && emailText.value?.length ?: 0 <= 8)
        ) {
            showMessage.value = "Введите адрес почты IITU"
            return
        }

        if (passwordText.value?.isEmpty() != false) {
            showMessage.value = "Введите пароль"
            return
        }

        if (passwordText.value?.length ?: 0 < 7) {
            showMessage.value = "Пароль слишком слабый"
            return
        }

        if (passwordText.value != confirmationPasswordText.value) {
            showMessage.value = "Пароли не совпадают"
            return
        }

        sendAuthRequest()
    }

    private fun sendAuthRequest() {
        launchLoadingCoroutine(mainBlock = {
            closeKeyboard.value = true
            val result =
                repository.sendAuthRequest(
                    nameText.value ?: "",
                    surnameText.value ?: "",
                    passwordText.value?.sha256() ?: "",
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