package pe.edu.upc.pethealth;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by genob on 20/10/2017.
 */

public class PetHealthApp extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
