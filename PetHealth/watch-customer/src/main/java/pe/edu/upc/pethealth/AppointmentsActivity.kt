package pe.edu.upc.pethealth

import android.content.SharedPreferences
import android.os.Bundle
import android.support.wear.widget.WearableLinearLayoutManager
import android.support.wearable.activity.WearableActivity

import com.google.android.gms.wearable.*
import kotlinx.android.synthetic.main.activity_appointments.*

import pe.edu.upc.lib.AppointmentResponse
import com.github.salomonbrys.kotson.*
import com.google.gson.Gson

class AppointmentsActivity : WearableActivity(), DataClient.OnDataChangedListener  {

    val APPT_KEY = "appt.key"
    val APPT_PATH = "/appt"
    val APPT_PREFS = "appt.prefs"

    lateinit var shared: SharedPreferences
    lateinit var apptsAdapter: AppointmentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)
        shared = getSharedPreferences(APPT_PREFS, 0)

        var appts = ArrayList<AppointmentResponse>()
        val apptsString = shared.getString(APPT_KEY,"")
        if (apptsString != ""){
            appts = Gson().fromJson<ArrayList<AppointmentResponse>>(apptsString)
        }
        apptsAdapter = AppointmentsAdapter(appts,AppointmentsRecyclerView.context)

        AppointmentsRecyclerView.apply {
            layoutManager = WearableLinearLayoutManager(this@AppointmentsActivity)
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
                            val editor = shared.edit()
                            editor.putString(APPT_KEY,apptsString)
                            editor.apply()
                            val apptsResponse = Gson().fromJson<ArrayList<AppointmentResponse>>(apptsString)
                            apptsAdapter.appts = apptsResponse
                            apptsAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}