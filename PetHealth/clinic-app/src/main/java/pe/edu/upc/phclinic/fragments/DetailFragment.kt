package pe.edu.upc.phclinic.fragments

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_detail_appointment.*
import pe.edu.upc.lib.models.ApptModel.ApptResponse
import pe.edu.upc.phclinic.R
import pe.edu.upc.phclinic.adapters.HistoriesAdapter

class DetailFragment(var appt: ApptResponse) : Fragment() {

    private lateinit var historiesAdapter : HistoriesAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_appointment, container, false)
        view.foreground.alpha = 0
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apptVetTextView.text = appt.pet.name
        petRaceTextView.text = appt.pet.race
        petDescTextView.text = appt.pet.description
        petBirthTextView.text = appt.pet.birth_date
        apptDateTextView.text = appt.appointment.appt_date
        apptDescTextView.text = appt.appointment.desc
        Picasso.get().load(appt.pet.image_url).transform(RoundedCornersTransformation(10, 20))
                .resize(600, 600).centerCrop().into(petImageView)
        updateHistories(appt)
        apptFinishButton.setOnClickListener{
            val finishApptView = LayoutInflater.from(this.activity).inflate(R.layout.popup_finish_appointment, null)
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
    }

    private fun updateHistories(appt: ApptResponse){
        val histories = appt.pet.history
        historiesAdapter = HistoriesAdapter(this)
        historiesRecyclerView.adapter = historiesAdapter
        historiesRecyclerView.layoutManager = GridLayoutManager(this.context, 1)
        historiesAdapter.setHistories(histories)
        historiesAdapter.notifyDataSetChanged()
    }

}