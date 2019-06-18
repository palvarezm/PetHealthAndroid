package pe.edu.upc.pethealth.fragments;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
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

    private ImageView photoANImageView;
    private TextView nameTextView;
    private TextView dniTextView;
    private TextView phoneTextView;
    private TextView addressTextView;
    //private Button editButton;
    private Person person;
    private AVLoadingIndicatorView loadingIndicatorView;

    private RecyclerView myPetsRecyclerView;
    private MyPetAdapters myPetAdapters;
    private RecyclerView.LayoutManager myPetLayoutManager;
    private TextView btAddPet;
    private SharedPreferencesManager sharedPreferencesManager;
    List<MyPet> myPets;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Edit Profile");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0){
            UserInformationFragment userInformationFragment = new UserInformationFragment();
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content,userInformationFragment).commit();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setFragmentToolbar("Profile",true,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.getContext());
        person = sharedPreferencesManager.getPerson();
        nameTextView = view.findViewById(R.id.nameTextView);
        loadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        photoANImageView = (ImageView) view.findViewById(R.id.profileImageView);
        dniTextView = (TextView) view.findViewById(R.id.documentNumberTextView);
        phoneTextView =(TextView) view.findViewById(R.id.phoneTextView);
        addressTextView = (TextView) view.findViewById(R.id.addressTextView);
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
        btAddPet = view.findViewById(R.id.newPetTextView);
        btAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddPetActivity.class);
                context.startActivity(intent);
            }
        });
        updateProfile();
        setHasOptionsMenu(true);
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
        Picasso.get().load(sharedPreferencesManager.getUser().getPhoto()).error(R.mipmap.ic_launcher)
                .transform(new RoundedCornersTransformation(10,0))
                .resize(150,160)
                .centerCrop()
                .into(photoANImageView);
        nameTextView.setText(person.getName() + " " + person.getLastName());
        dniTextView.setText(person.getDni());
        phoneTextView.setText(person.getPhone());
        addressTextView.setText(person.getAddress());
    }

}
