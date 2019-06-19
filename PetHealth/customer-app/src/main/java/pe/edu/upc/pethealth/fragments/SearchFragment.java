package pe.edu.upc.pethealth.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

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

    private Boolean mLocationPermissionsGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setFragmentToolbar("Search",true,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        getLocationPermission();

        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.getContext());
        veterinaries = new ArrayList<>();
        return view;
    }

    public void initMap(){
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.veterinariesMap);
        mapFragment.getMapAsync(this);
        getVeterinaries();
    }

    private void getVeterinaries(){
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

    public void getLocationPermission(){
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        Boolean permissionFineLoc = ContextCompat.checkSelfPermission(this.getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        Boolean permissionCourseLoc = ContextCompat.checkSelfPermission(this.getContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permissionCourseLoc && permissionFineLoc){
            mLocationPermissionsGranted = true;
        }
        else{
            ActivityCompat.requestPermissions(this.getActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng currentLatLng = new LatLng(currentLocationLat, currentLocationLong);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                Log.d("TEST", "in");
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d("Permissions Result", "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d("Permissions Result", "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void setVeterinariesMarkers(List<Veterinary> veterinaries) {
        for (Veterinary veterinary :
                veterinaries) {
            LatLng veterinaryLocation = new LatLng(veterinary.getLatitude(), veterinary.getLongitude());
            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.veterinary_marker, null));
            MarkerOptions markerOptions = new MarkerOptions().
                    position(veterinaryLocation).
                    title(veterinary.getName()).
                    snippet(String.format("%.2f", veterinary.getDistance()) + " Km.").
                    icon(markerIcon);
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(veterinary);
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
