package kz.iitu.iituboardandroid.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class BaseVM : ViewModel(), CoroutineScope {
    val isLoading = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Throwable>()
    val showMessage = MutableLiveData<String>()

    val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun launchLoadingCoroutine(
        mainBlock: suspend () -> Unit,
        errorBlock: ((Throwable) -> Unit)? = null
    ) {
        launch {
            try {
                isLoading.value = true
                mainBlock.invoke()
            } catch (e: Exception) {
                errorBlock?.invoke(e) ?: run {
                    isError.value = e
                }
            } finally {
                isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}