package kz.iitu.iituboardandroid.ui.login

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.databinding.ActivityLoginBinding
import kz.iitu.iituboardandroid.ui.BaseActivity
import kz.iitu.iituboardandroid.ui.auth.AuthActivity
import kz.iitu.iituboardandroid.ui.restore.RestorePasswordActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val vm: LoginVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = vm

        binding.registrationButton.paintFlags =
            binding.registrationButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.registrationButton.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, RestorePasswordActivity::class.java))
        }

        binding.rememberChk.setOnCheckedChangeListener { button, isChecked ->
            vm.shouldRememberLogin = isChecked
        }

        vm.showMessage.observe(this, Observer {
            showTextAlert(it)
        })

        vm.isLoading.observe(this, Observer {
            it?.let {
                if (it) {
                    showLoader(binding.rootView)
                } else {
                    hideLoader(binding.rootView)
                }
            }
        })

        vm.closeKeyboard.observe(this, Observer {
            closeKeyboard()
        })

        vm.isError.observe(this, Observer {
            showErrorMessageBy(it)
        })
    }
}