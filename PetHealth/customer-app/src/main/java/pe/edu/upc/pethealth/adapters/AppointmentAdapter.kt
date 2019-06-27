package pe.edu.upc.pethealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pe.edu.upc.lib.models.ApptModel.ApptResponse
import pe.edu.upc.pethealth.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Usuario on 17/11/2017.
 */

class AppointmentAdapter(var appts: ArrayList<ApptResponse> = ArrayList()) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val row = appts[position]
        holder.updateFrom(row)

    }

    override fun getItemCount(): Int = appts.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var vetTextView: TextView = itemView.findViewById<View>(R.id.apptTypeTextView) as TextView
        private var veterinaryTextView: TextView = itemView.findViewById<View>(R.id.apptVeterinaryTextView) as TextView
        private var dateTextView: TextView = itemView.findViewById<View>(R.id.apptDateTextView) as TextView
        fun updateFrom(response: ApptResponse){

            veterinaryTextView.text = response.veterinary.name
            vetTextView.text = response.veterinarian.name
            var apptDate: Date = Date.from(Instant.parse(response.appointment.appt_date))
            var apptStart: Date = Date.from(Instant.parse(response.appointment.start_t))
            var apptEnd: Date = Date.from(Instant.parse(response.appointment.end_t))

            val dateFormatter = SimpleDateFormat("dd/MM, ")
            val timeFormatter = SimpleDateFormat("h:mm aaa")

            dateTextView.text = dateFormatter.format(apptDate) + timeFormatter.format(apptStart) + '-'+ timeFormatter.format(apptEnd)
        }
    }
}
