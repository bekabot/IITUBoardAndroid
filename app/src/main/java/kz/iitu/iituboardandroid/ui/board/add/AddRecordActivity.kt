package kz.iitu.iituboardandroid.ui.board.add

import android.Manifest
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.databinding.ActivityAddRecordBinding
import kz.iitu.iituboardandroid.ui.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddRecordActivity : BaseActivity() {

    private lateinit var binding: ActivityAddRecordBinding
    private val vm: AddRecordVM by viewModel()

    private var chosenImagePickerID = -1

    private var permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            pickImage()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_record)
        binding.viewModel = vm
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)

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
            if (it) {
                closeKeyboard()
            }
        })

        vm.isError.observe(this, Observer {
            showErrorMessageBy(it)
        })

        binding.pickImage1.setOnClickListener {
            chosenImagePickerID = R.id.pick_image_1
            checkReadStoragePermission()
        }

        binding.pickImage2.setOnClickListener {
            chosenImagePickerID = R.id.pick_image_2
            checkReadStoragePermission()
        }

        binding.pickImage3.setOnClickListener {
            chosenImagePickerID = R.id.pick_image_3
            checkReadStoragePermission()
        }
    }

    private fun checkReadStoragePermission() {
        if (TedPermission.isGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            pickImage()
        } else {
            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Разрешение не предоставлено")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setDeniedCloseButtonText(R.string.title_close)
                .setGotoSettingButtonText(R.string.title_settings)
                .check()
        }
    }

    private fun pickImage() {

    }

    override fun onOptionsItemSelected(item: MenuItem?) =
        when (item!!.itemId) {
            android.R.id.home -> {
                navigateBack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun navigateBack() {
        val fragmentCount = supportFragmentManager.backStackEntryCount
        if (fragmentCount == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onBackPressed() {
        navigateBack()
    }
}