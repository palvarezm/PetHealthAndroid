package pe.edu.upc.pethealth.network

import com.google.gson.JsonObject


import pe.edu.upc.lib.models.ApptModel
import pe.edu.upc.lib.models.VeterinaryModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PetHealthServices {
    @POST(EndpointUrls.LOGIN)
    fun login(@Body requestBody: JsonObject): Call<RestView<JsonObject>>

    @POST(EndpointUrls.APPOINTMENTS)
    fun getAppts(@Header("access_token") accessToken: String, @Path("user_id") userId: Int): Call<ApptModel.Response>

    @GET(EndpointUrls.VETERINARIES)
    fun getCloseVeterinaries(@Header("access_token") accessToken: String, @Query("latitude") latitude: Double?, @Query("longitude") longitude: Double?): Call<VeterinaryModel.Response>
}
