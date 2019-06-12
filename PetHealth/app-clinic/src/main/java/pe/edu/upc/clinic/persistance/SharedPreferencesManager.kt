package pe.edu.upc.clinic.persistance


import android.content.Context
import android.content.SharedPreferences
import android.util.Log


import com.google.gson.Gson
import pe.edu.upc.lib.Person
import pe.edu.upc.lib.User

class SharedPreferencesManager(context: Context) {
    private val mPreferences: SharedPreferences
    private val gson: Gson

    val user: User?
        get() {
            val userString = mPreferences.getString(CURRENT_USER, "")
            val gson = Gson()
            if (!userString!!.isEmpty()) {
                Log.d("userstring", userString)
                return gson.fromJson<User>(userString, User::class.java)
            } else {
                return null
            }
        }

    val person: Person?
        get() {
            val personString = mPreferences.getString(CURRENT_PERSON, "")
            val gson = Gson()
            return if (!personString!!.isEmpty()) {
                gson.fromJson<Person>(personString, Person::class.java)
            } else {
                null
            }
        }

    val isUserLogged: Boolean
        get() = user != null

    val accessToken: String?
        get() = mPreferences.getString(ACCESS_TOKEN, "")

    init {
        this.mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        gson = Gson()
    }

    fun saveUser(user: String, person: String, access_token: String) {
        val editor = mPreferences.edit()
        editor.putString(CURRENT_USER, user)
        editor.putString(CURRENT_PERSON, person)
        editor.putString(ACCESS_TOKEN, access_token)
        editor.apply()
    }

    fun closeSession() {
        val editor = mPreferences.edit()
        editor.putString(CURRENT_USER, null)
        editor.putString(CURRENT_PERSON, null)
        editor.putString(COMPLETED_REGISTER, null)
        editor.apply()
    }

    companion object {
        private val PREFERENCES_NAME = "blcc"
        private val CURRENT_USER = "current_user"
        private val CURRENT_PERSON = "current_person"
        private val ACCESS_TOKEN = "access_token"
        private val COMPLETED_REGISTER = "current_register"

        private var self: SharedPreferencesManager? = null

        fun getInstance(context: Context): SharedPreferencesManager {
            if (self == null) {
                self = SharedPreferencesManager(context)
            }
            return self as SharedPreferencesManager
        }
    }
}