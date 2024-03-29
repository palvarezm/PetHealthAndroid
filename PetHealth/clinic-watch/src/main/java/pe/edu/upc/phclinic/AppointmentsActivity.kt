package pe.edu.upc.phclinic

import android.content.SharedPreferences
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import androidx.core.content.edit
import androidx.wear.widget.WearableLinearLayoutManager

import com.google.android.gms.wearable.*
import kotlinx.android.synthetic.main.activity_appointments.*

import pe.edu.upc.lib.models.ApptModel.ApptResponse
import com.github.salomonbrys.kotson.*
import com.google.gson.Gson

class AppointmentsActivity : WearableActivity(), DataClient.OnDataChangedListener  {

    val APPT_KEY = "appointment.key"
    val APPT_PATH = "/appointment"
    val APPT_PREFS = "appointment.prefs"

    lateinit var shared: SharedPreferences
    lateinit var apptsAdapter: AppointmentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)
        shared = getSharedPreferences(APPT_PREFS, 0)

        var appts = ArrayList<ApptResponse>()
        val apptsString = shared.getString(APPT_KEY,"")
        if (apptsString != ""){
            appts = Gson().fromJson(apptsString)
        }
        apptsAdapter = AppointmentsAdapter(appts,AppointmentsRecyclerView.context)
        AppointmentsRecyclerView.apply {
            isEdgeItemsCenteringEnabled = true
            layoutManager = WearableLinearLayoutManager(this@AppointmentsActivity).apply {
                stackFromEnd = true
            }
            adapter = apptsAdapter
        }

        apptsAdapter.notifyDataSetChanged()
        setAmbientEnabled()

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
                            val apptsString = getString(APPT_KEY)
                            shared.edit(true){putString(APPT_KEY,apptsString)}
                            val apptsResponse = Gson().fromJson<ArrayList<ApptResponse>>(apptsString)
                            apptsAdapter.appts = apptsResponse
                            apptsAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}