package pe.upc.watch

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.wear.widget.WearableLinearLayoutManager
import android.support.wear.widget.WearableRecyclerView
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_appointments.*
import pe.upc.lib.Appointment

class AppointmentsActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        var appts = arrayListOf(Appointment(),Appointment())
        setAmbientEnabled()
        AppointmentsRecyclerView.apply {
            layoutManager = WearableLinearLayoutManager(this@AppointmentsActivity)
            adapter = AppointmentsAdapter(appts)
        }
    }
}
