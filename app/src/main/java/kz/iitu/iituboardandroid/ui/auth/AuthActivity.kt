package kz.iitu.iituboardandroid.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.databinding.ActivityAuthBinding
import kz.iitu.iituboardandroid.ui.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val vm: AuthVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        binding.viewModel = vm

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