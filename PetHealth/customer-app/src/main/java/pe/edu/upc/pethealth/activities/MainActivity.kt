package pe.edu.upc.pethealth.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.fragments.AppointmentFragment
import pe.edu.upc.pethealth.fragments.ChatsFragment
import pe.edu.upc.pethealth.fragments.HomeFragment
import pe.edu.upc.pethealth.fragments.ProfileFragment
import pe.edu.upc.pethealth.fragments.SearchFragment
import pe.edu.upc.pethealth.models.User
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager

class MainActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var user: User? = null
    private var sharedPreferencesManager: SharedPreferencesManager? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item -> navigateAccordingTo(item.itemId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.applicationContext)
        user = sharedPreferencesManager!!.user
        toolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val navigation = findViewById<View>(R.id.navigation) as BottomNavigationView
        val menuView = navigation.getChildAt(0) as BottomNavigationMenuView

        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            val activeLabel = item.findViewById<View>(R.id.largeLabel)
            if (activeLabel is TextView) {
                activeLabel.setPadding(0, 0, 0, 0)
            }
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigateAccordingTo(R.id.navigation_home)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //this adds items to the action bar if present
        menuInflater.inflate(R.menu.navigation_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_logout -> {
                sharedPreferencesManager!!.closeSession()
                val intent = Intent(this@MainActivity, StartActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.navigation_search -> {
                supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content, SearchFragment()).commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateAccordingTo(id: Int): Boolean {
        try {
            val fm = supportFragmentManager
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content, getFragmentFor(id)!!).commit()
            return true
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return false
        }

    }

    private fun getFragmentFor(id: Int): Fragment? {
        when (id) {
            R.id.navigation_home -> return HomeFragment()
            R.id.navigation_myappointments -> {
                return AppointmentFragment()
            }
            R.id.navigation_chat -> return ChatsFragment()
            R.id.navigation_profile -> return ProfileFragment()
        }
        return null
    }


    fun setFragmentToolbar(toolbarTitle: String, backIcon: Boolean, fragmentManager: FragmentManager?) {
        toolbar!!.title = toolbarTitle
        val icon: Int?
        if (backIcon) {
            icon = R.drawable.ic_chevron_left_black_24dp
            toolbar!!.setNavigationIcon(icon)
            toolbar!!.setNavigationOnClickListener { fragmentManager!!.popBackStack() }
        } else {
            toolbar!!.navigationIcon = null
        }
    }
}
