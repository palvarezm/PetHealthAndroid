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
    private var latitudString: Double = 0.0
    private var longitudString: Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var veterinaryName: String
    private lateinit var retrofit: Retrofit
    private val LOCATION_REQUEST_CODE = 101

    public override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        // Enables always on.
        setAmbientEnabled()

        setContentView(R.layout.activity_maps)
        latitudString = intent.extras.getDouble("latitude")
        longitudString = intent.extras.getDouble("longitude")
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
        // Map is ready to be used.
        mMap = googleMap
        val service = retrofit.create<PethealthAPIInterface>(PethealthAPIInterface::class.java)

        val permission = checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_REQUEST_CODE)
        }
        // Inform user how to close app (Swipe-To-Close).
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, R.string.intro_text, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()

        // Adds a marker in Sydney, Australia and moves the camera.
        val veterinary = LatLng(latitudString, longitudString)

        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    val origin = LatLng(location!!.latitude,location!!.longitude)
                    service.getDirections(
                            "${origin.latitude},${origin.longitude}",
                            "${veterinary.latitude},${veterinary.longitude}",
                            "AIzaSyCksfpj6rpuIkIuC7WsD3AjjKl6cV-UqpM")
                            .enqueue(object : Callback<GoogleMapResponse> {
                        override fun onResponse(call: Call<GoogleMapResponse>?, response: Response<GoogleMapResponse>?){
                            val path =  ArrayList<LatLng>()

                            for (i in 0 until response!!.body()!!.routes[0].legs[0].steps.size){
                                path.addAll(decodePolyline(response!!.body()!!.routes[0].legs[0].steps[i].polyline.points))
                            }
                            val rectLine = PolylineOptions().width(3f).color(
                                    Color.BLUE)

                            for (i in 0 until path.size) {
                                rectLine.add(path[i])
                            }
                            googleMap.addPolyline(rectLine)
                        }

                        override fun onFailure(call: Call<GoogleMapResponse>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }


        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.uiSettings.isZoomControlsEnabled= true
        mMap.addMarker(MarkerOptions().position(veterinary).title(veterinaryName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(veterinary,13f))

    }
    private fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }
    private fun requestPermission(permissionType: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionType), requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Unable to show location - permission required", Toast.LENGTH_LONG).show()
                } else {
                    val mapFragment = map as MapFragment
                    mapFragment.getMapAsync(this)

                }
            }
        }
    }
}
