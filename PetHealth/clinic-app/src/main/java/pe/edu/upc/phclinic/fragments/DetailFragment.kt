package pe.edu.upc.phclinic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import pe.edu.upc.lib.ApptModel
import pe.edu.upc.phclinic.R
import pe.edu.upc.phclinic.adapters.HistoriesAdapter

class DetailFragment : Fragment() {
    private var historiesRecyclerView : RecyclerView? = null
    private var historiesAdapter : HistoriesAdapter? = null
    private var historiesLayoutManager: RecyclerView.LayoutManager? = null
    private val fragment = this


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {

        val view = inflater.inflate(R.layout.fragment_detail_appointment, container, false)

        val vetName = view?.findViewById<View>(R.id.nameTextView) as TextView?
        val race  = view?.findViewById<View>(R.id.raceTextView) as TextView?
        val pDescription  = view?.findViewById<View>(R.id.petDescriptionTextView) as TextView?
        val bDate  = view?.findViewById<View>(R.id.birthDateTextView) as TextView?
        val apptDate = view?.findViewById<View>(R.id.appointmentDateTextView) as TextView?
        val startTime = view?.findViewById<View>(R.id.startTimeTextView) as TextView?
        val endTime = view?.findViewById<View>(R.id.finishTimeTextView) as TextView?
        val apptDescription = view?.findViewById<View>(R.id.appointmentDescriptionTextView) as TextView?
        val petImageView = view?.findViewById<View>(R.id.petImageView) as ImageView?
        historiesRecyclerView = view.findViewById(R.id.historiesRecyclerView)
        historiesLayoutManager = GridLayoutManager(view.context, 1)

        val apptString = arguments!!.getString("appointment")
        val appt = Gson().fromJson<ApptModel.AppointmentResponse>(apptString)

        vetName?.text = appt.pet.name
        race?.text = appt.pet.race
        pDescription?.text = appt.pet.description
        bDate?.text = appt.pet.birth_date
        apptDate?.text = appt.appointment.appt_date
        startTime?.text = appt.appointment.start_t
        endTime?.text = appt.appointment.end_t
        apptDescription?.text = appt.appointment.desc
        Picasso.get().load(appt.pet.image_url).transform(CropCircleTransformation()).into(petImageView)
        updateHistories(appt)
        return view
    }

    private fun updateHistories(appt: ApptModel.AppointmentResponse){
        val histories = appt.pet.history
        historiesAdapter = HistoriesAdapter(fragment)
        historiesRecyclerView?.adapter = historiesAdapter
        historiesRecyclerView?.layoutManager = historiesLayoutManager
        historiesAdapter?.setHistories(histories)
        historiesAdapter?.notifyDataSetChanged()
    }

}