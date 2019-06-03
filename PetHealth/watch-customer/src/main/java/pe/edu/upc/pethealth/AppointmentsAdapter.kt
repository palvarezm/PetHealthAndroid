package pe.edu.upc.pethealth

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_appointment.view.*
import pe.edu.upc.lib.Appointment
import pe.edu.upc.lib.AppointmentResponse

/*class AppointmentsAdapter(private val myDataset: Array<String>) :
        RecyclerView.Adapter<AppointmentsAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsAdapter.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_text_view, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}*/
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
        val date = view.dayTextView
        val hour = view.hourTextView
        val desc = view.descriptionTextView

        val veterinary = view.veterinaryTextView
        val vet = view.vetTextView

        fun updateFrom(apptResponse: AppointmentResponse){
            date.text = apptResponse.appointment.appt_date.substring(0, Math.min(apptResponse.appointment.appt_date.length, 10));

            hour.text = apptResponse.appointment.start_t.substring(11, Math.min(apptResponse.appointment.start_t.length, 16));

            desc.text = apptResponse.appointment.desc

            vet.text = apptResponse.veterinarian.name

            veterinary.text = apptResponse.veterinary.name

            /*sessionLayout.setOnClickListener { view->
                val context = view.context
                context.startActivity(
                        Intent(context, SessionActivity::class.java)
                                .putExtras(session.toBundle()))
            }*/
        }

    }
}