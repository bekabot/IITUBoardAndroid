package kz.iitu.iituboardandroid.ui.board.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.RecordsAdapter
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.BaseActivity
import kz.iitu.iituboardandroid.ui.record.RecordActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
//todo reset search when chip is clicked while searching
//todo push did not come on old device
class NewsFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private val vm: NewsVM by viewModel()

    private var refreshLayout: SwipeRefreshLayout? = null

    private val recordsAdapter =
        RecordsAdapter(object : RecordsAdapter.OnProfileInteraction {
            override fun onRecordClick(item: Record) {
                startActivity(Intent(activity!!, RecordActivity::class.java).apply {
                    putExtra(Constants.EXTRA_RECORD_ID, item.id)
                })
            }
        })

    interface OnFragmentInteractionListener {
        fun setTitle(title: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        listener?.setTitle("Новости")

        vm.news.observe(viewLifecycleOwner, Observer {
            it?.let {
                view.findViewById<RecyclerView>(R.id.recycler).adapter = recordsAdapter
                recordsAdapter.set(it)
            }
        })

        refreshLayout = view.findViewById(R.id.swipe_refresh)
        refreshLayout?.run {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener {
                vm.requestFreshNews()
            }
        }

        vm.isLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!it) refreshLayout?.isRefreshing = false
            }
        })

        vm.logout.observe(viewLifecycleOwner, Observer {
            if (it) {
                (activity as BaseActivity?)?.logout()
            }
        })

        vm.showMessage.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        })

        view.findViewById<AppCompatEditText>(R.id.search).doAfterTextChanged {
            it?.let {
                vm.searchNews(it.toString())
                resetChipGroup(view)
            }
        }

        vm.searchResult.observe(viewLifecycleOwner, Observer {
            it?.let {
                view.findViewById<RecyclerView>(R.id.recycler).adapter = recordsAdapter
                recordsAdapter.set(it)
            }
        })

        setChipGroupCheckedListener(view)

        view.findViewById<Chip>(R.id.category_it).setOnCheckedChangeListener { view, ischecked ->
            vm.filterNewsByCategory("it")
        }

        view.findViewById<Chip>(R.id.category_others)
            .setOnCheckedChangeListener { view, ischecked ->
                vm.filterNewsByCategory("others")
            }

        view.findViewById<Chip>(R.id.category_sport).setOnCheckedChangeListener { view, ischecked ->
            vm.filterNewsByCategory("sport")
        }

        view.findViewById<Chip>(R.id.category_study).setOnCheckedChangeListener { view, ischecked ->
            vm.filterNewsByCategory("study")
        }

        vm.clearInputFields.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity).closeKeyboard()
            view.findViewById<AppCompatEditText>(R.id.search).text?.clear()
            resetChipGroup(view)
        })

        return view
    }

    private fun resetChipGroup(view: View) {
        val chipGroup = view.findViewById<ChipGroup>(R.id.category_group)
        chipGroup.setOnCheckedChangeListener(null)
        chipGroup.clearCheck()
        setChipGroupCheckedListener(chipGroup)
    }

    private fun setChipGroupCheckedListener(view: View) {
        view.findViewById<ChipGroup>(R.id.category_group)
            .setOnCheckedChangeListener { view, checkId ->
                if (checkId == View.NO_ID) {
                    vm.setUpNews()
                }
            }
    }

    fun updateNews() {
        vm.setUpNews()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            listener?.setTitle("Новости")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        const val FRAG_TAG = "news_fragment"

        fun newInstance() = NewsFragment()
    }
}