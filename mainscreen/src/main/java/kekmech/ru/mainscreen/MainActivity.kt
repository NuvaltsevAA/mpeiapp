package kekmech.ru.mainscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.widget.TextView
import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity
import kekmech.ru.feed.FeedFragment
import kekmech.ru.settings.SettingsDevFragment
import kekmech.ru.settings.SettingsFragment
import kekmech.ru.timetable.TimetableFragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.AppNavigator
import ru.terrakok.cicerone.android.SupportAppNavigator
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    val feedFragment by lazy { FeedFragment() }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                router.replaceScreen("FEED")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                router.replaceScreen("TIMETABLE")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                router.replaceScreen("SETTINGS")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    val navigator = object : SupportAppNavigator(this, R.id.main_container) {


        override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? = null
//            when (screenKey) { "MAIN" -> Intent(this@MainActivity, AuthActivity::class.java)
//            else -> null }

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            "FEED" -> feedFragment //FeedFragment()
            "TIMETABLE" -> TimetableFragment()
            "SETTINGS" -> SettingsFragment()
            "DEV" -> SettingsDevFragment()
            else -> null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    @Inject
    lateinit var navigationHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    override fun onResume() {
        super.onResume()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }
}
