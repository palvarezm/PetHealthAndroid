package pe.edu.upc.clinic.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import pe.edu.upc.clinic.R
import pe.edu.upc.clinic.network.Connection
import pe.edu.upc.clinic.network.LoggerCallback
import pe.edu.upc.clinic.network.RestClient
import pe.edu.upc.clinic.network.RestView
import pe.edu.upc.clinic.persistance.SharedPreferencesManager
import pe.edu.upc.lib.Person
import pe.edu.upc.lib.User
import retrofit2.Call
import retrofit2.Response

class SignInActivity : AppCompatActivity() {


    private var logoImageView: ImageView? = null
    private var userEditText: EditText? = null
    private var passwordTextInputEditText: EditText? = null
    private var signInButton: Button? = null
    private var user: User? = null
    private var person: Person? = null
    private var signUptextView: TextView? = null
    private var sharedPreferencesManager: SharedPreferencesManager? = null
    internal val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.context)
        if (sharedPreferencesManager!!.isUserLogged) {
            sendToApptsView()
        }
        setContentView(R.layout.activity_sign_in)

        logoImageView = findViewById<View>(R.id.logoImageView) as ImageView?
        userEditText = findViewById<View>(R.id.usernameEditText) as EditText?
        passwordTextInputEditText = findViewById<View>(R.id.passwordTextInputEditText) as EditText?
        signInButton = findViewById<View>(R.id.signInButton) as Button?
        signInButton!!.setOnClickListener { attemptLogin() }
        signUptextView = findViewById<View>(R.id.signUpTextView) as TextView?
        signUptextView!!.paintFlags = signUptextView!!.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        signUptextView!!.setOnClickListener { view ->
            val context = view.context
            //val intent = Intent(context, SignUpActivity::class.java)
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
        call.enqueue(object : LoggerCallback<RestView<JsonObject>>() {
            override fun onResponse(call: Call<RestView<JsonObject>>, response: Response<RestView<JsonObject>>) {
                super.onResponse(call, response)
                val answer = response.body()

                if (answer != null && "{}" != answer.data?.getAsJsonObject("user").toString()) {
                    try {
                        val userJSONObject = JSONObject(answer.data?.get("user").toString())
                        val gson = GsonBuilder().create()
                        user = gson.fromJson<User>(userJSONObject.toString(), User::class.java)

                        val personJSONObject = JSONObject(answer.data?.get("person").toString())
                        person = gson.fromJson<Person>(personJSONObject.toString(), Person::class.java)

                        sharedPreferencesManager!!.saveUser(
                                user!!.toString(),
                                person!!.toString(),
                                answer.data!!.get("access_token").asString
                        )
                        val intent = Intent(context, MainActivity::class.java)
                        Log.d("user", user!!.username)
                        Log.d("person", person!!.name)
                        intent.putExtra("user", user!!)
                        context.startActivity(intent)
                        finish()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    Log.d(getString(R.string.app_name), "User and password are incorrect")
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("User and password are incorrect")
                    builder.setPositiveButton(
                            "OK"
                    ) { dialogInterface, i -> dialogInterface.cancel() }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }


            override fun onFailure(call: Call<RestView<JsonObject>>, t: Throwable) {
                super.onFailure(call, t)
            }
        })
    }


    private fun sendToApptsView() {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        finish()
    }


    override fun onBackPressed() {

    }
}
