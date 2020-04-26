package kz.iitu.iituboardandroid.ui.board.ads

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.RecordsAdapter
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.BaseActivity
import kz.iitu.iituboardandroid.ui.record.RecordActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdsFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private val vm: AdsVM by viewModel()

    private var searchView: AppCompatEditText? = null
    private var searchTextWatcher: TextWatcher? = null

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
        fun openDrawer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ads, container, false)

        listener?.setTitle("Объявления")

        searchView = view.findViewById(R.id.search)

        vm.ads.observe(viewLifecycleOwner, Observer {
            it?.let {
                view.findViewById<RecyclerView>(R.id.recycler).adapter = recordsAdapter
                recordsAdapter.set(it)
            }
        })

        refreshLayout = view.findViewById(R.id.swipe_refresh)
        refreshLayout?.run {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener {
                vm.requestFreshAds()
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

        setUpSearchTextChangedListener()

        vm.searchResult.observe(viewLifecycleOwner, Observer {
            it?.let {
                view.findViewById<RecyclerView>(R.id.recycler).adapter = recordsAdapter
                recordsAdapter.set(it)
            }
        })

        vm.clearInputFields.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity).closeKeyboard()
            view.findViewById<AppCompatEditText>(R.id.search).text?.clear()
        })

        return view
    }

    fun updateAds() {
        vm.setUpAds()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun filterAdsByCategory(category: String) {
        searchView?.removeTextChangedListener(searchTextWatcher)
        searchView?.text?.clear()
        setUpSearchTextChangedListener()
        val imm =
            searchView?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

        vm.filterAdsByCategory(category)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun setUpSearchTextChangedListener() {
        searchTextWatcher = searchView?.doAfterTextChanged {
            it?.let {
                vm.searchAds(it.toString())
            }
        }
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
            listener?.setTitle("Объявления")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_drawer, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_open_drawer -> {
            listener?.openDrawer()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        const val FRAG_TAG = "ads_fragment"

        fun newInstance() = AdsFragment()
    }
}