package pe.edu.upc.clinic.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.appointment_content.*
import org.w3c.dom.Text
import pe.edu.upc.clinic.R
import pe.edu.upc.clinic.persistance.SharedPreferencesManager
import pe.edu.upc.lib.AppointmentResponse
import pe.edu.upc.lib.User


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *b
 */
class DetailFragment : Fragment() {
    private var user: User? = null
    private var sharedPreferencesManager : SharedPreferencesManager? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {

        val view = inflater.inflate(R.layout.appointment_content, container, false)

        val vetName = view?.findViewById<View>(R.id.nameTextView) as TextView?
        val race  = view?.findViewById<View>(R.id.raceTextView) as TextView?
        val pDescription  = view?.findViewById<View>(R.id.petDescriptionTextView) as TextView?
        val bDate  = view?.findViewById<View>(R.id.birthDateTextView) as TextView?
        val apptDate = view?.findViewById<View>(R.id.appointmentDateTextView) as TextView?
        val startTime = view?.findViewById<View>(R.id.startTimeTextView) as TextView?
        val endTime = view?.findViewById<View>(R.id.finishTimeTextView) as TextView?
        val apptDescription = view?.findViewById<View>(R.id.appointmentDescriptionTextView) as TextView?
        val petImageView = view?.findViewById<View>(R.id.petImageView) as ImageView?

        val apptString = arguments!!.getString("appointment")
        val appt = Gson().fromJson<AppointmentResponse>(apptString)

        Log.d("alv", appt.pet.name)
        Log.d("alv", appt.pet.race)
        Log.d("alv", appt.pet.description)
        Log.d("alv", appt.pet.birth_date)
        Log.d("alv", appt.appointment.appt_date)

        vetName?.text = appt.pet.name
        race?.text = appt.pet.race
        pDescription?.text = appt.pet.description
        bDate?.text = appt.pet.birth_date
        apptDate?.text = appt.appointment.appt_date
        startTime?.text = appt.appointment.start_t
        endTime?.text = appt.appointment.end_t
        apptDescription?.text = appt.appointment.desc
        Picasso.get().load(appt.pet.image_url).transform(CropCircleTransformation()).into(petImageView)
        return view
    }



}