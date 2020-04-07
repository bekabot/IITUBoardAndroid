package kz.iitu.iituboardandroid.ui.board.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kz.iitu.iituboardandroid.R

class AddRecordFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_record, container, false)
    }

    companion object {
        const val FRAG_TAG = "add_record_fragment"

        fun newInstance() = AddRecordFragment()
    }
}