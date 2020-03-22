package kz.iitu.iituboardandroid.ui.auth

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.ui.BaseVM

class AuthVM : BaseVM() {
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

        showMessage.value = "Запрос принят. Ожидайте письмо на почту"
    }
}