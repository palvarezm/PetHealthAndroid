package pe.upc.watch

import android.os.Bundle
import android.support.wear.widget.WearableLinearLayoutManager
import android.support.wearable.activity.WearableActivity
import android.util.Log
import com.google.android.gms.wearable.*
import kotlinx.android.synthetic.main.activity_appointments.*
import pe.upc.lib.Appointment

class AppointmentsActivity : WearableActivity(), DataClient.OnDataChangedListener  {

    val APPT_KEY = "appt.key"
    val APPT_PATH = "/appt"

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
    override fun onResume() {
        super.onResume()
        Wearable.getDataClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            // DataItem changed
            if (event.type == DataEvent.TYPE_CHANGED) {
                event.dataItem.also { item ->
                    if (item.uri.path.compareTo(APPT_PATH) == 0) {
                        DataMapItem.fromDataItem(item).dataMap.apply {
                            Log.d("taggy",getString(APPT_KEY))
                        }
                    }
                }
            } else if (event.type == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }
}
