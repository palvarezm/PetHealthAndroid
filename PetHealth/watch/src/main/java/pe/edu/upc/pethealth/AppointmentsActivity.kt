package pe.edu.upc.pethealth

import android.content.SharedPreferences
import android.os.Bundle
import android.support.wear.widget.WearableLinearLayoutManager
import android.support.wearable.activity.WearableActivity

import com.google.android.gms.wearable.*
import kotlinx.android.synthetic.main.activity_appointments.*

import pe.edu.upc.lib.Appointment
import org.json.JSONObject
import pe.edu.upc.lib.AppointmentResponse
import pe.edu.upc.lib.AppointmentResponseBeta

class AppointmentsActivity : WearableActivity(), DataClient.OnDataChangedListener  {

    val APPT_KEY = "appt.key"
    val APPT_PATH = "/appt"

    lateinit var shared: SharedPreferences
    lateinit var apptsAdapter: AppointmentsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)
        shared = getSharedPreferences(APPT_KEY, 0)

        var appts = arrayListOf(AppointmentResponseBeta())

        //if (shared.getString(APPT_KEY,"") != ""){
        //    val json = JSONObject(shared.getString(APPT_KEY,""))
        //}

        apptsAdapter = AppointmentsAdapter(appts,AppointmentsRecyclerView.context)

        AppointmentsRecyclerView.apply {
            layoutManager = WearableLinearLayoutManager(this@AppointmentsActivity)
            adapter = apptsAdapter
        }

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
                            //val editor = shared.edit()
                            //editor.put(APPT_KEY,getDataMapArrayList(APPT_KEY))
                            //editor.apply()

                            //parse
                            val data = getDataMapArrayList(APPT_KEY)
                            val appts: ArrayList<AppointmentResponseBeta> = arrayListOf()
                            data.forEach { dataMap ->
                                appts.add(AppointmentResponseBeta(dataMap["date"],dataMap["veterinary"],dataMap["vet"],dataMap["desc"],dataMap["hour"]))
                            }
                            apptsAdapter.appts = appts
                            apptsAdapter.notifyDataSetChanged()
                        }
                    }
                }
            } else if (event.type == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }
}