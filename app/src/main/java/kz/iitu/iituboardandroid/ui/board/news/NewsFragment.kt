package kz.iitu.iituboardandroid.ui.board.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.RecordsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private val vm: NewsVM by viewModel()

    interface OnFragmentInteractionListener {
        fun setTitle(title: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        listener?.setTitle("Новости")

        vm.news.observe(viewLifecycleOwner, Observer {
            it?.let {
                val recordsAdapter = RecordsAdapter()
                view.findViewById<RecyclerView>(R.id.recycler).adapter = recordsAdapter
                recordsAdapter.set(it)
            }
        })

        return view
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