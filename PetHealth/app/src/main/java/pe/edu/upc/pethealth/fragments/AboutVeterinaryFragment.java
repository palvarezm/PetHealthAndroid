package pe.edu.upc.pethealth.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.models.Veterinary;

public class AboutVeterinaryFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    TextView nameTextView;
    RatingBar rateRatingBar;
    Double lat,lng;
    Veterinary veterinary;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_veterinary, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        setRetainInstance(true);
        mapFragment.getMapAsync(this);
        ((MainActivity)getActivity()).setFragmentToolbar("Veterinaries",true,getFragmentManager());
        veterinary = Veterinary.from(getArguments());
        nameTextView = (TextView)view.findViewById(R.id.nameTextView);
        rateRatingBar = (RatingBar)view.findViewById(R.id.veterinaryRatingBar);
        rateRatingBar.setEnabled(false);
        nameTextView.setText(veterinary.getName());
        rateRatingBar.setRating(veterinary.getRating());
        lat = veterinary.getLatitude();
        lng = veterinary.getLongitude();
        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //LatLng sydney = new LatLng(-34, 151);
        LatLng vetpos = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(vetpos).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vetpos));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                //.bearing(90)                // Sets the orientation of the camera to east
                //.tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
