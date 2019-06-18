package pe.edu.upc.pethealth.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.models.DocumentType;
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

    private DatePickerDialog datePickerDialog;
    private JSONObject jsonPerson;
    private Person person;
    private List<DocumentType> documentTypeList;
    private Calendar calendar = Calendar.getInstance();
    private SharedPreferencesManager sharedPreferencesManager;

    int yy= calendar.get(Calendar.YEAR);
    int mm = calendar.get(Calendar.MONTH);
    int dd = calendar.get(Calendar.DAY_OF_MONTH);

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
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.getContext());
        person = sharedPreferencesManager.getPerson();
        jsonPerson = new JSONObject();
        documentTypeList = new ArrayList<DocumentType>();

        //Assign values
        nameEditText = (EditText) view.findViewById(R.id.nameTextInputEditText);
        lastNameEditText = (EditText) view.findViewById(R.id.lastNameTextInputEditText);
        documentNumberEditText = (EditText) view.findViewById(R.id.docNumberTextInputEditText);
        addressEditText = (EditText) view.findViewById(R.id.addressTextInputEditText);
        phoneEditText = (EditText) view.findViewById(R.id.phoneTextInputEditText);
        confirmButton = (Button) view.findViewById(R.id.confirmButton);

        updateDocumentTypeList(view);
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

        AndroidNetworking.post(PetHealthApiService.SIGNUP_CUSTOMER)
                .addJSONObjectBody(jsonPerson)
                .setTag(getString(R.string.app_name))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if ("done".equalsIgnoreCase(response.getString("status"))){
                                getFragmentManager().popBackStack();
                            } else {
                                Log.d(getString(R.string.app_name), "Error with the Resgistration of Person");
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Error with the Edit of Person");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(getString(R.string.app_name),anError.getLocalizedMessage());
                    }
                });
    }

    private void updateProfile() {
        nameEditText.setText(person.getName());
        lastNameEditText.setText(person.getLastName());
        documentNumberEditText.setText(person.getDni());
        phoneEditText.setText(person.getPhone());
        addressEditText.setText(person.getAddress());

        /*AndroidNetworking.get(PetHealthApiService.CUSTOMER_URL)
                .addQueryParameter("customerId",String.valueOf(bundle.getInt("user_id")))
                .setTag(getString(R.string.app_name))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject res = response.getJSONArray("res").getJSONObject(0);
                            if (res.getString("name") != null) {
                                person = new Person(res.getInt("id"),
                                        res.getString("name"),
                                        res.getString("lastname"),
                                        res.getString("nrodocumento"),
                                        res.getString("address"),
                                        res.getString("phone"),
                                        res.getString("birthdate"),
                                        res.getInt("tipodocumentoId")
                                );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });*/
    }

    private void updateDocumentTypeList(final View view) {
        AndroidNetworking.get(PetHealthApiService.DOCTYPE_URL)
                .setTag(getString(R.string.app_name))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if("error".equalsIgnoreCase(response.getString("status"))){
                                Log.d(getString(R.string.app_name),response.getString("message"));
                                return;
                            }
                            documentTypeList = DocumentType.from(response.getJSONArray("documents"));

                            ArrayList<String> shortenings = new ArrayList<String>();
                            for (int i =0; i<documentTypeList.size();i++){
                                shortenings.add(documentTypeList.get(i).getShortening());
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(getString(R.string.app_name), anError.getErrorBody());
                    }
                });
    }

}
