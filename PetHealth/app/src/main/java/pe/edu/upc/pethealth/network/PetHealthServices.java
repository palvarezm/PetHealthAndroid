package pe.edu.upc.pethealth.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PetHealthServices {
    @POST(EndpointUrls.LOGIN)
    Call<RestView<JsonObject>> login(@Body JsonObject requestBody);
}
