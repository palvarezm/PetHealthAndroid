package pe.edu.upc.phclinic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_detail_appointment.*
import pe.edu.upc.lib.models.ApptModel.ApptResponse
import pe.edu.upc.phclinic.R
import pe.edu.upc.phclinic.adapters.HistoriesAdapter

class DetailFragment(var appt: ApptResponse) : Fragment() {

    private lateinit var historiesAdapter : HistoriesAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {

        val view = inflater.inflate(R.layout.fragment_detail_appointment, container, false)

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