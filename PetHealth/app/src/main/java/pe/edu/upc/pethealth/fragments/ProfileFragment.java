package pe.edu.upc.pethealth.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.AddPetActivity;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.adapters.MyPetAdapters;
import pe.edu.upc.pethealth.models.MyPet;
import pe.edu.upc.pethealth.models.Person;
import pe.edu.upc.pethealth.network.PetHealthApiService;
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView tittleTextView;
    private ImageView photoANImageView;
    private TextView nameTextView;
    private TextView lastNameTextView;
    private TextView dniTextView;
    private TextView phoneTextView;
    private TextView addressTextView;
    private Button editButton;
    private Person person;
    private AVLoadingIndicatorView loadingIndicatorView;

    private RecyclerView myPetsRecyclerView;
    private MyPetAdapters myPetAdapters;
    private RecyclerView.LayoutManager myPetLayoutManager;
    private Button btAddPet;
    private SharedPreferencesManager sharedPreferencesManager;
    List<MyPet> myPets;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setFragmentToolbar("Profile",true,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.getContext());
        person = sharedPreferencesManager.getPerson();
        tittleTextView = (TextView) view.findViewById(R.id.tittleTextView);
        loadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        tittleTextView = (TextView) view.findViewById(R.id.tittleTextView);
        photoANImageView = (ImageView) view.findViewById(R.id.profileImageView);
        photoANImageView.setVisibility(View.INVISIBLE);
        dniTextView = (TextView) view.findViewById(R.id.dniDataTextView);
        phoneTextView =(TextView) view.findViewById(R.id.phoneDataTextView);
        addressTextView = (TextView) view.findViewById(R.id.addressDataTextView);
        editButton = (Button) view.findViewById(R.id.editValuesButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInformationFragment userInformationFragment = new UserInformationFragment();
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content,userInformationFragment).commit();
            }
        });

        //Pet List

        myPetsRecyclerView = (RecyclerView) view.findViewById(R.id.myPetsRecyclerView);myPets = new ArrayList<>();
        myPetAdapters = new MyPetAdapters(myPets);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            myPetLayoutManager = new GridLayoutManager(view.getContext(), 1);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            myPetLayoutManager = new GridLayoutManager(view.getContext(), 2);
        }
        myPetsRecyclerView.setAdapter(myPetAdapters);
        myPetsRecyclerView.setLayoutManager(myPetLayoutManager);
        btAddPet = (Button) view.findViewById(R.id.btAddPet);
        btAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Pet?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Context context = v.getContext();
                                Intent intent = new Intent(context, AddPetActivity.class);
                                //intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        }).show();
            }
        });
        updateProfile();
        updatePets();
        return view;
    }

    private void updatePets() {
        Log.d("TOKEN", sharedPreferencesManager.getAccessToken());
        AndroidNetworking.get(PetHealthApiService.PET_URL)
                .addPathParameter("userId", Integer.toString(sharedPreferencesManager.getUser().getId()))
                .addHeaders("access_token", sharedPreferencesManager.getAccessToken())
                .setPriority(Priority.LOW)
                .setTag(R.string.app_name)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            myPets = MyPet.from(response.getJSONArray("data"));
                            myPetAdapters.setMyPets(myPets);
                            myPetAdapters.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(getString(R.string.app_name), anError.getErrorBody());
                    }
                });
    }

    private void updateProfile(){
        dniTextView.setText(person.getDni());
        phoneTextView.setText(person.getPhone());
        addressTextView.setText(person.getAddress());
    }

    private void loadImage(String imageUrl){
        AndroidNetworking.get(imageUrl)
                .setTag("imageRequest")
                .setPriority(Priority.MEDIUM)
                .setBitmapMaxHeight(120)
                .setBitmapMaxWidth(120)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        photoANImageView.setImageBitmap(response);
                        photoANImageView.setVisibility(View.VISIBLE);
                        loadingIndicatorView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.toString());
                    }
                });
    }
}
