package pe.upc.watch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pe.upc.lib.Appointment

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
class AppointmentsAdapter(var appts: ArrayList<Appointment>
) : RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_appointment, parent, false)
        )
    }

    override fun getItemCount() = appts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val junction = appts.get(position)
        holder.updateFrom(junction)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*val firstAvenueTextView = view.firstAvenueTextView
        val secondAvenueTextView = view.secondAvenueTextView

        val dateTextView = view.dateTexView
        val sessionLayout = view.item_session
        */
        fun updateFrom(appt: Appointment){
            /*firstAvenueTextView.text = session.avenue_first
            secondAvenueTextView.text = session.avenue_second
            var year = session.date.subSequence(0,4).toString()
            var month = session.date.subSequence(5,7).toString()
            var day = session.date.subSequence(8,10).toString()
            dateTextView.text = day + "/" + month +"/"+year
            sessionLayout.setOnClickListener { view->
                val context = view.context
                context.startActivity(
                        Intent(context, SessionActivity::class.java)
                                .putExtras(session.toBundle()))
            }*/
        }

    }
}