package pe.edu.upc.clinic.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonArray
import pe.edu.upc.clinic.adapters.AppointmentAdapters
import pe.edu.upc.clinic.network.LoggerCallback
import pe.edu.upc.clinic.network.RestView
import pe.edu.upc.clinic.persistance.SharedPreferencesManager
import pe.edu.upc.clinic.R
import pe.edu.upc.clinic.network.RestClient
import retrofit2.Call
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *b
 */
class AppointmentsFragment : Fragment() {

    private var appointmentRecyclerView: RecyclerView? = null
    private var appointmentAdapters: AppointmentAdapters? = null
    private var appointmentLayoutManager: RecyclerView.LayoutManager? = null
    private val fragment = this

    private var answer: RestView<JsonArray>? = null
    private var sharedPreferencesManager: SharedPreferencesManager? = null



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //this.sharedPreferencesManager = this.context?.let { SharedPreferencesManager.getInstance(it) }
        val view = inflater.inflate(R.layout.fragment_appointments, container, false)
        updateAppointment()
        appointmentRecyclerView = view.findViewById(R.id.appointmentRecyclerView)
        appointmentLayoutManager = GridLayoutManager(view.context, 1)
        return view
    }

    override fun onResume() {
        super.onResume()
        updateAppointment()
    }

    private fun updateAppointment() {
        val call = RestClient().webServices
                .getAppts(/*sharedPreferencesManager?.accessToken*/ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjUsImlhdCI6MTU2MDAyMTY4OX0.2Oqucz1lSZVQswd8EVeRpBQTWgsGU7xzPaqyKhRbxQU", /*sharedPreferencesManager?.user?.id*/ 25 )
        call.enqueue(object : LoggerCallback<RestView<JsonArray>>() {
            override fun onResponse(call: Call<RestView<JsonArray>>, response: Response<RestView<JsonArray>>) {
                super.onResponse(call, response)
                answer = response.body()
                appointmentAdapters = AppointmentAdapters(fragment)
                appointmentRecyclerView?.adapter = appointmentAdapters
                appointmentRecyclerView?.layoutManager = appointmentLayoutManager
                answer?.data?.let { appointmentAdapters?.setAppointments(it) }
                appointmentAdapters?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<RestView<JsonArray>>, t: Throwable) {
                super.onFailure(call, t)
                Log.d("FAILURE", t.toString())
            }
        })

    }

}