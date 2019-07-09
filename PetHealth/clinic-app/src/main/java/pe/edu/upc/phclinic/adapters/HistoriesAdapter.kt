package pe.edu.upc.phclinic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pe.edu.upc.lib.models.ClinicHistory
import pe.edu.upc.phclinic.R


class HistoriesAdapter(var histories: ArrayList<ClinicHistory> = ArrayList(), val fragment: Fragment) : RecyclerView.Adapter<HistoriesAdapter.ViewHolder>() {


    override fun getItemCount(): Int = histories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_historial, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val row = histories[position]

        holder.motiveTextView.text = row.motive
        holder.diagnosisTextView.text = row.diagnosis
        if (position == histories.size -1) holder.historySegmentView.visibility = INVISIBLE

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var motiveTextView: TextView = itemView.findViewById<View>(R.id.motiveTextView) as TextView
        var diagnosisTextView: TextView = itemView.findViewById<View>(R.id.diagnosisTextView) as TextView
        var historySegmentView: View = itemView.findViewById(R.id.historySegmentView) as View
    }
}