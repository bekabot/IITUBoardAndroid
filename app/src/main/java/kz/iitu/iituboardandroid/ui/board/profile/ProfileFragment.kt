package kz.iitu.iituboardandroid.ui.board.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.api.LoginResponse
import kz.iitu.iituboardandroid.ui.login.LoginActivity
import wiki.depasquale.mcache.obtain

class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.findViewById<Button>(R.id.logout).setOnClickListener {
            obtain<LoginResponse>().build().delete()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            activity?.startActivity(intent)
        }
        return view
    }

    companion object {
        const val FRAG_TAG = "profile_fragment"

        fun newInstance() = ProfileFragment()
    }
}