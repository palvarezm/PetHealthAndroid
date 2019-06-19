package pe.edu.upc.pethealth.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.lib.Veterinary;
import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.network.LoggerCallback;
import pe.edu.upc.pethealth.network.RestClient;
import pe.edu.upc.pethealth.network.RestView;
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private List<pe.edu.upc.lib.Veterinary> veterinaries;
    private SharedPreferencesManager sharedPreferencesManager;
    private Double currentLocationLat = -12.0874509;
    private Double currentLocationLong = -77.0499422;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setFragmentToolbar("Search",true,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.veterinariesMap);

        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.getContext());
        veterinaries = new ArrayList<>();
        updateSearch();
        return view;
    }

    private void updateSearch(){

        mapFragment.getMapAsync(this);
        Call<RestView<JsonArray>> call = new RestClient().getWebServices().getCloseVeterinaries(sharedPreferencesManager.getAccessToken(), currentLocationLat, currentLocationLong);
        call.enqueue(new LoggerCallback<RestView<JsonArray>>(){
            @Override
            public void onResponse(Call<RestView<JsonArray>> call, Response<RestView<JsonArray>> response) {
                super.onResponse(call, response);
                parseResponse(response.body().getData());
                setVeterinariesMarkers(veterinaries);
            }

            @Override
            public void onFailure(Call<RestView<JsonArray>> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("FAILURE", t.toString());
            }
        });
    }

    private void parseResponse(JsonArray data) {
        for (JsonElement dataElement: data) {
            Gson gson = new Gson();
            pe.edu.upc.lib.Veterinary veterinary = gson.fromJson(dataElement.getAsJsonObject().get("veterinary").toString(), pe.edu.upc.lib.Veterinary.class);
            veterinary.setDistance(gson.fromJson(dataElement.getAsJsonObject().get("distance").toString(), Double.class));
            Log.d("Veterinary", veterinary.toString());
            veterinaries.add(veterinary);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentLatLng = new LatLng(currentLocationLat, currentLocationLong);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f));
    }

    private void setVeterinariesMarkers(List<Veterinary> veterinaries) {
        Log.d("TESTING LIST!", veterinaries.toString());
        for (Veterinary veterinary :
                veterinaries) {
            Log.d("TESTING LIST", veterinary.toString());
            LatLng veterinaryLocation = new LatLng(veterinary.getLatitude(), veterinary.getLongitude());
            mMap.addMarker(new MarkerOptions().position(veterinaryLocation).title(veterinary.getName()));
        }
    }
}
