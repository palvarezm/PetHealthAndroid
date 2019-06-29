package pe.edu.upc.pethealth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_appointment_detail.*
import pe.edu.upc.lib.models.ApptModel.ApptResponse
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class AppointmentDetailActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_detail)
        val apptString = intent.extras.getString("appointment")
        val appt = Gson().fromJson<ApptResponse>(apptString)

        val dateFormatter = SimpleDateFormat("dd/MM, ", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("h:mm aaa",Locale.getDefault())

        val schedule: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var date = Date.from(Instant.parse(appt.appointment.appt_date))
            val formatter = SimpleDateFormat("EEE, MMM d, hh:mm aaa", Locale.getDefault())
            schedule = formatter.format(date)

        } else {
            val date = appt.appointment.appt_date.substring(0, Math.min(appt.appointment.appt_date.length, 10));
            val start = appt.appointment.start_t.substring(11, Math.min(appt.appointment.start_t.length, 16));
            val end = appt.appointment.end_t.substring(11, Math.min(appt.appointment.end_t.length, 16));
            schedule = "$date $start - $end"
        }
        apptTypeTextView.text = appt.appointment.type
        descriptionTextView.text = appt. appointment.desc
        scheduleTextView.text = schedule
        vetTextView.text = appt.veterinarian.name
        veterinaryTextView.text = appt.veterinary.name
        Picasso.get()
                .load(appt.veterinary.logo)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .transform(CropCircleTransformation())
                .into(veterinaryImageView)
        locationButton.setOnClickListener{
            this@AppointmentDetailActivity.startActivity(
                    Intent(this@AppointmentDetailActivity, MapsActivity::class.java)
                            .putExtra("latitude",appt.veterinary.latitude)
                            .putExtra("longitude",appt.veterinary.longitude)
                            .putExtra("veterinaryName",appt.veterinary.name))
        }
        setAmbientEnabled()
    }
}
