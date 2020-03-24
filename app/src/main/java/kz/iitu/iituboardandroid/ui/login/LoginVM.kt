package kz.iitu.iituboardandroid.ui.login

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.sha256
import kz.iitu.iituboardandroid.ui.BaseVM

class LoginVM(private val repository: RemoteDataSource) : BaseVM() {

    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()

    var shouldRememberLogin = false

    fun login() {
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

        sendLoginRequest()
    }

    private fun sendLoginRequest() {
        launchLoadingCoroutine(mainBlock = {
            closeKeyboard.value = true
            val result =
                repository.sendLoginRequest(
                    passwordText.value?.sha256() ?: "",
                    emailText.value ?: ""
                )

            result.error?.let {
                showMessage.value = when (it) {
                    "THIS_USER_NOT_FOUND" -> "Пользователь не найден"
                    else -> "Произошла ошибка. Попробуйте еще раз"
                }
            } ?: run {
                result.token?.let {
                    if (shouldRememberLogin) {
                        //todo save user info in shared prefs
                        showMessage.value = "LOGIN SUCCESS"
                    }
                } ?: run {
                    showMessage.value = "Произошла ошибка. Попробуйте еще раз"
                }
            }
        })
    }
}