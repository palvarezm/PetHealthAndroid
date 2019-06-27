package pe.edu.upc.phclinic.network

import com.google.gson.JsonObject
import pe.edu.upc.lib.models.ApptModel
import pe.edu.upc.phclinic.network.RestClient.Companion.APPOINTMENTS
import pe.edu.upc.phclinic.network.RestClient.Companion.LOGIN
import pe.edu.upc.phclinic.network.RestClient.Companion.SIGNUP_USER
import retrofit2.Call
import retrofit2.http.*

interface PetHealthServices {
    @POST(LOGIN)
    fun login(@Body requestBody: JsonObject): Call<RestView<JsonObject>>

    @GET(APPOINTMENTS)
    fun getAppts(@Header("access_token") accessToken: String, @Path("user_id") userId: Int): Call<ApptModel.Response>

    @POST(SIGNUP_USER)
    fun signup(@Body requestBody: JsonObject): Call<RestView<JsonObject>>
}