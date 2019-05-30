package pe.edu.upc.pethealth.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PetHealthServices {
    @POST(EndpointUrls.LOGIN)
    Call<RestView<JsonObject>> login(@Body JsonObject requestBody);

    @HTTP(method = "GET", path = EndpointUrls.APPOINTMENTS, hasBody = true)
    Call<RestView<JsonObject>> getAppts(@Body JsonObject body, @Path("user_id") int userId);
}
