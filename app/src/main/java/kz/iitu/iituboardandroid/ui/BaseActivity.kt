package kz.iitu.iituboardandroid.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kz.iitu.iituboardandroid.R

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    fun showLoader(parentView: ViewGroup) {
        if (!this.isFinishing) {
            closeKeyboard(this)
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

    fun closeKeyboard(context: Activity) {
        context.currentFocus?.let {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}