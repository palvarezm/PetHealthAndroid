package pe.edu.upc.pethealth.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.models.User;
import pe.edu.upc.pethealth.network.Connection;
import pe.edu.upc.pethealth.network.PetHealthApiService;

public class StartActivity extends AppCompatActivity {

    private ImageView logoImageView;
    private EditText userEditText;
    private EditText passwordTextInputEditText;
    private Button signInButton;
    private User user;
    private TextView signUptextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        userEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordTextInputEditText = (TextInputEditText) findViewById(R.id.passwordTextInputEditText);
        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        signUptextView = (TextView) findViewById(R.id.signUpTextView);
        signUptextView.setPaintFlags(signUptextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signUptextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, SignUpActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void attemptLogin() {

        // Reset errors.
        userEditText.setError(null);
        passwordTextInputEditText.setError(null);
        final Context context = this;

        // Store values at the time of the login attempt.
        String email = userEditText.getText().toString();
        String password = passwordTextInputEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            userEditText.setError(getString(R.string.error_field_required));
            focusView = userEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            userEditText.setError(getString(R.string.error_field_required));
            focusView = userEditText;
            cancel = true;
        }

        if(!Connection.isOnline(getApplicationContext())){
            cancel = true;
            return;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            AndroidNetworking.post(PetHealthApiService.LOGIN_URL)
                    .addBodyParameter("username", email)
                    .addBodyParameter("password", password)
                    .setTag(getString(R.string.app_name))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            try {
                                if ("success".equalsIgnoreCase(response.getString("status"))) {
                                    user = user.from(response.getJSONObject("userLog"));
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtras(user.toBundle());
                                    context.startActivity(intent);
                                    finish();
                                } else {
                                    Log.d(getString(R.string.app_name), "User and password are incorrect");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("User and password are incorrect");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
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
