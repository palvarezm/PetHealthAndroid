package pe.edu.upc.phclinic.fragments

import android.graphics.drawable.ColorDrawable
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
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_detail_appointment.*
import pe.edu.upc.lib.models.ApptModel.ApptResponse
import pe.edu.upc.phclinic.R
import pe.edu.upc.phclinic.adapters.HistoriesAdapter
import pe.edu.upc.phclinic.graphics.CircleTransform

class DetailFragment(var apptResponse: ApptResponse) : Fragment() {

    private lateinit var historiesAdapter : HistoriesAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_appointment, container, false)
        historiesAdapter = HistoriesAdapter(fragment = this)

        this.activity!!.window.decorView.foreground = ColorDrawable(0xCC000000.toInt())
        this.activity!!.window.decorView.foreground.alpha = 0
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historiesRecyclerView.apply {
            adapter = historiesAdapter
            layoutManager = GridLayoutManager(this.context, 1)
        }
        petNameTextView.text = apptResponse.pet.name
        petRaceTextView.text = apptResponse.pet.race
        petDescTextView.text = apptResponse.pet.description
        petBirthTextView.text = apptResponse.pet.birth_date

        apptTypeTextView.text = apptResponse.appointment.type
        apptDateTextView.text = apptResponse.appointment.appt_date
        apptDescTextView.text = apptResponse.appointment.desc
        apptVetTextView.text = apptResponse.veterinarian.name

        Picasso.get().load(apptResponse.pet.image_url)
                .transform(CircleTransform())
                .into(petImageView)
        updateHistories(apptResponse)
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
            this.activity!!.window.decorView.foreground.alpha = 220
            buttonPopup.setOnClickListener {
                popupWindow.dismiss()
                this.activity!!.window.decorView.foreground.alpha = 0
            }

        }
    }

    private fun updateHistories(appt: ApptResponse){
        val histories = appt.pet.history
        historiesAdapter.histories = histories
        historiesAdapter.notifyDataSetChanged()
    }

}