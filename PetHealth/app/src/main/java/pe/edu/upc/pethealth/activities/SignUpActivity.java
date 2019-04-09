package pe.edu.upc.pethealth.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.models.DocumentType;
import pe.edu.upc.pethealth.network.PetHealthApiService;

public class SignUpActivity extends AppCompatActivity {

    Button doneButton;
    String tag;
    EditText usernameEditText;
    TextInputEditText passwordEditText;
    EditText emailEditText;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StartActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        setSupportActionBar(myToolbar);
        usernameEditText =(EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (TextInputEditText) findViewById(R.id.passwordTextInputEditText);

        emailEditText =(EditText) findViewById(R.id.emailEditText);

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptToSignUp();
            }
        });
        tag = "PetHealth";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attemptToSignUp() {
        //Reset Errors

        passwordEditText.setError(null);
        usernameEditText.setError(null);
        emailEditText.setError(null);


        //Store values

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        View focusView = null;
        Boolean cancel = false;

        if(TextUtils.isEmpty(username)) {
            usernameEditText.setError("This field is empty");
            focusView = usernameEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(password)) {
            passwordEditText.setError("This field is empty");
            focusView = passwordEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(email)) {
            emailEditText.setError("This field is empty");
            focusView = emailEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            JSONObject user = new JSONObject();
            try {
                user.put("username",username);
                user.put("password",password);
                user.put("mail",email);

            }catch (JSONException e){
                e.printStackTrace();
            }

            AndroidNetworking.post(PetHealthApiService.SIGNUP_USER)
                    .addJSONObjectBody(user)
                    .setTag(getString(R.string.app_name))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            try {
                                if ("done".equalsIgnoreCase(response.getString("status"))) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("User Corretly Created");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            Intent intent = new Intent(context, StartActivity.class);
                                            context.startActivity(intent);
                                            finish();
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                } else {
                                    Log.d(getString(R.string.app_name), "Error with the Resgistration of User");
                                }

                            } catch (JSONException e) {
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
