package pe.edu.upc.pethealth.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PetHealthServices {
    @POST(EndpointUrls.LOGIN)
    Call<RestView<JsonObject>> login(@Body JsonObject requestBody);

    @POST(EndpointUrls.APPOINTMENTS)
    Call<RestView<JsonArray>> getAppts(@Header("access_token") String accessToken, @Path("user_id") int userId);

    @GET(EndpointUrls.VETERINARIES)
    Call<RestView<JsonArray>> getCloseVeterinaries(@Header("access_token") String accessToken, @Query("latitude") Double latitude, @Query("longitude") Double longitude);
}
