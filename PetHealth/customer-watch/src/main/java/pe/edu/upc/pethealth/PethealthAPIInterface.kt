package pe.edu.upc.pethealth

import pe.edu.upc.lib.models.ApptModel
import pe.edu.upc.lib.models.GoogleMapModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PethealthAPIInterface {

    @GET("/maps/api/directions/json")
    fun getDirections(
            @Query("origin") origin: String,
            @Query("destination") destination: String,
            @Query("key") key: String): Call<GoogleMapModel.Response
            >
    @GET("/users/{user_id}/appointments")
    fun getAppts(@Header("access_token") accessToken: String, @Path("user_id") userId: Int): Call<ApptModel.Response>

}