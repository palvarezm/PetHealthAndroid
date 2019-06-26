package pe.edu.upc.phclinic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

import kotlinx.android.synthetic.main.fragment_profile.*
import pe.edu.upc.lib.models.User
import pe.edu.upc.phclinic.R
import pe.edu.upc.phclinic.persistance.SharedPreferencesManager
import pe.edu.upc.lib.Veterinary


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {
    private var veterinary: Veterinary? = null
    private var user : User? = null
    private var sharedPreferencesManager: SharedPreferencesManager? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager.getInstance(context!!)
        veterinary = sharedPreferencesManager?.veterinary
        user = sharedPreferencesManager?.user
        updateProfile()
        Picasso.get().load(sharedPreferencesManager!!.user!!.photo).error(R.mipmap.ic_launcher)
                .transform(RoundedCornersTransformation(10, 0))
                .resize(250, 170)
                .centerCrop()
                .into(vetImageView)
    }

    private fun updateProfile() {
        nameTextView.text = veterinary?.name
        contactTextView.text = veterinary?.phone
        addressTextView.text = user?.mail
        scheduleTextView.text = veterinary?.opening_hours
    }

}