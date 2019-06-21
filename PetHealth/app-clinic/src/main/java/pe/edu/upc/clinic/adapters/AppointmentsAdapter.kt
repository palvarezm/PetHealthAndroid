package pe.edu.upc.clinic.adapters

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import pe.edu.upc.clinic.R
import pe.edu.upc.clinic.fragments.DetailFragment
import pe.edu.upc.clinic.persistance.SharedPreferencesManager

class AppointmentAdapters(private val fragment: Fragment) : RecyclerView.Adapter<AppointmentAdapters.ViewHolder>() {
    override fun getItemCount(): Int{
        return cardInfo!!.size()
    }

    private var cardInfo: JsonArray? = null
    private var selectedAppointment: JsonObject? = null
    var sharedPreferences : SharedPreferencesManager? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jsonCardInfo = cardInfo!!.get(position).asJsonObject
        val fullDate = jsonCardInfo.get("appointment").asJsonObject.get("appt_date").asString
        val fullStartTime = jsonCardInfo.get("appointment").asJsonObject.get("start_t").asString

        holder.veterinaryTextView.text = jsonCardInfo.get("veterinary").asJsonObject.get("name").asString
        holder.vetTextView.text = jsonCardInfo.get("veterinarian").asJsonObject.get("name").asString
        holder.dateTextView.text = fullDate.substring(0, Math.min(fullDate.length, 10))
        holder.descriptionTextView.text = jsonCardInfo.get("appointment").asJsonObject.get("desc").asString
        holder.startTimeTextView.text = fullStartTime.substring(11, Math.min(fullDate.length, 16))

    }

    fun setAppointments(cardInfo: JsonArray) {
        this.cardInfo = cardInfo
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var vetTextView: TextView
        internal var veterinaryTextView: TextView
        internal var dateTextView: TextView
        internal var startTimeTextView: TextView
        internal var descriptionTextView: TextView
        internal var veterinarianButton: Button

        init {
            vetTextView = itemView.findViewById<View>(R.id.vetTextView) as TextView
            veterinaryTextView = itemView.findViewById<View>(R.id.veterinaryTextView) as TextView
            dateTextView = itemView.findViewById<View>(R.id.dateTextView) as TextView
            startTimeTextView = itemView.findViewById<View>(R.id.startTimeTextView) as TextView
            descriptionTextView = itemView.findViewById<View>(R.id.descriptionTextView) as TextView
            veterinarianButton = itemView.findViewById<View>(R.id.veterinarianButton) as Button

            itemView.setOnClickListener { v: View ->
                var position: Int = adapterPosition
                Snackbar.make(v, "Click detected on item $position",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show()
                var apptString = Gson().toJson(cardInfo!!.get(position))
                val newFrag = DetailFragment()
                val bundle = Bundle()
                bundle.putString("appointment", apptString)
                newFrag.arguments = bundle
                fragment.fragmentManager!!.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, newFrag).commit()
            }
        }



    }
}