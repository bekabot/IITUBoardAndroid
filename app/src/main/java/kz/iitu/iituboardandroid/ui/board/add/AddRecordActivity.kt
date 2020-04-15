package kz.iitu.iituboardandroid.ui.board.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.Observer
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.convertToStringBytes
import kz.iitu.iituboardandroid.databinding.ActivityAddRecordBinding
import kz.iitu.iituboardandroid.ui.BaseActivity
import kz.iitu.iituboardandroid.utils.FileUtil
import kz.iitu.iituboardandroid.utils.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddRecordActivity : BaseActivity() {

    private val attachedFileInfo1: ConstraintLayout by bind(R.id.attached_file_1)
    private val attachedFileInfo2: ConstraintLayout by bind(R.id.attached_file_2)
    private val attachedFileInfo3: ConstraintLayout by bind(R.id.attached_file_3)

    private val attachedFileDescription1: TextView by bind(R.id.file_description_1)
    private val attachedFileDescription2: TextView by bind(R.id.file_description_2)
    private val attachedFileDescription3: TextView by bind(R.id.file_description_3)

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

        binding.removeFile1.setOnClickListener {
            chosenImagePickerID = -1
            attachedFileInfo1.visibility = View.GONE
            vm.imageFile1 = null
        }

        binding.pickImage2.setOnClickListener {
            chosenImagePickerID = R.id.pick_image_2
            checkReadStoragePermission()
        }

        binding.removeFile2.setOnClickListener {
            chosenImagePickerID = -1
            attachedFileInfo2.visibility = View.GONE
            vm.imageFile2 = null
        }

        binding.pickImage3.setOnClickListener {
            chosenImagePickerID = R.id.pick_image_3
            checkReadStoragePermission()
        }

        binding.removeFile3.setOnClickListener {
            chosenImagePickerID = -1
            attachedFileInfo3.visibility = View.GONE
            vm.imageFile3 = null
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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.OPEN_IMAGE_PICKER_REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Constants.OPEN_IMAGE_PICKER_REQUEST_CODE -> {
                        data?.data?.let { uri ->
                            val document = DocumentFile.fromSingleUri(this, uri)
                            val fileName = document?.name
                            val fileSize = document?.length()
                            val maxFileSize = 10485760
                            fileName?.let {
                                fileSize?.let { size ->
                                    when {
                                        size.toInt() == 0 ->
                                            showTextAlert(getString(R.string.file_not_found))
                                        size >= maxFileSize -> {
                                            showTextAlert(getString(R.string.file_too_large))
                                            removeAttachedFile()
                                        }
                                        else -> {
                                            val sizeInfo = "(${size.toDouble()
                                                .convertToStringBytes(this)})"
                                            setUpSelectedFile("$fileName $sizeInfo", uri)
                                        }
                                    }
                                }
                            } ?: run {
                                showTextAlert(getString(R.string.file_not_found))
                                removeAttachedFile()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUpSelectedFile(info: String, uri: Uri) {
        val path = FileUtil.getPath(this, uri)
        val file = path?.let {
            File(path)
        } ?: run {
            showTextAlert(getString(R.string.file_not_found))
            removeAttachedFile()
            return
        }

        when (chosenImagePickerID) {
            R.id.pick_image_1 -> {
                attachedFileInfo1.visibility = View.VISIBLE
                attachedFileDescription1.text = info
                vm.imageFile1 = file
            }
            R.id.pick_image_2 -> {
                attachedFileInfo2.visibility = View.VISIBLE
                attachedFileDescription2.text = info
                vm.imageFile2 = file
            }
            R.id.pick_image_3 -> {
                attachedFileInfo3.visibility = View.VISIBLE
                attachedFileDescription3.text = info
                vm.imageFile3 = file
            }
        }
    }

    private fun removeAttachedFile() {
        when (chosenImagePickerID) {
            R.id.pick_image_1 -> {
                attachedFileInfo1.visibility = View.GONE
                vm.imageFile1 = null
            }
            R.id.pick_image_2 -> {
                attachedFileInfo2.visibility = View.GONE
                vm.imageFile2 = null
            }
            R.id.pick_image_3 -> {
                attachedFileInfo3.visibility = View.GONE
                vm.imageFile3 = null
            }
        }
    }

    override fun onBackPressed() {
        navigateBack()
    }
}