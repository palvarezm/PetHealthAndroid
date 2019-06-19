package pe.edu.upc.pethealth

import pe.edu.upc.lib.GoogleMapResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PethealthAPIInterface {

    @GET("/maps/api/directions/json")
    fun getDirections(
            @Query("origin") origin: String,
            @Query("destination") destination: String,
            @Query("key") key: String): Call<GoogleMapResponse>

}