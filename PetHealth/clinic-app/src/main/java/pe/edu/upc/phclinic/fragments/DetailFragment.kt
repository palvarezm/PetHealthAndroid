package pe.edu.upc.phclinic.fragments

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail_appointment.*
import kotlinx.android.synthetic.main.popup_finish_appointment.*
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

        val vetName = view?.findViewById<View>(R.id.petNameTextView) as TextView?
        val race  = view?.findViewById<View>(R.id.raceTextView) as TextView?
        val pDescription  = view?.findViewById<View>(R.id.petDescriptionTextView) as TextView?
        val bDate  = view?.findViewById<View>(R.id.birthDateTextView) as TextView?
        val apptDate = view?.findViewById<View>(R.id.appointmentDateTextView) as TextView?
        val apptDescription = view?.findViewById<View>(R.id.appointmentDescriptionTextView) as TextView?
        val petImageView = view?.findViewById<View>(R.id.petImageView) as ImageView?
        val finishApptButton = view?.findViewById<View>(R.id.finishAppointmentButton) as Button?
        historiesRecyclerView = view.findViewById(R.id.historiesRecyclerView)
        historiesLayoutManager = GridLayoutManager(view.context, 1)
        view.foreground.alpha = 0
        val apptString = arguments!!.getString("appointment")
        val appt = Gson().fromJson<ApptModel.AppointmentResponse>(apptString)

        vetName?.text = appt.pet.name
        race?.text = appt.pet.race
        pDescription?.text = appt.pet.description
        bDate?.text = appt.pet.birth_date
        apptDate?.text = appt.appointment.appt_date
        apptDescription?.text = appt.appointment.desc
        Picasso.get().load(appt.pet.image_url).transform(RoundedCornersTransformation(10, 20))
                .resize(600, 600).centerCrop().into(petImageView)
        updateHistories(appt)

        finishApptButton!!.setOnClickListener{
            val finishApptView = inflater.inflate(R.layout.popup_finish_appointment, null)
            val popupWindow = PopupWindow(finishApptView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            val buttonPopup = finishApptView.findViewById<Button>(R.id.cancelButton)
            // Create a new slide animation for popup window enter transition
            popupWindow.elevation = 20.0F
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.RIGHT
            popupWindow.exitTransition = slideOut
            TransitionManager.beginDelayedTransition(appointmentDetailConstraintLayout)
            popupWindow.showAtLocation(
                    appointmentDetailConstraintLayout, // Location to display popup window
                    Gravity.CENTER, // Exact position of layout to display popup
                    0, // X offset
                    0 // Y offset
            )

            view.foreground.alpha = 220
            buttonPopup.setOnClickListener {
                popupWindow.dismiss()
                view.foreground.alpha = 0
            }

        }
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