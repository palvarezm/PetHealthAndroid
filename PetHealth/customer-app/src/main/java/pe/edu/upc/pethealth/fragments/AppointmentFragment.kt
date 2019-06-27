package pe.edu.upc.pethealth.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager

import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.android.synthetic.main.fragment_appointment.*
import pe.edu.upc.lib.models.ApptModel

import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.activities.MainActivity
import pe.edu.upc.pethealth.adapters.AppointmentAdapter
import pe.edu.upc.pethealth.network.RestClient
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class AppointmentFragment : Fragment() {

    companion object{
        val APPT_KEY = "appointment.key"
        val APPT_PATH = "/appointment"
    }
    private lateinit var appointmentsAdapter: AppointmentAdapter
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var dataClient: DataClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.context!!)
        val view = inflater.inflate(R.layout.fragment_appointment, container, false)

        (activity as MainActivity).setFragmentToolbar("Next Appointments", true, fragmentManager!!)
        dataClient = Wearable.getDataClient(this.context!!)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appointmentsAdapter = AppointmentAdapter()
        appointmentRecyclerView.apply {
            adapter = appointmentsAdapter
            layoutManager = GridLayoutManager(view.context, 1)
        }
        updateAppointment()
    }

    override fun onResume() {
        super.onResume()
        updateAppointment()
    }

    internal fun sendWearData(appt: ArrayList<ApptModel.ApptResponse>) {

        val putDataMapReq = PutDataMapRequest.create(APPT_PATH)
        putDataMapReq.dataMap.putString(APPT_KEY, appt.toString())
        val putDataReq = putDataMapReq.asPutDataRequest().setUrgent()
        dataClient.putDataItem(putDataReq)
    }

    private fun updateAppointment() {
        val call = RestClient().service.getAppts(sharedPreferencesManager.accessToken!!, sharedPreferencesManager.user!!.id)
        call.enqueue(object : Callback<ApptModel.Response> {
            override fun onResponse(call: Call<ApptModel.Response>, response: Response<ApptModel.Response>) {
                val appts = response.body()!!.data
                sendWearData(appts)
                appointmentsAdapter.appts = appts
                appointmentsAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ApptModel.Response>, t: Throwable) {
                Log.d("FAILURE", t.toString())
            }
        })
    }
}
