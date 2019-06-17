package pe.edu.upc.phclinic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


internal open class LoggerCallback<T> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        Log.d("Logger Response", response.toString())
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        Log.d("Logger Response", t.toString())
    }
}