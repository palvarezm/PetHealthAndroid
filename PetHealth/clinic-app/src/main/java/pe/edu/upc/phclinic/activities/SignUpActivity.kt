package pe.edu.upc.phclinic.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.salomonbrys.kotson.put
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_sing_up.*
import org.json.JSONException
import org.json.JSONObject
import pe.edu.upc.phclinic.R
import pe.edu.upc.phclinic.network.LoggerCallback
import pe.edu.upc.phclinic.network.RestClient
import pe.edu.upc.phclinic.network.RestView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : AppCompatActivity() {

    private lateinit var profileUri: Uri
    internal val context: Context = this
    lateinit var storageRef: StorageReference
    private val GALLERY_IMAGE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        storageRef = FirebaseStorage.getInstance().reference
        doneButton.setOnClickListener { attemptToSignUp() }
        profileImageView.setOnClickListener { chooseImage()}
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            GALLERY_IMAGE -> {
                profileImageView.setImageURI(data?.data)
                profileUri = data!!.data
            }
        }
    }
    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, GALLERY_IMAGE)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun attemptToSignUp() {

        //Reset Errors
        passwordTextInputEditText.error = null
        usernameTextInputEditText.error = null
        emailTextInputEditText.error = null
        nameTextInputEditText.error = null
        lastNameTextInputEditText.error = null
        docNumberTextInputEditText.error = null
        phoneTextInputEditText.error = null


        var focusView: View? = null
        var cancel: Boolean? = false

        if (TextUtils.isEmpty(usernameTextInputEditText.text)) {
            usernameTextInputEditText.error = "This field is empty"
            focusView = usernameTextInputEditText
            cancel = true
        }
        if (TextUtils.isEmpty(passwordTextInputEditText.text)) {
            passwordTextInputEditText.error = "This field is empty"
            focusView = passwordTextInputEditText
            cancel = true
        }
        if (TextUtils.isEmpty(emailTextInputEditText.text)) {
            emailTextInputEditText.error = "This field is empty"
            focusView = emailTextInputEditText
            cancel = true
        }
        if (TextUtils.isEmpty(nameTextInputEditText.text)) {
            nameTextInputEditText.error = "This field is empty"
            focusView = nameTextInputEditText
            cancel = true
        }
        if (TextUtils.isEmpty(lastNameTextInputEditText.text)) {
            lastNameTextInputEditText.error = "This field is empty"
            focusView = lastNameTextInputEditText
            cancel = true
        }
        if (TextUtils.isEmpty(docNumberTextInputEditText.text)) {
            docNumberTextInputEditText.error = "This field is empty"
            focusView = docNumberTextInputEditText
            cancel = true
        }
        if (TextUtils.isEmpty(phoneTextInputEditText.text)) {
            phoneTextInputEditText.error = "This field is empty"
            focusView = phoneTextInputEditText
            cancel = true
        }

        if (cancel!!) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            prepareImage(usernameTextInputEditText.text){profileDownloadUri ->
                val bodyToSend = JsonObject()
                try {
                    bodyToSend.put(Pair("username", usernameTextInputEditText.text))
                    bodyToSend.put(Pair("password", passwordTextInputEditText.text))
                    bodyToSend.put(Pair("mail", emailTextInputEditText.text))
                    bodyToSend.put(Pair("photo", profileDownloadUri))
                    bodyToSend.put(Pair("userable_type", 2))
                    bodyToSend.put(Pair("name", nameTextInputEditText.text))
                    bodyToSend.put(Pair("last_name", lastNameTextInputEditText.text))
                    bodyToSend.put(Pair("dni", docNumberTextInputEditText.text))
                    bodyToSend.put(Pair("phone,address", phoneTextInputEditText.text))
                    bodyToSend.put(Pair("linkedin_link", ""))
                    bodyToSend.put(Pair("degree", ""))
                    bodyToSend.put(Pair("location", ""))
                    bodyToSend.put(Pair("opening_hours", ""))
                    bodyToSend.put(Pair("website_url", ""))
                    bodyToSend.put(Pair("youtube_url", ""))
                    bodyToSend.put(Pair("twitter_url", ""))

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
/*
                val call = RestClient().webServices.signup(bodyToSend)
                call.enqueue( Callback<>(){
                    override fun onResponse(call: Call<RestView<JsonObject>>, response: Response<RestView<JsonObject>>) {
                        super.onResponse(call, response)

                    }
                }
                )*/

            }
        }
    }
    private fun prepareImage(username: Editable?, callback: (profileDownloadUri: String)-> Unit) {
        val ref = storageRef.child("images/${username.toString()}")
        var uploadTask = ref.putFile(profileUri)

        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                callback(downloadUri.toString())
            }
        }
    }
}