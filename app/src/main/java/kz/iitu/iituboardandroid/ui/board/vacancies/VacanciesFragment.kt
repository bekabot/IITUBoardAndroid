package kz.iitu.iituboardandroid.ui.board.vacancies

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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.RecordsAdapter
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.BaseActivity
import kz.iitu.iituboardandroid.ui.record.RecordActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class VacanciesFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private val vm: VacanciesVM by viewModel()

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
        val view = inflater.inflate(R.layout.fragment_vacancies, container, false)

        vm.vacancies.observe(viewLifecycleOwner, Observer {
            it?.let {
                view.findViewById<RecyclerView>(R.id.recycler).adapter = recordsAdapter
                recordsAdapter.set(it)
            }
        })

        listener?.setTitle("Вакансии")

        refreshLayout = view.findViewById(R.id.swipe_refresh)
        refreshLayout?.run {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener {
                vm.requestFreshVacancies()
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
                vm.searchVacancies(it.toString())
            }
        }

        vm.searchResult.observe(viewLifecycleOwner, Observer {
            it?.let {
                view.findViewById<RecyclerView>(R.id.recycler).adapter = recordsAdapter
                recordsAdapter.set(it)
            }
        })

        return view
    }

    fun updateVacancies() {
        vm.setUpVacancies()
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
            listener?.setTitle("Вакансии")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        const val FRAG_TAG = "vacancies_fragment"

        fun newInstance() = VacanciesFragment()
    }
}