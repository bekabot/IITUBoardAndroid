package kz.iitu.iituboardandroid.ui.record

import android.os.Bundle
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.ui.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecordActivity : BaseActivity() {

    private val vm: RecordVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        val recordId = intent.getIntExtra(Constants.EXTRA_RECORD_ID, -1)
        vm.setUpRecordInfo(recordId)
    }
}