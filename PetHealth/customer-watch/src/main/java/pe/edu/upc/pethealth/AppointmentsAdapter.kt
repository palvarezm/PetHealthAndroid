package pe.edu.upc.pethealth

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_appointment.view.*
import pe.edu.upc.lib.models.ApptModel.ApptResponse
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class AppointmentsAdapter(var appts: ArrayList<ApptResponse>,
                          private val context: Context
) : RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_appointment, parent, false)
        )
    }

    override fun getItemCount() = appts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val junction = appts[position]
        holder.updateFrom(junction)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout = view.appointmentLayout
        val date = view.scheduleTextView
        val petImage = view.petImageView
        val type= view.AppointmentTextView

        fun updateFrom(apptResponse: ApptResponse){

            val schedule: String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var apptDate: Date
                var apptStart: Date
                var apptEnd: Date

                val dateFormatter = SimpleDateFormat("dd/MM, ")
                val timeFormatter = SimpleDateFormat("h:mm aaa")

                apptDate = Date.from(Instant.parse(apptResponse.appointment.appt_date))
                apptStart = Date.from(Instant.parse(apptResponse.appointment.start_t))
                apptEnd = Date.from(Instant.parse(apptResponse.appointment.end_t))
                schedule = dateFormatter.format(apptDate) + timeFormatter.format(apptStart) + '-'+ timeFormatter.format(apptEnd)

            } else {
                val date = apptResponse.appointment.appt_date.substring(0, min(apptResponse.appointment.appt_date.length, 10));
                val start = apptResponse.appointment.start_t.substring(11, min(apptResponse.appointment.start_t.length, 16));
                val end = apptResponse.appointment.end_t.substring(11, min(apptResponse.appointment.end_t.length, 16));
                schedule = "$date $start - $end"
            }
            type.text = apptResponse.appointment.type
            date.text = schedule
            Picasso.get().load(apptResponse.pet.image_url).transform(CropCircleTransformation()).into(petImage)
            layout.setOnClickListener { view->
                val context = view.context
                val appt = Gson().toJson(apptResponse)
                context.startActivity(
                        Intent(context, AppointmentDetailActivity::class.java).
                                putExtra("appointment", appt))
            }
        }

    }
}