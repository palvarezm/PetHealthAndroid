package pe.edu.upc.pethealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.upc.lib.ApptModel.AppointmentResponse
import pe.edu.upc.pethealth.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Usuario on 17/11/2017.
 */

class AppointmentAdapter(var appts: ArrayList<AppointmentResponse> = ArrayList()) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val junction = appts[position]
        holder.updateFrom(junction)

    }

    override fun getItemCount(): Int = appts.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var vetTextView: TextView = itemView.findViewById<View>(R.id.vetTextView) as TextView
        internal var veterinaryTextView: TextView = itemView.findViewById<View>(R.id.veterinaryTextView) as TextView
        internal var dateTextView: TextView = itemView.findViewById<View>(R.id.dateTextView) as TextView
        internal var descriptionTextView: TextView = itemView.findViewById<View>(R.id.descriptionTextView) as TextView
        fun updateFrom(appt: AppointmentResponse){
            veterinaryTextView.text = appt.veterinary.name
            vetTextView.text = appt.veterinarian.name
            descriptionTextView.text = appt.appointment.desc
            var apptDate: Date
            var apptStart: Date
            var apptEnd: Date

            val dateFormatter = SimpleDateFormat("dd/MM, ")
            val timeFormatter = SimpleDateFormat("h:mm aaa")

            apptDate = Date.from(Instant.parse(appt.appointment.appt_date))
            apptStart = Date.from(Instant.parse(appt.appointment.start_t))
            apptEnd = Date.from(Instant.parse(appt.appointment.end_t))
            dateTextView.text = dateFormatter.format(apptDate) + timeFormatter.format(apptStart) + '-'+ timeFormatter.format(apptEnd)
        }
    }
}
