package kz.iitu.iituboardandroid.ui.board.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import kz.iitu.iituboardandroid.Constants
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private val vm: ProfileVM by viewModel()

    private lateinit var binding: FragmentProfileBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.viewModel = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setTitle("Профиль")

        binding.logout.setOnClickListener {
            listener?.logout()
        }

        vm.userRecords.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.icOpenNews.visibility = View.VISIBLE
                binding.myRecordsTitle.visibility = View.VISIBLE
                binding.myRecordsTitle.text = "Мои записи (${it.size})"
            }
        })

        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        binding.adsSwitcher.isChecked = prefs.getBoolean(Constants.ADS_NOTIFF_ALLOWED, true)
        binding.adsSwitcher.setOnCheckedChangeListener { _, isChecked ->
            PreferenceManager.getDefaultSharedPreferences(activity!!)
                .edit()
                .putBoolean(Constants.ADS_NOTIFF_ALLOWED, isChecked)
                .apply()
        }

        binding.newsSwitcher.isChecked = prefs.getBoolean(Constants.NEWS_NOTIFF_ALLOWED, true)
        binding.newsSwitcher.setOnCheckedChangeListener { _, isChecked ->
            PreferenceManager.getDefaultSharedPreferences(activity!!)
                .edit()
                .putBoolean(Constants.NEWS_NOTIFF_ALLOWED, isChecked)
                .apply()
        }

        binding.vacanciesSwitcher.isChecked = prefs.getBoolean(Constants.VACANCIED_NOTIFF_ALLOWED, true)
        binding.vacanciesSwitcher.setOnCheckedChangeListener { _, isChecked ->
            PreferenceManager.getDefaultSharedPreferences(activity!!)
                .edit()
                .putBoolean(Constants.VACANCIED_NOTIFF_ALLOWED, isChecked)
                .apply()
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
            listener?.setTitle("Профиль")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun updateProfile() {
        vm.updateProfile()
    }

    companion object {
        const val FRAG_TAG = "profile_fragment"

        fun newInstance() = ProfileFragment()
    }
}