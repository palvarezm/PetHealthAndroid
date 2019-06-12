package pe.edu.upc.pethealth

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_appointment_detail.*
import pe.edu.upc.lib.AppointmentResponse
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class AppointmentDetailActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_detail)
        val apptString = intent.extras.getString("appt")
        val appt = Gson().fromJson<AppointmentResponse>(apptString)
        var date = Date.from(Instant.parse(appt.appointment.appt_date))
        val formatter = SimpleDateFormat("EEE, MMM d, hh:mm aaa")
        val schedule = formatter.format(date)
        apptTypeTextView.text = appt.appointment.type
        descriptionTextView.text = appt. appointment.desc
        scheduleTextView.text = schedule.toString()
        vetTextView.text = appt.veterinarian.name
        veterinaryTextView.text = appt.veterinary.name
        Picasso.get().load(appt.veterinary.logo).transform(CropCircleTransformation()).into(veterinaryImageView)
        locationButton.setOnClickListener{
            this@AppointmentDetailActivity.startActivity(
                    Intent(this@AppointmentDetailActivity, MapsActivity::class.java)
                            .putExtra("latitud",appt.veterinary.latitude)
                            .putExtra("longitud",appt.veterinary.longitude)
                            .putExtra("veterinaryName",appt.veterinary.name))
        }
        setAmbientEnabled()
    }
}
