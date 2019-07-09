package pe.edu.upc.pethealth.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.ArrayList

import pe.edu.upc.lib.models.VeterinaryModel
import pe.edu.upc.lib.models.VeterinaryModel.VeterinaryDistance
import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.activities.MainActivity
import pe.edu.upc.pethealth.network.RestClient
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */

class SearchFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private val DEFAULT_ZOOM = 12f
        private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        private val LOCATION_REQUEST_CODE = 1234
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var veterinariesArray: ArrayList<VeterinaryDistance>
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var currentLatLng = LatLng(-12.0874509,-77.0499422)
    private lateinit var call: Call<VeterinaryModel.Response>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setFragmentToolbar(getString(R.string.toolbar_title_search_veterinaries), true, fragmentManager!!)
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context!!)
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.context!!)
        veterinariesArray = ArrayList()
        mapFragment = childFragmentManager.findFragmentById(R.id.veterinariesMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        getDeviceLocation()
        getVeterinaries()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.context!!, "Unable to show location - permission required", Toast.LENGTH_LONG).show()
                } else {
                    getDeviceLocation()
                }
            }
        }
    }

    private fun getVeterinaries() {
        call = RestClient().service.getCloseVeterinaries(sharedPreferencesManager.accessToken!!, currentLatLng.latitude, currentLatLng.longitude)
        call.enqueue(object : Callback<VeterinaryModel.Response> {
            override fun onResponse(call: Call<VeterinaryModel.Response>, response: Response<VeterinaryModel.Response>) {
                response.body()!!.data.forEach { veterinariesArray.add(it) }
                setVeterinariesMarkers(veterinariesArray)
            }

            override fun onFailure(call: Call<VeterinaryModel.Response>, t: Throwable) {
                Log.d("FAILURE", t.toString())
            }
        })
    }

    private fun getDeviceLocation() {
        val fineLocationPermission = checkSelfPermission(this.context!!, FINE_LOCATION)
        val coarseLocationPermission = checkSelfPermission(this.context!!, COARSE_LOCATION)
        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

        } else {
            ActivityCompat.requestPermissions(this.activity!!, arrayOf(FINE_LOCATION, COARSE_LOCATION), LOCATION_REQUEST_CODE)
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {location: Location? ->
            location?.let { currentLatLng = LatLng(it.latitude,it.longitude) }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
    }

    private fun setVeterinariesMarkers(veterinariesArray: ArrayList<VeterinaryDistance>) {
        veterinariesArray.forEach {
            val veterinary = it.veterinary
            val distance = it.distance
            val veterinaryLocation = LatLng(veterinary.latitude, veterinary.longitude)
            val markerIcon = getMarkerIconFromDrawable(ResourcesCompat.getDrawable(resources, R.mipmap.pin, null)!!)
            val markerOptions = MarkerOptions().position(veterinaryLocation).title(veterinary.name).snippet(String.format("%.2f", distance) + " Km.").icon(markerIcon)
            val marker = mMap.addMarker(markerOptions)
            marker.tag = veterinary
        }

    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onStop() {
        super.onStop()
        call.cancel()
    }
}
