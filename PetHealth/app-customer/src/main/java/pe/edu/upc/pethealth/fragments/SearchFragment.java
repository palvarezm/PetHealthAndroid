package pe.edu.upc.pethealth.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
        Call<RestView<JsonArray>> call = new RestClient().getWebServices().getCloseVeterinaries(sharedPreferencesManager.getAccessToken(), -12.0874509, -77.0499422);
        call.enqueue(new LoggerCallback<RestView<JsonArray>>(){
            @Override
            public void onResponse(Call<RestView<JsonArray>> call, Response<RestView<JsonArray>> response) {
                super.onResponse(call, response);
                parseResponse(response.body().getData());
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

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
