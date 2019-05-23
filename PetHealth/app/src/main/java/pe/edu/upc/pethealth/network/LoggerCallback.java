package pe.edu.upc.pethealth.network;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pe.edu.upc.pethealth.utils.Constants.RESPONSE_TAG;

public class LoggerCallback<T> implements Callback<T> {

    public LoggerCallback(){

    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.d(RESPONSE_TAG, String.valueOf(response));
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d(RESPONSE_TAG, String.valueOf(t));
    }
}
