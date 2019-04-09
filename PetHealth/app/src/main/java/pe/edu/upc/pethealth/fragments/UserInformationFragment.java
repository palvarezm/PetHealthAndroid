package pe.edu.upc.pethealth.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.AddPetActivity;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.activities.SignUpActivity;
import pe.edu.upc.pethealth.activities.StartActivity;
import pe.edu.upc.pethealth.models.DocumentType;
import pe.edu.upc.pethealth.models.Person;
import pe.edu.upc.pethealth.models.User;
import pe.edu.upc.pethealth.network.PetHealthApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInformationFragment extends Fragment {


    TextView editTextView;
    TextView nameTextView;
    TextView lastNameTextView;
    TextView birthDateTextView;
    TextView documentTypeTextView;
    TextView documentNumberTextView;
    TextView addressTextView;
    TextView phoneTextView;

    EditText nameEditText;
    EditText lastNameEditText;
    EditText birthDateEditText;
    EditText documentNumberEditText;
    EditText addressEditText;
    EditText phoneEditText;

    Spinner documentTypeSpinner;
    Button confirmButton;

    Bundle bundle;
    JSONObject jsonPerson;
    Person person;
    List<DocumentType> documentTypeList;

    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
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
         bundle = getArguments();
         jsonPerson = new JSONObject();
        documentTypeList = new ArrayList<DocumentType>();
        //Assign values
        nameTextView = (TextView) view.findViewById(R.id.profileNameTextView);
        lastNameTextView = (TextView) view.findViewById(R.id.profileLastNameTextView);
        birthDateTextView = (TextView) view.findViewById(R.id.profileBirthDateTextView);
        documentTypeTextView = (TextView) view.findViewById(R.id.documentTypeTextView);
        documentNumberTextView = (TextView) view.findViewById(R.id.documentNumberTextView);
        addressTextView = (TextView) view.findViewById(R.id.profileAddressTextView);
        phoneTextView = (TextView) view.findViewById(R.id.profilephoneTextView);

        nameEditText = (EditText) view.findViewById(R.id.profileNameEditText);
        lastNameEditText = (EditText) view.findViewById(R.id.profileLastNameEditText);
        birthDateEditText = (EditText) view.findViewById(R.id.profileBirthdateEditText);
        documentNumberEditText = (EditText) view.findViewById(R.id.documentNumberEditText);
        addressEditText = (EditText) view.findViewById(R.id.profileAddressEditText);
        phoneEditText = (EditText) view.findViewById(R.id.profilePhoneEditText);

        documentTypeSpinner = (Spinner) view.findViewById(R.id.documentTypeSpinner);
        confirmButton = (Button) view.findViewById(R.id.confirmButton);

        updateDocumentTypeList(view);
        updateProfile();

        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(birthDateEditText.getWindowToken(), 0);

                //Asignando el valor escogido en calendar al EditText para que se muestre
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener(){

                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedYY, int selectedMM, int selectedDD) {
                                yy = selectedYY;
                                mm = selectedMM;
                                dd = selectedDD;
                                birthDateEditText.setText(dd + "/" + (mm+1) + "/" + yy);
                            }
                        },yy,mm,dd);
                calendar.set(yy,mm,dd);
                datePickerDialog.show();
            }
        });
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
        final String birthdate = birthDateEditText.getText().toString();
        final Integer documentTypeId = documentTypeSpinner.getSelectedItemPosition()+1;
        final String phone = phoneEditText.getText().toString();
        final String documentNumber = documentNumberEditText.getText().toString();
        final String address  = addressEditText.getText().toString();
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            jsonPerson.put("userId",bundle.getInt("user_id"));
            jsonPerson.put("name", name);
            jsonPerson.put("lastName",lastName);
            jsonPerson.put("nrodocumento",documentNumber);
            jsonPerson.put("tipodocumento", documentTypeId);
            jsonPerson.put("adress",address);
            jsonPerson.put("birthdate", format.parse(birthdate));
            jsonPerson.put("phone",phone);
        }catch (JSONException e){
            e.printStackTrace();
        } catch (ParseException e) {
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
        AndroidNetworking.get(PetHealthApiService.CUSTOMER_URL)
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
                                nameEditText.setText(person.getName());
                                lastNameEditText.setText(person.getLastName());
                                documentTypeSpinner.setSelection(person.getTipoDocumentoId()-1);
                                documentNumberEditText.setText(person.getDni());
                                phoneEditText.setText(person.getPhone());
                                addressEditText.setText(person.getAddress());
                                birthDateEditText.setText(person.getBirthdate());
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

                            documentTypeSpinner.setAdapter(new ArrayAdapter<String>(view.getContext(),
                                    android.R.layout.simple_spinner_item, shortenings));

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
