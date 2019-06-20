package pe.edu.upc.pethealth.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

import java.util.ArrayList

import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.activities.MainActivity
import pe.edu.upc.pethealth.adapters.AppointmentAdapters
import pe.edu.upc.pethealth.network.LoggerCallback
import pe.edu.upc.pethealth.network.PetHealthApiService
import pe.edu.upc.pethealth.network.RestClient
import pe.edu.upc.pethealth.network.RestView
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class AppointmentFragment : Fragment() {
    private var appointmentRecyclerView: RecyclerView? = null
    private var appointmentAdapters: AppointmentAdapters? = null
    private var appointmentLayoutManager: RecyclerView.LayoutManager? = null
    private val fragment = this

    private var answer: RestView<JsonArray>? = null
    private var sharedPreferencesManager: SharedPreferencesManager? = null

    internal lateinit var dataClient: DataClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.context)
        val view = inflater.inflate(R.layout.fragment_appointment, container, false)
        updateAppointment()
        appointmentRecyclerView = view.findViewById<View>(R.id.appointmentRecyclerView) as RecyclerView
        appointmentLayoutManager = GridLayoutManager(view.context, 1)
        (activity as MainActivity).setFragmentToolbar("Next Appointments", true, fragmentManager)
        dataClient = Wearable.getDataClient(this.context!!)
        return view
    }

    override fun onResume() {
        super.onResume()
        updateAppointment()
    }

    internal fun sendWearData(jsonArray: JsonArray) {
        val APPT_KEY = "appt.key"
        val APPT_PATH = "/appt"
        val response = Gson().toJson(jsonArray, JsonArray::class.java)
        val putDataMapReq = PutDataMapRequest.create(APPT_PATH)
        putDataMapReq.dataMap.putString(APPT_KEY, response)
        val putDataReq = putDataMapReq.asPutDataRequest().setUrgent()
        dataClient.putDataItem(putDataReq)
    }

    private fun updateAppointment() {
        val call = RestClient().webServices.getAppts(sharedPreferencesManager!!.accessToken, sharedPreferencesManager!!.user!!.id)
        call.enqueue(object : LoggerCallback<RestView<JsonArray>>() {
            override fun onResponse(call: Call<RestView<JsonArray>>, response: Response<RestView<JsonArray>>) {
                super.onResponse(call, response)
                answer = response.body()
                sendWearData(answer!!.data)
                appointmentAdapters = AppointmentAdapters(fragment)
                appointmentRecyclerView!!.adapter = appointmentAdapters
                appointmentRecyclerView!!.layoutManager = appointmentLayoutManager
                appointmentAdapters!!.setAppointments(answer!!.data)
                appointmentAdapters!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<RestView<JsonArray>>, t: Throwable) {
                super.onFailure(call, t)
                Log.d("FAILURE", t.toString())
            }
        })
    }
}// Required empty public constructor
