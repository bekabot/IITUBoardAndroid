package kz.iitu.iituboardandroid.ui.restore

import androidx.lifecycle.MutableLiveData
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.ui.BaseVM
import timber.log.Timber

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
        Timber.w(emailText.value)
    }
}