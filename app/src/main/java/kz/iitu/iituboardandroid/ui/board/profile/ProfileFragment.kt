package kz.iitu.iituboardandroid.ui.board.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kz.iitu.iituboardandroid.R

class ProfileFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    interface OnFragmentInteractionListener {
        fun setTitle(title: String)
        fun logout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.findViewById<TextView>(R.id.logout).setOnClickListener {
            listener?.logout()
        }

        listener?.setTitle("Профиль")

        return view
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
            listener?.setTitle("Профиль")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        const val FRAG_TAG = "profile_fragment"

        fun newInstance() = ProfileFragment()
    }
}