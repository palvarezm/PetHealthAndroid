package pe.edu.upc.pethealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.squareup.picasso.Picasso
import pe.edu.upc.lib.models.Pet

import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.activities.MainActivity
import pe.edu.upc.pethealth.fragments.MyPetDescriptionFragment

/**
 * Created by genob on 18/09/2017.
 */

class PetsAdapter(var pets: ArrayList<Pet>?) : RecyclerView.Adapter<PetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_pets, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pet = pets!![position]
        holder.petNameTextView.text = pets!![position].name
        holder.petDescriptionTextView.text = pets!![position].description
        Picasso.get().load(pets!![position].image_url).error(R.mipmap.ic_launcher).into(holder.petPhoto)
        holder.moreTextView.setOnClickListener { view ->
            val context = view.context as MainActivity
            val newFragment = MyPetDescriptionFragment()
            newFragment.arguments?.putString("pet",pet.toString())
            context.supportFragmentManager.beginTransaction()
                    .replace(R.id.content, newFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    override fun getItemCount(): Int {
        return pets!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var petNameTextView: TextView = itemView.findViewById<View>(R.id.titleTextView) as TextView
        internal var petPhoto: ImageView = itemView.findViewById<View>(R.id.myPetImageView) as ImageView
        internal var petDescriptionTextView: TextView = itemView.findViewById<View>(R.id.myPetDescriptionTextView) as TextView
        internal var moreTextView: TextView = itemView.findViewById<View>(R.id.moreTextView) as TextView
    }
}
