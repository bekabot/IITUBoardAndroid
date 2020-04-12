package kz.iitu.iituboardandroid.ui.record

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.databinding.ActivityRecordBinding
import kz.iitu.iituboardandroid.ui.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecordActivity : BaseActivity() {

    private lateinit var binding: ActivityRecordBinding

    private val vm: RecordVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record)
        binding.viewModel = vm
        binding.lifecycleOwner = this

        val recordId = intent.getIntExtra(Constants.EXTRA_RECORD_ID, -1)
        vm.setUpRecordInfo(recordId)

        vm.recordNotFound.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Запись не найдена", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
}