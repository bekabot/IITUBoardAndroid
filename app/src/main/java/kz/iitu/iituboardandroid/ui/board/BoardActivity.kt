package kz.iitu.iituboardandroid.ui.board

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.ui.BaseActivity
import kz.iitu.iituboardandroid.ui.board.add.AddRecordActivity
import kz.iitu.iituboardandroid.ui.board.ads.AdsFragment
import kz.iitu.iituboardandroid.ui.board.news.NewsFragment
import kz.iitu.iituboardandroid.ui.board.profile.ProfileFragment
import kz.iitu.iituboardandroid.ui.board.vacancies.VacanciesFragment
import kz.iitu.iituboardandroid.utils.ScrollableBottomNavView
import kz.iitu.iituboardandroid.utils.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class BoardActivity : BaseActivity(), NewsFragment.OnFragmentInteractionListener,
    AdsFragment.OnFragmentInteractionListener,
    ProfileFragment.OnFragmentInteractionListener, VacanciesFragment.OnFragmentInteractionListener {

    private val bottomNavigation: ScrollableBottomNavView by bind(R.id.bottom_nav)
    private val toolbar: Toolbar by bind(R.id.toolbar)
    private val rootView: FrameLayout by bind(R.id.root_view)

    private val vm: BoardVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        setSupportActionBar(toolbar)

        bottomNavigation.itemIconTintList = null

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_news -> {
                    showFragmentBy(tag = NewsFragment.FRAG_TAG)
                }
                R.id.action_vacancies -> {
                    showFragmentBy(tag = VacanciesFragment.FRAG_TAG)
                }
                R.id.action_add -> {
                    startActivity(Intent(this, AddRecordActivity::class.java))
                    //todo add refresh board if added successfully
                }
                R.id.action_ads -> {
                    showFragmentBy(tag = AdsFragment.FRAG_TAG)
                }
                R.id.action_profile -> {
                    showFragmentBy(tag = ProfileFragment.FRAG_TAG)
                }
            }
            true
        }

        createFragments()

        vm.showMessage.observe(this, Observer {
            showTextAlert(it)
        })

        vm.isLoading.observe(this, Observer {
            it?.let {
                if (it) {
                    showLoader(rootView)
                } else {
                    hideLoader(rootView)
                }
            }
        })

        vm.closeKeyboard.observe(this, Observer {
            if (it) {
                closeKeyboard()
            }
        })

        vm.isError.observe(this, Observer {
            showErrorMessageBy(it)
        })

        vm.updateRecords.observe(this, Observer {
            if (it) {
                supportFragmentManager.findFragmentByTag(VacanciesFragment.FRAG_TAG)
                    ?.let { fragment ->
                        (fragment as VacanciesFragment).updateVacancies()
                    }

                supportFragmentManager.findFragmentByTag(NewsFragment.FRAG_TAG)
                    ?.let { fragment ->
                        (fragment as NewsFragment).updateNews()
                    }

                supportFragmentManager.findFragmentByTag(AdsFragment.FRAG_TAG)
                    ?.let { fragment ->
                        (fragment as AdsFragment).updateAds()
                    }
            }
        })

        vm.logout.observe(this, Observer {
            if (it) {
                logout()
            }
        })
    }

    private fun createFragments() {
        val newsFragment = NewsFragment.newInstance()
        val vacanciesFragment = VacanciesFragment.newInstance()
        val adsFragment = AdsFragment.newInstance()
        val profileFragment = ProfileFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, profileFragment, ProfileFragment.FRAG_TAG)
            .hide(profileFragment)
            .add(R.id.fragment_container, vacanciesFragment, VacanciesFragment.FRAG_TAG)
            .hide(vacanciesFragment)
            .add(R.id.fragment_container, adsFragment, AdsFragment.FRAG_TAG)
            .hide(adsFragment)
            .add(R.id.fragment_container, newsFragment, NewsFragment.FRAG_TAG)
            .commitAllowingStateLoss()
    }

    private fun showFragmentBy(tag: String) {
        supportFragmentManager.findFragmentByTag(tag)?.let { fragment ->
            if (fragment.isAdded) {
                val fragTransaction = supportFragmentManager.beginTransaction()
                fragTransaction.show(fragment)

                when (tag) {
                    NewsFragment.FRAG_TAG -> {
                        supportFragmentManager.findFragmentByTag(VacanciesFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(AdsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(ProfileFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                    }
                    VacanciesFragment.FRAG_TAG -> {
                        supportFragmentManager.findFragmentByTag(NewsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(AdsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(ProfileFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                    }
                    AdsFragment.FRAG_TAG -> {
                        supportFragmentManager.findFragmentByTag(NewsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(VacanciesFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(ProfileFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                    }
                    ProfileFragment.FRAG_TAG -> {
                        supportFragmentManager.findFragmentByTag(NewsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(VacanciesFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(AdsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                    }
                }
                fragTransaction.commitAllowingStateLoss()
            }
        }
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }
}