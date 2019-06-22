package pe.edu.upc.pethealth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.wear.widget.SwipeDismissFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_maps.*
import pe.edu.upc.lib.GoogleMapResponse
import pe.edu.upc.lib.decodePolyline
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsActivity : WearableActivity(), OnMapReadyCallback {

    /**
     * Map is initialized when it's fully loaded and ready to be used.
     * See [onMapReady]
     */
    private lateinit var mMap: GoogleMap
    private lateinit var destination: LatLng
    private var origin = LatLng(-12.0874509,-77.0499422)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var veterinaryName: String
    private lateinit var retrofit: Retrofit

    companion object{
        private val LOCATION_REQUEST_CODE = 101
        private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }
    public override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        // Enables always on.
        setAmbientEnabled()

        setContentView(R.layout.activity_maps)
        destination = LatLng(intent.extras.getDouble("latitude"),intent.extras.getDouble("longitude"))
        veterinaryName = intent.extras.getString("veterinaryName")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        swipe_dismiss_root_container.addCallback(object : SwipeDismissFrameLayout.Callback() {
            override fun onDismissed(layout: SwipeDismissFrameLayout?) {
                layout?.visibility = View.GONE
                finish()
            }
        })

        swipe_dismiss_root_container.setOnApplyWindowInsetsListener { _, insetsArg ->
            val insets = swipe_dismiss_root_container.onApplyWindowInsets(insetsArg)
            val params = map_container.layoutParams as FrameLayout.LayoutParams
            params.setMargins(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
            map_container.layoutParams = params
            insets
        }

        val mapFragment = map as MapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.uiSettings.isZoomControlsEnabled= true
        mMap.addMarker(MarkerOptions().position(destination).title(veterinaryName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination,13f))

        val toast = Toast.makeText(applicationContext, R.string.intro_text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()

        getDeviceLocation()

    }
    private fun getDeviceLocation(){

        val permission = checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(FINE_LOCATION, COARSE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    location?.let { origin = LatLng(it.latitude, it.longitude) }
                    getDirections(mMap,origin,destination)
                }
    }
    private fun getDirections(map: GoogleMap, orig: LatLng, dest: LatLng){
        val service = retrofit.create(PethealthAPIInterface::class.java)
        service.getDirections(
                "${orig.latitude},${orig.longitude}",
                "${dest.latitude},${dest.longitude}",
                "AIzaSyBqSMe-H7GhXqYN6X0oMLiaZ62LvyJ7lnM")
                .enqueue(object : Callback<GoogleMapResponse> {
                    override fun onResponse(call: Call<GoogleMapResponse>?, response: Response<GoogleMapResponse>?){
                        val path =  ArrayList<LatLng>()
                        if (response!!.body()!!.routes.isEmpty()) return
                        response!!.body()!!.routes[0].legs[0].steps.forEach {step ->
                            val latLngs = decodePolyline(step.polyline.points).map { LatLng(it.first,it.second) }
                            path.addAll(latLngs)

                        }
                        val rectLine = PolylineOptions().width(3f).color(Color.BLUE)
                        path.forEach { rectLine.add(it) }
                        map.addPolyline(rectLine)
                    }

                    override fun onFailure(call: Call<GoogleMapResponse>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Unable to show location - permission required", Toast.LENGTH_LONG).show()
                } else {
                    getDeviceLocation()
                }
            }
        }
    }
}
