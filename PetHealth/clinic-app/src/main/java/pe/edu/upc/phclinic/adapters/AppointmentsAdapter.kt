package pe.edu.upc.phclinic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import pe.edu.upc.lib.models.ApptModel.ApptResponse
import pe.edu.upc.phclinic.fragments.DetailFragment
import pe.edu.upc.phclinic.R
import kotlin.math.min

class AppointmentsAdapter(var appts: ArrayList<ApptResponse> = ArrayList(),
                          val fragment: Fragment) : RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {
    override fun getItemCount(): Int = appts.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val row = appts[position]
        holder.updateFrom(row)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var apptType: TextView = itemView.findViewById<View>(R.id.apptTypeTextView) as TextView
        var apptVeterinary: TextView = itemView.findViewById<View>(R.id.apptDateTextView) as TextView
        var apptDate: TextView = itemView.findViewById<View>(R.id.apptDateTextView) as TextView
        var petImageView: ImageView = itemView.findViewById<View>(R.id.petImageView) as ImageView

        fun updateFrom(response: ApptResponse){

            apptType.text = response.appointment.type
            apptVeterinary.text = response.veterinary.name

            val fullDate = response.appointment.appt_date
            apptDate.text = fullDate.substring(0, min(fullDate.length, 10))

            Picasso.get().load(response.pet.image_url)
                    .transform(RoundedCornersTransformation(10, 20))
                    .resize(430, 410).into(petImageView)

            itemView.setOnClickListener {
                val newFrag = DetailFragment(response)
                fragment.fragmentManager!!
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, newFrag)
                        .commit()
            }
        }
    }
}