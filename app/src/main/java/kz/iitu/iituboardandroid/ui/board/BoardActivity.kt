package kz.iitu.iituboardandroid.ui.board

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import kz.iitu.iituboardandroid.R
import kz.iitu.iituboardandroid.ui.BaseActivity
import kz.iitu.iituboardandroid.ui.board.add.AddRecordFragment
import kz.iitu.iituboardandroid.ui.board.ads.AdsFragment
import kz.iitu.iituboardandroid.ui.board.news.NewsFragment
import kz.iitu.iituboardandroid.ui.board.profile.ProfileFragment
import kz.iitu.iituboardandroid.ui.board.vacancies.VacanciesFragment
import kz.iitu.iituboardandroid.utils.ScrollableBottomNavView
import kz.iitu.iituboardandroid.utils.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardActivity : BaseActivity(), NewsFragment.OnFragmentInteractionListener,
    AddRecordFragment.OnFragmentInteractionListener, AdsFragment.OnFragmentInteractionListener,
    ProfileFragment.OnFragmentInteractionListener, VacanciesFragment.OnFragmentInteractionListener {

    private val bottomNavigation: ScrollableBottomNavView by bind(R.id.bottom_nav)
    private val toolbar: Toolbar by bind(R.id.toolbar)
    private val rootView: FrameLayout by bind(R.id.root_view)

    private val vm: BoardVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        setSupportActionBar(toolbar)

        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_news -> {
                    showFragmentBy(tag = NewsFragment.FRAG_TAG)
                    setIcon(item.itemId, R.drawable.ic_news)
                }
                R.id.action_vacancies -> {
                    showFragmentBy(tag = VacanciesFragment.FRAG_TAG)
                    setIcon(R.id.action_vacancies, R.drawable.ic_vacancy)
                }
                R.id.action_add -> {
                    showFragmentBy(tag = AddRecordFragment.FRAG_TAG)
                    setIcon(R.id.action_add, R.drawable.ic_add)
                }
                R.id.action_ads -> {
                    showFragmentBy(tag = AdsFragment.FRAG_TAG)
                    setIcon(R.id.action_ads, R.drawable.ic_ads)
                }
                R.id.action_profile -> {
                    showFragmentBy(tag = ProfileFragment.FRAG_TAG)
                    setIcon(R.id.action_profile, R.drawable.ic_profile)
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

    private fun setIcon(selectedItemId: Int, selectedIcon: Int) {
        bottomNavigation.menu.findItem(R.id.action_news).icon =
            ContextCompat.getDrawable(this, R.drawable.ic_news)
        bottomNavigation.menu.findItem(R.id.action_vacancies).icon =
            ContextCompat.getDrawable(this, R.drawable.ic_vacancy)
        bottomNavigation.menu.findItem(R.id.action_add).icon =
            ContextCompat.getDrawable(this, R.drawable.ic_add)
        bottomNavigation.menu.findItem(R.id.action_ads).icon =
            ContextCompat.getDrawable(this, R.drawable.ic_ads)
        bottomNavigation.menu.findItem(R.id.action_profile).icon =
            ContextCompat.getDrawable(this, R.drawable.ic_profile)
        bottomNavigation.menu.findItem(selectedItemId).icon =
            ContextCompat.getDrawable(this, selectedIcon)
    }

    private fun createFragments() {
        val newsFragment = NewsFragment.newInstance()
        val vacanciesFragment = VacanciesFragment.newInstance()
        val addRecordFragment = AddRecordFragment.newInstance()
        val adsFragment = AdsFragment.newInstance()
        val profileFragment = ProfileFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, profileFragment, ProfileFragment.FRAG_TAG)
            .hide(profileFragment)
            .add(R.id.fragment_container, vacanciesFragment, VacanciesFragment.FRAG_TAG)
            .hide(vacanciesFragment)
            .add(R.id.fragment_container, addRecordFragment, AddRecordFragment.FRAG_TAG)
            .hide(addRecordFragment)
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
                        supportFragmentManager.findFragmentByTag(AddRecordFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(AdsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(ProfileFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                    }
                    VacanciesFragment.FRAG_TAG -> {
                        supportFragmentManager.findFragmentByTag(NewsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(AddRecordFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(AdsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(ProfileFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                    }
                    AddRecordFragment.FRAG_TAG -> {
                        supportFragmentManager.findFragmentByTag(NewsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(VacanciesFragment.FRAG_TAG)
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
                        supportFragmentManager.findFragmentByTag(AddRecordFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(ProfileFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                    }
                    ProfileFragment.FRAG_TAG -> {
                        supportFragmentManager.findFragmentByTag(NewsFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(VacanciesFragment.FRAG_TAG)
                            ?.let { fragTransaction.hide(it) }
                        supportFragmentManager.findFragmentByTag(AddRecordFragment.FRAG_TAG)
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