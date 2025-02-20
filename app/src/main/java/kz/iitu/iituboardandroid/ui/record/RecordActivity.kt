package kz.iitu.iituboardandroid.ui.record

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.databinding.ActivityRecordBinding
import kz.iitu.iituboardandroid.ui.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecordActivity : BaseActivity() {

    private lateinit var binding: ActivityRecordBinding

    private val vm: RecordVM by viewModel()

    private var isMyRecord = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record)
        binding.viewModel = vm
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)

        val adapter = CarouselItemAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.onFlingListener = null
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(binding.recyclerView)

        val recordId = intent.getIntExtra(Constants.EXTRA_RECORD_ID, -1)
        vm.setUpRecordInfo(recordId)

        vm.recordNotFound.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Запись не найдена", Toast.LENGTH_SHORT).show()
                finish()
            }
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

        vm.writeToEmail.observe(this, Observer {
            if (it.isNotEmpty()) {
                val emailIntent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:?to=$it"))
                emailIntent.resolveActivity(packageManager)?.let {
                    startActivity(emailIntent)
                }
            }
        })

        vm.callNumber.observe(this, Observer {
            if (it.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + it)
                intent.resolveActivity(packageManager)?.let {
                    startActivity(intent)
                }
            }
        })

        vm.openTelegram.observe(this, Observer {
            if (it.isNotEmpty()) {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/$it")).apply {
                        `package` = "org.telegram.messenger"
                    }
                intent.resolveActivity(packageManager)?.let {
                    startActivity(intent)
                } ?: run {
                    safeOpenBrowser("https://telegram.me/$it")
                }
            }
        })

        vm.openWhatsApp.observe(this, Observer {
            if (it.isNotEmpty()) {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://api.whatsapp.com/send?phone=$it")
                    ).apply {
                        `package` = "com.whatsapp"
                    }
                intent.resolveActivity(packageManager)?.let {
                    startActivity(intent)
                } ?: run {
                    safeOpenBrowser("https://api.whatsapp.com/send?phone=$it")
                }
            }
        })

        vm.logout.observe(this, Observer {
            if (it) {
                logout()
            }
        })

        vm.showMessage.observe(this, Observer {
            showTextAlert(it)
        })

        vm.isRecordMine.observe(this, Observer {
            isMyRecord = it
            invalidateOptionsMenu()
        })

        vm.images.observe(this, Observer {
            if (it.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.recyclerView.setOnClickListener(null)
                binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val layoutManager =
                            binding.recyclerView.layoutManager as LinearLayoutManager?

                        layoutManager?.findFirstCompletelyVisibleItemPosition()?.let {
                            when (it) {
                                0 -> {
                                    binding.dot1.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            binding.dot1.context,
                                            R.drawable.ic_dot_activ
                                        )
                                    )
                                    binding.dot2.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            binding.dot1.context,
                                            R.drawable.ic_dot_inactiv
                                        )
                                    )
                                    binding.dot3.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            binding.dot1.context,
                                            R.drawable.ic_dot_inactiv
                                        )
                                    )
                                }
                                1 -> {
                                    binding.dot1.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            binding.dot1.context,
                                            R.drawable.ic_dot_inactiv
                                        )
                                    )
                                    binding.dot2.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            binding.dot1.context,
                                            R.drawable.ic_dot_activ
                                        )
                                    )
                                    binding.dot3.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            binding.dot1.context,
                                            R.drawable.ic_dot_inactiv
                                        )
                                    )
                                }
                                2 -> {
                                    binding.dot1.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            binding.dot1.context,
                                            R.drawable.ic_dot_inactiv
                                        )
                                    )
                                    binding.dot2.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            binding.dot1.context,
                                            R.drawable.ic_dot_inactiv
                                        )
                                    )
                                    binding.dot3.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            binding.dot1.context,
                                            R.drawable.ic_dot_activ
                                        )
                                    )
                                }
                            }
                        }
                    }
                })

                setUpDotsContainer(it)

                adapter.set(it)
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        if (isMyRecord) {
            menuInflater.inflate(R.menu.my_record_menu, menu)
        } else {
            menuInflater.inflate(R.menu.record_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
        when (item!!.itemId) {
            android.R.id.home -> {
                navigateBack()
                true
            }

            R.id.action_complain -> {
                showComplainDialog()
                true
            }

            R.id.action_delete -> {
                showDeleteConfirmationDialog()
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

    private fun setUpDotsContainer(it: List<String>) {
        binding.dotsContainer.visibility = View.VISIBLE
        when (it.size) {
            2 -> {
                binding.dot1.visibility = View.VISIBLE
                binding.dot2.visibility = View.VISIBLE
            }
            3 -> {
                binding.dot1.visibility = View.VISIBLE
                binding.dot2.visibility = View.VISIBLE
                binding.dot3.visibility = View.VISIBLE
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        showUltimateAlert(
            title = "Вы уверены, что хотите удалить запись?"
            , positiveBtnName = "ДА",
            positiveBtnHandler = DialogInterface.OnClickListener { dialog, which ->
                vm.onDeleteRecord()
            },
            negativeBtnName = "НЕТ"
        )
    }

    private fun showComplainDialog() {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_complaint, null)
        builder.setView(view)
        builder.setCancelable(false)

        val alertDialog = builder.create()
        alertDialog.show()

        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            val imm =
                it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            alertDialog.dismiss()
        }
        view.findViewById<Button>(R.id.send_button).setOnClickListener {
            val imm =
                it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            vm.sendComplaint(view.findViewById<AppCompatEditText>(R.id.text).text.toString())
            alertDialog.dismiss()
        }
    }
}