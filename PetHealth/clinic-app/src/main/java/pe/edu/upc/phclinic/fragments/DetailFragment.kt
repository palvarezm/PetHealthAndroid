package pe.edu.upc.phclinic.fragments

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_appointment.*
import pe.edu.upc.lib.models.ApptModel.ApptResponse
import pe.edu.upc.lib.models.User
import pe.edu.upc.phclinic.R
import pe.edu.upc.phclinic.adapters.HistoriesAdapter
import pe.edu.upc.phclinic.graphics.CircleTransform
import pe.edu.upc.phclinic.network.RestClient
import pe.edu.upc.phclinic.network.RestView
import pe.edu.upc.phclinic.persistance.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment(var apptResponse: ApptResponse) : Fragment() {

    private lateinit var historiesAdapter : HistoriesAdapter
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var user : User

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_appointment, container, false)
        historiesAdapter = HistoriesAdapter(fragment = this)

        this.activity!!.window.decorView.foreground = ColorDrawable(0xCC000000.toInt())
        this.activity!!.window.decorView.foreground.alpha = 0
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.context!!)
        user = sharedPreferencesManager.user!!
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historiesRecyclerView.apply {
            adapter = historiesAdapter
            layoutManager = GridLayoutManager(this.context, 1)
        }
        petNameTextView.text = apptResponse.pet.name
        petRaceTextView.text = apptResponse.pet.race
        petDescTextView.text = apptResponse.pet.description
        petBirthTextView.text = apptResponse.pet.birth_date

        apptTypeTextView.text = apptResponse.appointment.type
        apptDateTextView.text = apptResponse.appointment.appt_date
        apptDescTextView.text = apptResponse.appointment.desc
        apptVetTextView.text = apptResponse.veterinarian.name

        Picasso.get().load(apptResponse.pet.image_url)
                .transform(CircleTransform())
                .into(petImageView)
        updateHistories(apptResponse)
        apptFinishButton.setOnClickListener{
            this.activity!!.window.decorView.foreground.alpha = 220
            val alert = AlertDialog.Builder(this.context!!)
            val finishApptView = LayoutInflater.from(this.activity).inflate(R.layout.popup_finish_appointment, null)
            //val popupWindow = PopupWindow(finishApptView, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
            val buttonAccept = finishApptView.findViewById<Button>(R.id.acceptButton)
            val buttonCancel = finishApptView.findViewById<Button>(R.id.cancelButton)
            val diagnoseEditText  = finishApptView.findViewById<TextInputEditText>(R.id.diagnoseTextInputEditText)
            val prescriptionEditText  = finishApptView.findViewById<TextInputEditText>(R.id.prescriptionTextInputEditText)


            val dialog = alert.create()
            dialog.setView(finishApptView)
            dialog.show()

/*
            // Create a new slide animation for popup window enter transition
            popupWindow.elevation = .0F
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn
            popupWindow.isTouchable = true

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.RIGHT
            popupWindow.exitTransition = slideOut
            TransitionManager.beginDelayedTransition(appointmentDetailConstraintLayout)
            popupWindow.showAtLocation(
                    appointmentDetailConstraintLayout, // Location to display popup window
                    Gravity.CENTER, // Exact position of layout to display popup
                    0, // X offset
                    0 // Y offset
            )
            this.activity!!.window.decorView.foreground.alpha = 220
            buttonPopup.setOnClickListener {
                popupWindow.dismiss()
                this.activity!!.window.decorView.foreground.alpha = 0
            */
            buttonAccept.setOnClickListener{

                dialog.hide()
                this.activity!!.window.decorView.foreground.alpha = 0
            }


            buttonCancel.setOnClickListener {
                dialog.hide()
                this.activity!!.window.decorView.foreground.alpha = 0
            }

        }
    }

    private fun updateHistories(appt: ApptResponse){
        val histories = appt.pet.history
        historiesAdapter.histories = histories
        historiesAdapter.notifyDataSetChanged()
    }

    private fun finishAppt(diagnose: String, prescription: String){
        val bodyToSend = JsonObject()
        bodyToSend.addProperty("motive", diagnose)
        bodyToSend.addProperty("diagnosis", prescription)

        val call = RestClient().service.finishAppointment(sharedPreferencesManager.accessToken!!, apptResponse.appointment.id!!, bodyToSend)
        call.enqueue(object : Callback<RestView<JsonObject>> {
            override fun onResponse(call: Call<RestView<JsonObject>>, response: Response<RestView<JsonObject>>) {
                /*val answer

                if (answer != null && "{}" != answer.data?.getAsJsonObject("user").toString()) {
                    try {
                        val userJSONObject = JSONObject(answer.data?.get("user").toString())
                        val gson = GsonBuilder().create()
                        user = gson.fromJson<User>(userJSONObject.toString(), User::class.java)

                        val veterinaryJSONObject = JSONObject(answer.data?.get("person").toString())
                        veterinary = gson.fromJson<VeterinaryModel.Veterinary>(veterinaryJSONObject.toString(), VeterinaryModel.Veterinary::class.java)

                        sharedPreferencesManager!!.saveUser(
                                gson.toJson(user),
                                gson.toJson(veterinary),
                                answer.data!!.get("access_token").asString
                        )

                        Log.d("TESTING", "sharedpreferences")
                        Log.d("user sp", sharedPreferencesManager.user.toString())
                        Log.d("veterinary sp", sharedPreferencesManager.veterinary.toString())
                        Log.d("accesstoken sp", sharedPreferencesManager.accessToken.toString())

                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("user", user!!)
                        context.startActivity(intent)
                        finish()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    Log.d(getString(R.string.app_name), "User and password are incorrect")
                    val builder = android.app.AlertDialog.Builder(context)
                    builder.setMessage("User and password are incorrect")
                    builder.setPositiveButton(
                            "OK"
                    ) { dialogInterface, _ -> dialogInterface.cancel() }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }*/
            }


            override fun onFailure(call: Call<RestView<JsonObject>>, t: Throwable) {
            }
        })
    }

}