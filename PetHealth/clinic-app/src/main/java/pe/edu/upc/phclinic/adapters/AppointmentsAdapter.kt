package pe.edu.upc.phclinic.adapters

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import pe.edu.upc.phclinic.fragments.DetailFragment
import pe.edu.upc.phclinic.R

class AppointmentAdapters(private val fragment: Fragment) : RecyclerView.Adapter<AppointmentAdapters.ViewHolder>() {
    override fun getItemCount(): Int {
        return cardInfo!!.size()
    }

    private var cardInfo: JsonArray? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jsonCardInfo = cardInfo!!.get(position).asJsonObject
        val fullDate = jsonCardInfo.get("appointment").asJsonObject.get("appt_date").asString
        val petPhoto = jsonCardInfo.get("pet").asJsonObject.get("image_url").asString

        holder.veterinaryTextView.text = jsonCardInfo.get("veterinary").asJsonObject.get("name").asString
        holder.vetTextView.text = jsonCardInfo.get("veterinarian").asJsonObject.get("name").asString
        holder.dateTextView.text = fullDate.substring(0, Math.min(fullDate.length, 10))
        holder.descriptionTextView.text = jsonCardInfo.get("appointment").asJsonObject.get("desc").asString
        Picasso.get().load(petPhoto).transform(RoundedCornersTransformation(10, 20))
                .resize(430, 410).into(holder.petImageView)

    }

    fun setAppointments(cardInfo: JsonArray) {
        this.cardInfo = cardInfo
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var vetTextView: TextView
        internal var veterinaryTextView: TextView
        internal var dateTextView: TextView
        internal var descriptionTextView: TextView
        internal var petImageView: ImageView

        init {
            vetTextView = itemView.findViewById<View>(R.id.vetTextView) as TextView
            veterinaryTextView = itemView.findViewById<View>(R.id.veterinaryTextView) as TextView
            dateTextView = itemView.findViewById<View>(R.id.dateTextView) as TextView
            descriptionTextView = itemView.findViewById<View>(R.id.descriptionTextView) as TextView
            petImageView = itemView.findViewById<View>(R.id.petImageView) as ImageView


            itemView.setOnClickListener { v: View ->
                var position: Int = adapterPosition
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