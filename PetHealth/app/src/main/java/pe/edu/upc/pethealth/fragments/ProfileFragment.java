package pe.edu.upc.pethealth.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.models.Person;
import pe.edu.upc.pethealth.network.PetHealthApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView tittleTextView;
    ImageView photoANImageView;
    TextView nameTextView;
    TextView lastNameTextView;
    TextView dniTextView;
    TextView mailTextView;
    TextView phoneTextView;
    TextView addressTextView;
    Button editButton;
    Person person;
    AVLoadingIndicatorView loadingIndicatorView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setFragmentToolbar("Profile",true,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        person = new Person();
        final Bundle b = getArguments();
        tittleTextView = (TextView) view.findViewById(R.id.tittleTextView);
        loadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        tittleTextView = (TextView) view.findViewById(R.id.tittleTextView);
        photoANImageView = (ImageView) view.findViewById(R.id.profileImageView);
        photoANImageView.setVisibility(View.INVISIBLE);
        dniTextView = (TextView) view.findViewById(R.id.dniDataTextView);
        mailTextView = (TextView) view.findViewById(R.id.mailDataTextView);
        phoneTextView =(TextView) view.findViewById(R.id.phoneDataTextView);
        addressTextView = (TextView) view.findViewById(R.id.addressDataTextView);
        editButton = (Button) view.findViewById(R.id.editValuesButton);
        updateProfile(b);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInformationFragment userInformationFragment = new UserInformationFragment();
                userInformationFragment.setArguments(b);
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content,userInformationFragment).commit();
            }
        });
        return view;
    }

    private void updateProfile(final Bundle bundle){
        AndroidNetworking.get(PetHealthApiService.CUSTOMER_URL+ "/" +String.valueOf(bundle.getInt("user_id")))
                .setTag(getString(R.string.app_name))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getJSONArray("res") != null) {
                            JSONObject res = response.getJSONArray("res").getJSONObject(0);
                                person = new Person(res.getInt("id"),
                                        res.getString("name"),
                                        res.getString("lastname"),
                                        res.getString("nrodocumento"),
                                        res.getString("address"),
                                        res.getString("phone"),
                                        res.getString("birthdate"),
                                        res.getInt("tipodocumentoId")
                                );
                            photoANImageView.setImageResource(R.mipmap.ic_launcher);
                            //photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher_round);
                            //photoANImageView.setErrorImageResId(R.mipmap.ic_launcher_round);
                            //photoANImageView.setImageUrl("http://jbblog.flopro.taco-hvac.com/wp-content/uploads/2014/05/smart-person.jpg");//TODO change for profile image url
                            tittleTextView.setText(person.getName()+" "+person.getLastName());
                            dniTextView.setText(person.getDni());
                            mailTextView.setText(bundle.getString("mail"));
                            phoneTextView.setText(person.getPhone());
                            addressTextView.setText(person.getAddress());
                            loadImage("http://jbblog.flopro.taco-hvac.com/wp-content/uploads/2014/05/smart-person.jpg");
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
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
