package kz.iitu.iituboardandroid.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.iitu.iituboardandroid.App
import kotlin.coroutines.CoroutineContext

open class BaseVM : ViewModel(), CoroutineScope {
    val isLoading = MutableLiveData<Boolean?>(null)
    val isError = MutableLiveData<Throwable>()
    val showMessage = MutableLiveData<String>()
    val closeKeyboard = MutableLiveData<Boolean>()
    val logout = MutableLiveData(false)

    private val job = Job()

    protected val context: Context
        get() = App.getInstance().applicationContext

    protected val prefs: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

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