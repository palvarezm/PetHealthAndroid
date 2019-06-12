package pe.edu.upc.clinic.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener

import kotlinx.android.synthetic.main.fragment_profile.*
import pe.edu.upc.clinic.R
import pe.edu.upc.clinic.persistance.SharedPreferencesManager
import pe.edu.upc.lib.Person


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {
    private var person: Person? = null

    private var sharedPreferencesManager: SharedPreferencesManager? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(context!!)
        person = sharedPreferencesManager?.person
        vetImageView.visibility = View.INVISIBLE

        updateProfile()
        sharedPreferencesManager?.user?.photo?.let { loadImage(it) }

        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    private fun updateProfile() {
        nameTextView.text = person?.name
        contactTextView.text = person?.phone
        addressTextView.text = person?.address
    }

    private fun loadImage(imageUrl: String) {
        AndroidNetworking.get(imageUrl)
                .setTag("imageRequest")
                .setPriority(Priority.MEDIUM)
                .setBitmapMaxHeight(120)
                .setBitmapMaxWidth(120)
                .build()
                .getAsBitmap(object : BitmapRequestListener {
                    override fun onResponse(response: Bitmap) {
                        vetImageView.setImageBitmap(response)
                        vetImageView.visibility = View.VISIBLE
                    }

                    override fun onError(anError: ANError) {
                        println(anError.toString())
                    }
                })
    }
}