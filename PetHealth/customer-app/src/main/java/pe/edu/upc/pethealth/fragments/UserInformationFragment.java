package pe.edu.upc.pethealth.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.models.Person;
import pe.edu.upc.pethealth.network.PetHealthApiService;
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInformationFragment extends Fragment {

    private EditText nameEditText;
    private EditText lastNameEditText;
    private EditText documentNumberEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private Button confirmButton;

    private JSONObject jsonPerson;
    private Person person;
    private SharedPreferencesManager sharedPreferencesManager;


    public UserInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_information, container, false);
        ((MainActivity)getActivity()).setFragmentToolbar("Edit Profile",true,getFragmentManager());
        //Initiate models
        sharedPreferencesManager = SharedPreferencesManager.Companion.getInstance(this.getContext());
        person = sharedPreferencesManager.getPerson();
        jsonPerson = new JSONObject();

        //Assign values
        nameEditText = (EditText) view.findViewById(R.id.nameTextInputEditText);
        lastNameEditText = (EditText) view.findViewById(R.id.lastNameTextInputEditText);
        documentNumberEditText = (EditText) view.findViewById(R.id.docNumberTextInputEditText);
        addressEditText = (EditText) view.findViewById(R.id.addressTextInputEditText);
        phoneEditText = (EditText) view.findViewById(R.id.phoneTextInputEditText);
        confirmButton = (Button) view.findViewById(R.id.confirmButton);

        updateProfile();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });
        return view;
    }

    private void editProfile() {
        final String name = nameEditText.getText().toString();
        final String lastName = lastNameEditText.getText().toString();
        final String phone = phoneEditText.getText().toString();
        final String documentNumber = documentNumberEditText.getText().toString();
        final String address  = addressEditText.getText().toString();
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            jsonPerson.put("userId",person.getId());
            jsonPerson.put("name", name);
            jsonPerson.put("lastName",lastName);
            jsonPerson.put("nrodocumento",documentNumber);
            jsonPerson.put("adress",address);
            jsonPerson.put("phone",phone);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void updateProfile() {
        nameEditText.setText(person.getName());
        lastNameEditText.setText(person.getLastName());
        documentNumberEditText.setText(person.getDni());
        phoneEditText.setText(person.getPhone());
        addressEditText.setText(person.getAddress());
    }
}
