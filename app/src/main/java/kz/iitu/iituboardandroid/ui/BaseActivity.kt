package kz.iitu.iituboardandroid.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.api.NoInternetException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private var alert: AlertDialog? = null

    fun showLoader(parentView: ViewGroup) {
        if (!this.isFinishing) {
            closeKeyboard()
            val isLoading = parentView.findViewById<ViewGroup>(R.id.loader_view) != null
            if (!isLoading) {
                val loaderView =
                    LayoutInflater.from(this).inflate(R.layout.loader_view, parentView, false)
                parentView.addView(loaderView)
            }
        }
    }

    fun hideLoader(parentView: ViewGroup) {
        if (!this.isFinishing) {
            if (parentView.findViewById<ViewGroup>(R.id.loader_view) != null)
                parentView.removeView(parentView.findViewById(R.id.loader_view))
        }
    }

    fun closeKeyboard() {
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun showTextAlert(message: String) {

        if (!this.isFinishing) {
            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setMessage(message)
            alertBuilder.setPositiveButton("OK", null)
            alertBuilder.setCancelable(true)
            alert = alertBuilder.create()
            alert!!.show()
        }
    }

    fun showErrorMessageBy(error: Throwable?) {
        if (!isFinishing) {
            error?.let {
                when (error) {
                    is SocketTimeoutException, is TimeoutException, is ConnectException, is HttpException -> {
                        showTextAlert("Ошибка сервера, попробуйте позднее")
                        return
                    }
                    is UnknownHostException, is NoInternetException -> {
                        showTextAlert("Нет соединения с интернетом")
                        return
                    }
                    else -> {
                        showTextAlert("Системная ошибка")
                        return
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        alert?.dismiss()
    }
}