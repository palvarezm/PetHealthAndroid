package pe.edu.upc.pethealth.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton

import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonObject

import org.json.JSONException
import org.json.JSONObject

import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.models.Person
import pe.edu.upc.pethealth.models.User
import pe.edu.upc.pethealth.network.Connection
import pe.edu.upc.pethealth.network.RestClient
import pe.edu.upc.pethealth.network.RestView
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartActivity : AppCompatActivity() {

    private lateinit var logoImageView: ImageView
    private lateinit var userEditText: TextInputEditText
    private lateinit var passwordTextInputEditText: TextInputEditText
    private lateinit var signInButton: MaterialButton
    private lateinit var user: User
    private lateinit var person: Person
    private lateinit var signUpTextView: TextView
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    internal val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this)
        if (sharedPreferencesManager.isUserLogged) {
            sendToTipsView()
        }
        setContentView(R.layout.activity_start)
        val toolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        logoImageView = findViewById<View>(R.id.logoImageView) as ImageView
        userEditText = findViewById<View>(R.id.usernameTextInputEditText) as TextInputEditText
        passwordTextInputEditText = findViewById<View>(R.id.passwordTextInputEditText) as TextInputEditText
        signInButton = findViewById<View>(R.id.signInButton) as MaterialButton
        signUpTextView = findViewById<View>(R.id.signUpTextView) as TextView

        setSupportActionBar(toolbar)
        signInButton!!.setOnClickListener { attemptLogin() }
        signUpTextView!!.paintFlags = signUpTextView!!.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        signUpTextView!!.setOnClickListener { view ->
            val context = view.context
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }

    private fun attemptLogin() {

        // Reset errors.
        userEditText!!.error = null
        passwordTextInputEditText!!.error = null

        // Store values at the time of the login attempt.
        val email = userEditText!!.text.toString()
        val password = passwordTextInputEditText!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            userEditText!!.error = getString(R.string.error_field_required)
            focusView = userEditText
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            userEditText!!.error = getString(R.string.error_field_required)
            focusView = userEditText
            cancel = true
        }

        if (!Connection.isOnline(applicationContext)) {
            cancel = true
            return
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            performLogin(email, password, context)
        }
    }

    private fun performLogin(email: String, password: String, context: Context) {
        val bodyToSend = JsonObject()
        bodyToSend.addProperty("username", email)
        bodyToSend.addProperty("password", password)

        val call = RestClient().webServices.login(bodyToSend)
        call.enqueue(object : Callback<RestView<JsonObject>> {
            override fun onResponse(call: Call<RestView<JsonObject>>, response: Response<RestView<JsonObject>>) {
                val answer = response.body()

                if (answer != null && "{}" != answer.data.getAsJsonObject("user").toString()) {
                    try {
                        val userJSONObject = JSONObject(answer.data.get("user").toString())
                        user = Gson().fromJson(userJSONObject.toString(), User::class.java)
                        val personJSONObject = JSONObject(answer.data.get("person").toString())
                        person = Person.from(personJSONObject)
                        sharedPreferencesManager!!.saveUser(user!!.toString(), person!!.toString(), answer.data.get("access_token").asString)
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtras(user!!.toBundle())
                        context.startActivity(intent)
                        finish()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    Log.d(getString(R.string.app_name), "User and password are incorrect")
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("User and password are incorrect")
                    builder.setPositiveButton("OK") { dialogInterface, i -> dialogInterface.cancel() }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }

            override fun onFailure(call: Call<RestView<JsonObject>>, t: Throwable) {
            }
        })
    }

    private fun sendToTipsView() {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        finish()
    }

    override fun onBackPressed() {

    }
}
