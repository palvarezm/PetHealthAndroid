package pe.edu.upc.pethealth.network

import com.google.gson.JsonObject
import org.json.JSONObject


import pe.edu.upc.lib.models.ApptModel
import pe.edu.upc.lib.models.VeterinaryModel
import retrofit2.Call
import retrofit2.http.*

interface PetHealthServices {
    @POST(EndpointUrls.LOGIN)
    fun login(@Body requestBody: JsonObject): Call<RestView<JsonObject>>

    @GET(EndpointUrls.APPOINTMENTS)
    fun getAppts(@Header("access_token") accessToken: String, @Path("user_id") userId: Int): Call<ApptModel.Response>

    @GET(EndpointUrls.VETERINARIES)
    fun getCloseVeterinaries(@Header("access_token") accessToken: String, @Query("latitude") latitude: Double?, @Query("longitude") longitude: Double?): Call<VeterinaryModel.Response>

    @PUT(EndpointUrls.EDIT_USER)
    fun editProfileData(@Header("access_token") accessToken: String, @Path("user_id") id: Int, @Body requestBody: JSONObject): Call<RestView<JsonObject>>
}
