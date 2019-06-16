package pe.edu.upc.clinic.persistance


import android.content.Context
import android.content.SharedPreferences
import android.util.Log


import com.google.gson.Gson
import com.google.gson.JsonParseException
import pe.edu.upc.lib.User
import pe.edu.upc.lib.Veterinary

class SharedPreferencesManager(context: Context) {
    private val mPreferences: SharedPreferences
    private val gson: Gson

    val user: User?
        get() {
            val userString = mPreferences.getString(CURRENT_USER, "")
            val gson = Gson()
            var user: User? = null
            if(userString!!.isEmpty()) {

            } else {
                user = gson.fromJson<User>(userString.toString(), User::class.java)
            }
            return user;
        }

    val veterinary: Veterinary?
        get() {
            val veterinaryString = mPreferences.getString(CURRENT_VETERINARY, "")
            val gson = Gson()
            var veterinary: Veterinary? = null
            if (veterinaryString!!.isEmpty()) {

            } else {
                veterinary = gson.fromJson<Veterinary>(veterinaryString.toString(), Veterinary::class.java)
            }
            return veterinary
        }

    val isUserLogged: Boolean
        get() = user != null

    val accessToken: String?
        get() = mPreferences.getString(ACCESS_TOKEN, "")

    init {
        this.mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        gson = Gson()
    }

    fun saveUser(user: String, veterinary: String, access_token: String) {
        val editor = mPreferences.edit()
        editor.putString(CURRENT_USER, user)
        editor.putString(CURRENT_VETERINARY, veterinary)
        editor.putString(ACCESS_TOKEN, access_token)
        editor.apply()
    }

    fun closeSession() {
        val editor = mPreferences.edit()
        editor.putString(CURRENT_USER, null)
        editor.putString(CURRENT_VETERINARY, null)
        editor.putString(COMPLETED_REGISTER, null)
        editor.apply()
    }

    companion object {
        private val PREFERENCES_NAME = "blcc"
        private val CURRENT_USER = "current_user"
        private val CURRENT_VETERINARY = "current_veterinary"
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