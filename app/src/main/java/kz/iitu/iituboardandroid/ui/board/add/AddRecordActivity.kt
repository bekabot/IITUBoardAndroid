package kz.iitu.iituboardandroid.ui.board.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.Observer
import com.google.android.gms.common.util.IOUtils
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.databinding.ActivityAddRecordBinding
import kz.iitu.iituboardandroid.getFileName
import kz.iitu.iituboardandroid.getPhoneNumber
import kz.iitu.iituboardandroid.ui.BaseActivity
import kz.iitu.iituboardandroid.utils.FileUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

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

        vm.recordCreated.observe(this, Observer {
            if (it) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        vm.logout.observe(this, Observer {
            if (it) {
                logout()
            }
        })

        binding.categories.setOnClickListener {
            showAdsCategoriesMenu()
        }

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

        binding.typeAds.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vm.recordType.value = "ads"
                binding.categories.visibility = View.VISIBLE
            } else {
                binding.categories.visibility = View.GONE
            }
        }

        binding.typeVacancy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vm.recordType.value = "vacancy"
            }
        }

        val phoneTextChangedListener = MaskedTextChangedListener(
            Constants.NUMBER_MASK,
            binding.phoneField,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean, extractedValue: String,
                    formattedValue: String
                ) {
                    if (maskFilled) {
                        closeKeyboard()
                        vm.phoneNumber.value = extractedValue.getPhoneNumber()
                    } else {
                        vm.phoneNumber.value = ""
                    }
                }
            }
        )
        binding.phoneField.addTextChangedListener(phoneTextChangedListener)

        val whatsAppTextChangedListener = MaskedTextChangedListener(
            Constants.NUMBER_MASK,
            binding.whatsappField,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean, extractedValue: String,
                    formattedValue: String
                ) {
                    if (maskFilled) {
                        closeKeyboard()
                        vm.whatsApp.value = extractedValue.getPhoneNumber()
                    } else {
                        vm.whatsApp.value = ""
                    }
                }
            }
        )
        binding.whatsappField.addTextChangedListener(whatsAppTextChangedListener)
    }

    private fun showAdsCategoriesMenu() {
        val adsCategories = arrayOf(
            "Услуги", "Учеба", "Бюро находок", "Спорт", "Хобби",
            "Продам", "Обмен/Отдам даром", "Аренда жилья", "Поиск соседа"
        )

        PopupMenu(this, binding.categories).apply {
            menuInflater.inflate(R.menu.ads_categories_menu, menu)
            setOnMenuItemClickListener { item ->
                val title = item.title
                vm.adsCategory.value = adsCategories.indexOf(title)
                binding.categories.text = title
                true
            }
        }.show()
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
        startActivityForResult(intent, Constants.REQUEST_CODE_OPEN_IMAGE_PICKER)
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
                    Constants.REQUEST_CODE_OPEN_IMAGE_PICKER -> {
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
                                        }
                                        else -> {
                                            setUpSelectedFile(uri)
                                        }
                                    }
                                }
                            } ?: run {
                                showTextAlert(getString(R.string.file_not_found))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUpSelectedFile(uri: Uri) {
        when (chosenImagePickerID) {
            R.id.pick_image_1 -> {
                binding.pickImage1.setImageURI(uri)
                vm.imageFile1 = getFileFromUri(uri)
                vm.imageName1 = contentResolver.getFileName(uri)
            }
            R.id.pick_image_2 -> {
                binding.pickImage2.setImageURI(uri)
                vm.imageFile2 = getFileFromUri(uri)
                vm.imageName2 = contentResolver.getFileName(uri)
            }
            R.id.pick_image_3 -> {
                binding.pickImage3.setImageURI(uri)
                vm.imageFile3 = getFileFromUri(uri)
                vm.imageName3 = contentResolver.getFileName(uri)
            }
        }
    }

    private fun getFileFromUri(uri: Uri) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r", null)
            parcelFileDescriptor?.let {
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(cacheDir, contentResolver.getFileName(uri))
                val outputStream = FileOutputStream(file)
                IOUtils.copyStream(inputStream, outputStream)
                file
            } ?: run {
                showTextAlert(getString(R.string.file_not_found))
                null
            }
        } else {
            val path = FileUtil.getPath(this, uri)
            path?.let {
                File(path)
            } ?: run {
                showTextAlert(getString(R.string.file_not_found))
                null
            }
        }

    override fun onBackPressed() {
        navigateBack()
    }
}