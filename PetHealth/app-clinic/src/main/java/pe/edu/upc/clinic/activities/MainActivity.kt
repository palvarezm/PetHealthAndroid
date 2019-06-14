package pe.edu.upc.clinic.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import pe.edu.upc.clinic.R
import pe.edu.upc.clinic.fragments.AppointmentsFragment
import pe.edu.upc.clinic.fragments.ChatFragment
import pe.edu.upc.clinic.fragments.ProfileFragment
import pe.edu.upc.clinic.persistance.SharedPreferencesManager
import pe.edu.upc.lib.User

class MainActivity : AppCompatActivity() {
    private var user: User? = null
    private var sharedPreferencesManager: SharedPreferencesManager? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item->
        when(item.itemId){
            R.id.navigation_appointments ->{
                println("Mis citas")
                replaceFragment(AppointmentsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_myProfile ->{
                println("Mi perfil")
                replaceFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chat ->{
                println("Chat")
                replaceFragment(ChatFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.applicationContext)
        user = sharedPreferencesManager!!.user

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        /*val host = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,host)
            .setPrimaryNavigationFragment(host)
            .commit()*/
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
