package pe.edu.upc.pethealth.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.models.User;
import pe.edu.upc.pethealth.network.PetHealthApiService;
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager;

public class AddPetActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText raceEditText;
    private EditText birthDateEditText;
    private EditText descriptionEditText;
    private TextView cameraTextView;
    private Button addButton;
    private Button cameraButton;
    DatePickerDialog datePickerDialog;

    private User user;
    private SharedPreferencesManager sharedPreferencesManager;
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    Calendar calendar = Calendar.getInstance();
    int yy= calendar.get(Calendar.YEAR);
    int mm = calendar.get(Calendar.MONTH);
    int dd = calendar.get(Calendar.DAY_OF_MONTH);
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        sharedPreferencesManager = SharedPreferencesManager.Companion.getInstance(this.getApplicationContext());
        user = sharedPreferencesManager.getUser();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_petToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraTextView = (TextView) findViewById(R.id.cameraTextView);
        nameEditText = (EditText) findViewById(R.id.titleTextView);
        raceEditText = (EditText) findViewById(R.id.petRaceEditText);
        descriptionEditText = (EditText) findViewById(R.id.petDescriptionEditText);
        addButton = (Button) findViewById(R.id.addPetButton); 
        birthDateEditText = (EditText) findViewById(R.id.birthDateEditText);
        birthDateEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Esconde el teclado en caso se haya clickeado algun otro view antes de este
                //para luego recien clickear el datepicker
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(birthDateEditText.getWindowToken(), 0);

                //Asignando el valor escogido en calendar al EditText para que se muestre
                datePickerDialog = new DatePickerDialog(AddPetActivity.this,
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempToAddPet();
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d(getString(R.string.app_name),"Pet Pic Saved");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attempToAddPet() {

        nameEditText.setError(null);
        raceEditText.setError(null);
        descriptionEditText.setError(null);
        birthDateEditText.setError(null);
        final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        View focusView = null;
        Boolean cancel = false;
        final Context context = this;
        final String name = nameEditText.getText().toString();
        final String race = raceEditText.getText().toString();
        final String birthDate = birthDateEditText.getText().toString();
        final String description = descriptionEditText.getText().toString();

        if(TextUtils.isEmpty(name)) {
            nameEditText.setError("This field is required");
            focusView = nameEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(race)) {
            raceEditText.setError("This field is required");
            focusView = raceEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(birthDate)) {
            birthDateEditText.setError("This field is required");
            focusView = birthDateEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(description)) {
            descriptionEditText.setError("This field is required");
            focusView = descriptionEditText;
            cancel = true;
        }
        if(cancel) {
            focusView.requestFocus();
        } else {
            JSONObject pet = new JSONObject();
            try {
                pet.put("name", name);
                pet.put("race",race);
                pet.put("description",description);
                pet.put("birth_date",format.format(format.parse(birthDate)));
                pet.put("status",1);
                pet.put("image_url", "https://static1.squarespace.com/static/58e086d737c5814ef81f146d/t/5939a8f26a4963343981c6d6/1497656344237/piglet");
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(pet);
            AndroidNetworking.post(PetHealthApiService.ADD_PET_URL)
                    .addPathParameter("userId", Integer.toString(sharedPreferencesManager.getUser().getId()))
                    .addHeaders("access_token", sharedPreferencesManager.getAccessToken())
                    .addJSONObjectBody(pet)
                    .setTag(getString(R.string.app_name))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if ("Ok".equalsIgnoreCase(response.getString("status"))) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("Your Pet was successfully added");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            Intent intent = new Intent(context, MainActivity.class);
                                            context.startActivity(intent);
                                            finish();
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                } else {
                                    Toast toast = Toast.makeText(context,"Error",Toast.LENGTH_SHORT);
                                    toast.show();
                                    Log.d(getString(R.string.app_name), "Error with the Resgistration of Pet");
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d(getString(R.string.app_name), anError.getLocalizedMessage());
                        }
                    });
        }
    }
}
