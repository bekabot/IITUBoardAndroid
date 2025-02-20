package kz.iitu.iituboardandroid.ui.board

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kz.iitu.iituboardandroid.Constants
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
    private val drawerLayout: DrawerLayout by bind(R.id.drawer_layout)
    private val navigationView: NavigationView by bind(R.id.nav_view)

    private val vm: BoardVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        setSupportActionBar(toolbar)

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        bottomNavigation.itemIconTintList = null
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_ads -> {
                    showFragmentBy(tag = AdsFragment.FRAG_TAG)
                }
                R.id.action_vacancies -> {
                    showFragmentBy(tag = VacanciesFragment.FRAG_TAG)
                }
                R.id.action_add -> {
                    startActivityForResult(
                        Intent(this, AddRecordActivity::class.java),
                        Constants.REQUEST_CODE_CREATE_RECORD
                    )
                }
                R.id.action_news -> {
                    showFragmentBy(tag = NewsFragment.FRAG_TAG)
                }
                R.id.action_profile -> {
                    showFragmentBy(tag = ProfileFragment.FRAG_TAG)
                }
            }
            true
        }

        findViewById<TextView>(R.id.all_ads).setOnClickListener {
            handleFilterByAdsCategory("")
        }

        findViewById<ImageView>(R.id.ic_services).setOnClickListener {
            handleFilterByAdsCategory("services")
        }

        findViewById<TextView>(R.id.title_services).setOnClickListener {
            handleFilterByAdsCategory("services")
        }

        findViewById<ImageView>(R.id.ic_lost_n_found).setOnClickListener {
            handleFilterByAdsCategory("lost_and_found")
        }

        findViewById<TextView>(R.id.title_lost_n_found).setOnClickListener {
            handleFilterByAdsCategory("lost_and_found")
        }

        findViewById<ImageView>(R.id.ic_sport).setOnClickListener {
            handleFilterByAdsCategory("sport")
        }

        findViewById<TextView>(R.id.title_sport).setOnClickListener {
            handleFilterByAdsCategory("sport")
        }

        findViewById<ImageView>(R.id.ic_hobby).setOnClickListener {
            handleFilterByAdsCategory("hobby")
        }

        findViewById<TextView>(R.id.title_hobby).setOnClickListener {
            handleFilterByAdsCategory("hobby")
        }

        findViewById<ImageView>(R.id.ic_sell).setOnClickListener {
            handleFilterByAdsCategory("sells")
        }

        findViewById<TextView>(R.id.title_sell).setOnClickListener {
            handleFilterByAdsCategory("sells")
        }

        findViewById<ImageView>(R.id.ic_exchange).setOnClickListener {
            handleFilterByAdsCategory("exchange_free")
        }

        findViewById<TextView>(R.id.title_exchange).setOnClickListener {
            handleFilterByAdsCategory("exchange_free")
        }

        findViewById<ImageView>(R.id.ic_rent).setOnClickListener {
            handleFilterByAdsCategory("rent")
        }

        findViewById<TextView>(R.id.title_rent).setOnClickListener {
            handleFilterByAdsCategory("rent")
        }

        findViewById<ImageView>(R.id.ic_search_roommate).setOnClickListener {
            handleFilterByAdsCategory("mate_search")
        }

        findViewById<TextView>(R.id.title_search_roommate).setOnClickListener {
            handleFilterByAdsCategory("mate_search")
        }

        findViewById<ImageView>(R.id.ic_study).setOnClickListener {
            handleFilterByAdsCategory("study")
        }

        findViewById<TextView>(R.id.title_study).setOnClickListener {
            handleFilterByAdsCategory("study")
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
                supportFragmentManager.findFragmentByTag(ProfileFragment.FRAG_TAG)
                    ?.let { fragment ->
                        (fragment as ProfileFragment).updateProfile()
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
            .add(R.id.fragment_container, newsFragment, NewsFragment.FRAG_TAG)
            .hide(newsFragment)
            .add(R.id.fragment_container, adsFragment, AdsFragment.FRAG_TAG)
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

    @FlowPreview
    private fun handleFilterByAdsCategory(adsCategory: String) {
        drawerLayout.closeDrawer(GravityCompat.END)
        supportFragmentManager.findFragmentByTag(AdsFragment.FRAG_TAG)
            ?.let { fragment ->
                (fragment as AdsFragment).filterAdsByCategory(adsCategory)
            }
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_CREATE_RECORD && resultCode == Activity.RESULT_OK) {
            vm.loadAllRecords()
        }
    }
}