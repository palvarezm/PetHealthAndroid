package pe.edu.upc.pethealth

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_appointment.view.*
import pe.edu.upc.lib.AppointmentResponse

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
        val hour = view.hourTextView
        val petImage = view.petImageView
        val type= view.AppointmentTextView

        fun updateFrom(apptResponse: AppointmentResponse){
            hour.text = apptResponse.appointment.appt_date.substring(2, Math.min(apptResponse.appointment.appt_date.length, 10)) + " " + apptResponse.appointment.start_t.substring(11, Math.min(apptResponse.appointment.start_t.length, 13))
            type.text = apptResponse.appointment.type
            Picasso.get().load(apptResponse.pet.image_url).transform(CropCircleTransformation()).into(petImage)
            layout.setOnClickListener { view->
                val context = view.context
                context.startActivity(
                        Intent(context, AppointmentDetailActivity::class.java))
            }
        }

    }
}