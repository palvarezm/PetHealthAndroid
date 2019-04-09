package pe.edu.upc.pethealth.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.models.Vet;
import pe.edu.upc.pethealth.models.Veterinary;
import pe.edu.upc.pethealth.network.PetHealthApiService;

public class AddAppointmentActivity extends AppCompatActivity {
    Spinner vetSpinner;
    Spinner veterinarySpinner;
    EditText appointmentDateEditText;
    EditText descriptionEditText;
    EditText prescriptionEditText;
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    int yy = calendar.get(Calendar.YEAR);
    int mm = calendar.get(Calendar.MONTH);
    int dd = calendar.get(Calendar.DAY_OF_MONTH);
    List<Vet> vets = new ArrayList<>();
    List<Veterinary> veterinaries;
    ArrayAdapter vetSpinnerArrayAdapter;
    ArrayAdapter veterinarySpinnerArrayAdapter;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadVetSpinner(this);
        loadVeterinarySpinner(this);
        setContentView(R.layout.activity_add_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        doneButton = (Button) findViewById(R.id.doneButton);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                finish();
            }
        });
        vetSpinner = findViewById(R.id.vetSpinner);
        veterinarySpinner = findViewById(R.id.veterinarySpinner);
        appointmentDateEditText = findViewById(R.id.appointmentDateEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        prescriptionEditText = findViewById(R.id.prescriptionEditText);
        appointmentDateEditText.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                //Esconde el teclado en caso se haya clickeado algun otro view antes de este
                //para luego recien clickear el datepicker
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(appointmentDateEditText.getWindowToken(), 0);

                //Asignando el valor escogido en calendar al EditText para que se muestre
                datePickerDialog = new DatePickerDialog(AddAppointmentActivity.this,
                        new DatePickerDialog.OnDateSetListener(){

                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedYY, int selectedMM, int selectedDD) {
                                yy = selectedYY;
                                mm = selectedMM;
                                dd = selectedDD;
                                appointmentDateEditText.setText(dd + "/" + (mm+1) + "/" + yy);
                            }
                        },yy,mm,dd);
                calendar.set(yy,mm,dd);
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                Date maxDate = new Date();
                Date minDate = new Date();
                try {
                    maxDate = f.parse("01/01/2020");
                    minDate = f.parse("01/01/1950");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
                datePickerDialog.show();
            }
        });
        final Context context = this;
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    Vet vet = (Vet)vetSpinner.getSelectedItem();
                    Veterinary veterinary = (Veterinary) veterinarySpinner.getSelectedItem();
                    final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String actualdate = String.format("%tD",format.parse(appointmentDateEditText.getText().toString()));;
                    jsonObject.put("petId", getIntent().getExtras().getInt("petId"));
                    jsonObject.put("vetId", vet.getVetId());
                    jsonObject.put("veterinaryId", veterinary.getId());
                    jsonObject.put("status","ACT");
                    jsonObject.put("date", actualdate);
                    jsonObject.put("description",descriptionEditText.getText().toString());
                    jsonObject.put("prescription",prescriptionEditText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (ParseException e){

                }
                System.out.println(jsonObject);
                AndroidNetworking.post(PetHealthApiService.ADD_APPOINTMENT_URL)
                        .addJSONObjectBody(jsonObject)
                        .setTag(R.string.app_name)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast toast;
                                try {
                                    if ("ok".equalsIgnoreCase(response.getString("status"))){
                                         toast = Toast.makeText(context,"Done!",Toast.LENGTH_SHORT);
                                         finish();
                                    } else {
                                         toast = Toast.makeText(context,"Error",Toast.LENGTH_SHORT);
                                    }
                                    toast.show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                anError.printStackTrace();
                            }
                        });
            }
        });
    }

    private void loadVetSpinner(final Context context){
        AndroidNetworking.get(PetHealthApiService.VET_URL)
                .setPriority(Priority.LOW)
                .setTag(R.string.app_name)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vets = Vet.from(response.getJSONArray("vets"));
                            vetSpinnerArrayAdapter = new ArrayAdapter(context,R.layout.spinner_text,vets);
                            vetSpinner.setAdapter(vetSpinnerArrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        vets = new ArrayList<>();
                    }
                });
    }

    private void loadVeterinarySpinner(final Context context){
        AndroidNetworking.get(PetHealthApiService.VETERINARY_URL)
                .setPriority(Priority.LOW)
                .setTag(R.string.app_name)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            veterinaries = Veterinary.from(response.getJSONArray("veterinaries"));
                            veterinarySpinnerArrayAdapter = new ArrayAdapter(context, R.layout.spinner_text, veterinaries);
                            veterinarySpinner.setAdapter(veterinarySpinnerArrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        veterinaries = new ArrayList<>();
                    }
                });
    }
}