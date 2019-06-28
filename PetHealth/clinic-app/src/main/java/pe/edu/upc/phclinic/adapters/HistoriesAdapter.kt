package pe.edu.upc.phclinic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pe.edu.upc.lib.models.ClinicHistory
import pe.edu.upc.phclinic.R


class HistoriesAdapter(private val fragment: Fragment) : RecyclerView.Adapter<HistoriesAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        return cardInfo!!.size
    }

    private var cardInfo: ArrayList<ClinicHistory>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_historial, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jsonCardInfo = cardInfo!![position]

        holder.motiveTextView.text = jsonCardInfo.motive
        holder.diagnosisTextView.text = jsonCardInfo.diagnosis

    }

    fun setHistories(cardInfo: ArrayList<ClinicHistory>) {
        this.cardInfo = cardInfo
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var motiveTextView: TextView
        internal var diagnosisTextView: TextView


        init {
            motiveTextView = itemView.findViewById<View>(R.id.motiveTextView) as TextView
            diagnosisTextView = itemView.findViewById<View>(R.id.diagnosisTextView) as TextView



        }
    }
}