package kz.iitu.iituboardandroid.ui.record

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
}