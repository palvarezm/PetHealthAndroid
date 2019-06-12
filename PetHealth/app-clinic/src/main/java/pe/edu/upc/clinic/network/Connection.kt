package pe.edu.upc.clinic.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telecom.ConnectionService
import android.widget.Toast

/**
 * Created by Usuario on 14/11/2017.
 */

object Connection {
    fun isOnline(context: Context): Boolean {
        val conMgr = context.getSystemService(ConnectionService.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo

        if (netInfo == null || !netInfo.isConnected || !netInfo.isAvailable) {
            Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}