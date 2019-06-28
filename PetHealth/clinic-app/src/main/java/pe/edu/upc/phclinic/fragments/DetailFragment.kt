package pe.edu.upc.phclinic.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_detail_appointment.*
import org.json.JSONException
import org.json.JSONObject
import pe.edu.upc.lib.models.ApptModel.ApptResponse
import pe.edu.upc.lib.models.User
import pe.edu.upc.lib.models.VeterinaryModel
import pe.edu.upc.phclinic.R
import pe.edu.upc.phclinic.activities.MainActivity
import pe.edu.upc.phclinic.adapters.HistoriesAdapter
import pe.edu.upc.phclinic.network.RestClient
import pe.edu.upc.phclinic.network.RestView
import pe.edu.upc.phclinic.persistance.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment(var appt: ApptResponse) : Fragment() {

    private lateinit var historiesAdapter : HistoriesAdapter
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var user : User

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_appointment, container, false)
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.context!!)
        user = sharedPreferencesManager.user!!
        Log.d("Appointment_id", appt.appointment.id.toString())
        view.foreground.alpha = 0
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apptVetTextView.text = appt.pet.name
        petRaceTextView.text = appt.pet.race
        petDescTextView.text = appt.pet.description
        petBirthTextView.text = appt.pet.birth_date
        apptDateTextView.text = appt.appointment.appt_date
        apptDescTextView.text = appt.appointment.desc
        Picasso.get().load(appt.pet.image_url).transform(RoundedCornersTransformation(10, 20))
                .resize(600, 600).centerCrop().into(petImageView)
        updateHistories(appt)


        apptFinishButton.setOnClickListener{

            view.foreground.alpha = 220

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
            */
            buttonAccept.setOnClickListener{
                val diagnose = diagnoseEditText.text.toString()
                val prescription = prescriptionEditText.text.toString()
                finishAppt(diagnose, prescription)
                dialog.hide()
                replaceFragment(AppointmentsFragment())
                view.foreground.alpha = 0
            }


            buttonCancel.setOnClickListener {
                dialog.hide()
                view.foreground.alpha = 0
            }

        }
    }

    private fun updateHistories(appt: ApptResponse){
        val histories = appt.pet.history
        historiesAdapter = HistoriesAdapter(this)
        historiesRecyclerView.adapter = historiesAdapter
        historiesRecyclerView.layoutManager = GridLayoutManager(this.context, 1)
        historiesAdapter.setHistories(histories)
        historiesAdapter.notifyDataSetChanged()
    }

    private fun finishAppt(diagnose: String, prescription: String){
        val bodyToSend = JsonObject()
        bodyToSend.addProperty("motive", diagnose)
        bodyToSend.addProperty("diagnosis", prescription)

        val call = RestClient().service.finishAppointment(sharedPreferencesManager.accessToken!!, appt.appointment.id!!, bodyToSend)
        call.enqueue(object : Callback<RestView<JsonObject>> {
            override fun onResponse(call: Call<RestView<JsonObject>>, response: Response<RestView<JsonObject>>) {
                val answer = response.body()!!.status

                if(answer != null && answer == "OK"){
                    Log.d("Oshe", "funcionó")
                }
                else {
                    Log.d(getString(R.string.app_name), "There was a problem")
                    val builder = android.app.AlertDialog.Builder(context)
                    builder.setMessage("There's a problem with the content")
                    builder.setPositiveButton(
                            "OK"
                    ) { dialogInterface, _ -> dialogInterface.cancel() }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }


            override fun onFailure(call: Call<RestView<JsonObject>>, t: Throwable) {
            }
        })
    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = this.fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

}