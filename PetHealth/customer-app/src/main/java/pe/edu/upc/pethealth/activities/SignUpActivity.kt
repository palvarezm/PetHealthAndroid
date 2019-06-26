package pe.edu.upc.pethealth.activities

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
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_sing_up.*

import org.json.JSONException
import org.json.JSONObject

import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.network.PetHealthApiService

class SignUpActivity : AppCompatActivity() {

    private lateinit var profileUri: Uri
    internal val context: Context = this
    lateinit var storageRef: StorageReference
    private val GALLERY_IMAGE = 1
    private var fields = ArrayList<TextInputEditText>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        storageRef = FirebaseStorage.getInstance().reference
        Picasso.get().load(R.mipmap.ic_default_profile_image_foreground).
                transform(RoundedCornersTransformation(10,5))
                .fit().centerCrop().into(profileImageView)

        fields.add(usernameTextInputEditText)
        fields.add(passwordTextInputEditText)
        fields.add(emailTextInputEditText)
        fields.add(nameTextInputEditText)
        fields.add(lastNameTextInputEditText)
        fields.add(docNumberTextInputEditText)
        fields.add(addressTextInputEditText)
        fields.add(phoneTextInputEditText)

        doneButton.setOnClickListener { attemptToSignUp() }
        profileImageView.setOnClickListener { chooseImage()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            GALLERY_IMAGE -> {
                Picasso.get().load(data?.data)
                        .transform(RoundedCornersTransformation(10,5))
                        .fit().centerCrop()
                        .into(profileImageView)
                //profileImageView.setImageURI(data?.data)
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

        var focusView: View? = null
        var cancel: Boolean? = false


        this.fields.forEach{ field  ->
            if (TextUtils.isEmpty(field.text)){
                field.error = "${field.hint} is empty"
                focusView = field
                cancel = true
            }
        }
        if (cancel!!) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            prepareImage(usernameTextInputEditText.text){profileDownloadUri ->
                val bodyToSend = JSONObject()
                try {
                    bodyToSend.put("username", usernameTextInputEditText.text)
                    bodyToSend.put("password", passwordTextInputEditText.text)
                    bodyToSend.put("mail", emailTextInputEditText.text)
                    bodyToSend.put("photo", profileDownloadUri)
                    bodyToSend.put("userable_type", 2)
                    bodyToSend.put("name", nameTextInputEditText.text)
                    bodyToSend.put("last_name", lastNameTextInputEditText.text)
                    bodyToSend.put("dni", docNumberTextInputEditText.text)
                    bodyToSend.put("phone", phoneTextInputEditText.text)
                    bodyToSend.put("address", addressTextInputEditText.text)
//                    bodyToSend.put("linkedin_link", "")
                } catch (e: JSONException) {
                    e.printStackTrace()
//                    bodyToSend.put("degree", "")
//                    bodyToSend.put("location", "")
//                    bodyToSend.put("opening_hours", "")
//                    bodyToSend.put("website_url", "")
//                    bodyToSend.put("youtube_url", "")
//                    bodyToSend.put("twitter_url", "")

                }
                sendUserData(bodyToSend)
            }
        }
    }
    private fun prepareImage(username: Editable?, callback: (profileDownloadUri: String)-> Unit) {
        val ref = storageRef.child("images/clients/${username.toString()}")
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
    private fun sendUserData(body: JSONObject){

        AndroidNetworking.post(PetHealthApiService.SIGNUP_USER)
                .addJSONObjectBody(body)
                .setTag(getString(R.string.app_name))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response
                        try {
                            if ("ok".equals(response.getString("status"), ignoreCase = true)) {
                                val builder = AlertDialog.Builder(context)
                                builder.setMessage("User Corretly Created")
                                builder.setPositiveButton("OK") { dialogInterface, i ->
                                    dialogInterface.cancel()
                                    val intent = Intent(context, StartActivity::class.java)
                                    context.startActivity(intent)
                                    finish()
                                }
                                val alertDialog = builder.create()
                                alertDialog.show()

                            } else {
                                Log.d(getString(R.string.app_name), "Error with the Resgistration of User")
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onError(anError: ANError) {
                        Log.d(getString(R.string.app_name), anError.localizedMessage)
                    }
                })
    }
}
