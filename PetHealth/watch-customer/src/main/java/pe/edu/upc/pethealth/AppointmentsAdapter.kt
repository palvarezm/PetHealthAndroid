package pe.edu.upc.pethealth

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_appointment.view.*
import pe.edu.upc.lib.AppointmentResponse
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class AppointmentsAdapter(var appts: ArrayList<AppointmentResponse>,
                          val context: Context
) : RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_appointment, parent, false)
        )
    }

    override fun getItemCount() = appts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val junction = appts.get(position)
        holder.updateFrom(junction)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout = view.appointmentLayout
        val date = view.scheduleTextView
        val petImage = view.petImageView
        val type= view.AppointmentTextView

        fun updateFrom(apptResponse: AppointmentResponse){
            var apptDate = Date.from(Instant.parse(apptResponse.appointment.appt_date))
            var apptStart = Date.from(Instant.parse(apptResponse.appointment.start_t))
            var apptEnd = Date.from(Instant.parse(apptResponse.appointment.end_t))
            val dateFormatter = SimpleDateFormat("dd/MM, ")
            val timeFormatter = SimpleDateFormat("h:mm aaa")
            val schedule = dateFormatter.format(apptDate) + timeFormatter.format(apptStart) + '-'+ timeFormatter.format(apptEnd)
            date.text = schedule
            Picasso.get().load(apptResponse.pet.image_url).transform(CropCircleTransformation()).into(petImage)
            layout.setOnClickListener { view->
                val context = view.context
                val appt = Gson().toJson(apptResponse)
                context.startActivity(
                        Intent(context, AppointmentDetailActivity::class.java).
                                putExtra("appt", appt))
            }
        }

    }
}