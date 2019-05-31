package pe.edu.upc.pethealth.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import pe.edu.upc.pethealth.models.Person;
import pe.edu.upc.pethealth.models.User;

public class SharedPreferencesManager {
    private static final String PREFERENCES_NAME = "blcc";
    private static final String CURRENT_USER = "current_user";
    private static final String CURRENT_PERSON = "current_person";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String COMPLETED_REGISTER = "current_register";

    private static SharedPreferencesManager self;
    private final SharedPreferences mPreferences;
    private Gson gson;

    public SharedPreferencesManager(Context context) {
        this.mPreferences =context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static SharedPreferencesManager getInstance(Context context){
        if (self == null){
            self = new SharedPreferencesManager(context);
        }
        return self;
    }

    public void saveUser(String user, String person, String access_token){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(CURRENT_USER, user);
        editor.putString(CURRENT_PERSON, person);
        editor.putString(ACCESS_TOKEN, access_token);
        editor.apply();
    }

    public void closeSession(){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(CURRENT_USER, null);
        editor.putString(CURRENT_PERSON, null);
        editor.putString(COMPLETED_REGISTER, null);
        editor.apply();
    }

    public User getUser(){
        String userString = mPreferences.getString(CURRENT_USER, "");
        Gson gson = new Gson();
        if(!userString.isEmpty()){
            Log.d("userstring", userString);
            User user = gson.fromJson(userString, User.class);
            return user;
        }
        else{
            return null;
        }
    }

    public Person getPerson(){
        String personString = mPreferences.getString(CURRENT_PERSON, "");
        Gson gson = new Gson();
        if (!personString.isEmpty()){
            Person person = gson.fromJson(personString, Person.class);
            return person;
        }
        else{
            return null;
        }
    }

    public boolean isUserLogged(){
        return getUser() != null;
    }

    public String getAccessToken(){
        String accessToken = mPreferences.getString(ACCESS_TOKEN, "");
        return accessToken;
    }
}
