package pe.edu.upc.pethealth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_my_pet_description.*
import androidx.fragment.app.Fragment

import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import pe.edu.upc.lib.models.Pet


import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.activities.MainActivity

class MyPetDescriptionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val pet: Pet = Gson().fromJson(arguments!!.getString("pet"))
        val petName = pet.name + "'s Description"
        (activity as MainActivity).setFragmentToolbar(petName, true, fragmentManager)
        val view = inflater.inflate(R.layout.fragment_my_pet_description, container, false)

        titleTextView.text = pet.name
        Picasso.get().load(pet.image_url).into(petImageView)
        raceTextView.text = pet.race
        descriptionValueTextView.text = pet.description
        return view
    }
}
