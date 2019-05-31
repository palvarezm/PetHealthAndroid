package pe.edu.upc.pethealth.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.model.Progress;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.AddAppointmentActivity;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.adapters.AppointmentAdapters;
import pe.edu.upc.pethealth.network.LoggerCallback;
import pe.edu.upc.pethealth.network.PetHealthApiService;
import pe.edu.upc.pethealth.network.RestClient;
import pe.edu.upc.pethealth.network.RestView;
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {
    private RecyclerView appointmentRecyclerView;
    private AppointmentAdapters appointmentAdapters;
    private RecyclerView.LayoutManager appointmentLayoutManager;
    private Fragment fragment = this;

    private RestView<JsonArray> answer;
    private SharedPreferencesManager sharedPreferencesManager;

    DataClient dataClient;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this.getContext());
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        updateAppointment();
        appointmentRecyclerView = (RecyclerView) view.findViewById(R.id.appointmentRecyclerView);
        appointmentLayoutManager = new GridLayoutManager(view.getContext(), 1);
        ((MainActivity)getActivity()).setFragmentToolbar("Next Appointments",true,getFragmentManager());
        dataClient = Wearable.getDataClient(this.getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAppointment();
    }

    void sendWearData(String jsonString){
        String APPT_KEY = "appt.key";
        String APPT_PATH = "/appt";
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(APPT_PATH);
        putDataMapReq.getDataMap().putString(APPT_KEY,jsonString+"as");
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
        dataClient.putDataItem(putDataReq);
    }
    private void updateAppointment(){
        Call<RestView<JsonArray>> call = new RestClient().getWebServices().getAppts(sharedPreferencesManager.getAccessToken(), sharedPreferencesManager.getUser().getId());
        call.enqueue(new LoggerCallback<RestView<JsonArray>>(){
            @Override
            public void onResponse(Call<RestView<JsonArray>> call, Response<RestView<JsonArray>> response) {
                super.onResponse(call, response);
                answer =  response.body();
                sendWearData(answer.getData().toString());
                appointmentAdapters = new AppointmentAdapters(fragment);
                appointmentRecyclerView.setAdapter(appointmentAdapters);
                appointmentRecyclerView.setLayoutManager(appointmentLayoutManager);
                appointmentAdapters.setAppointments(answer.getData());
                appointmentAdapters.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RestView<JsonArray>> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("FAILURE", t.toString());
            }
        });

        /*AndroidNetworking.get(PetHealthApiService.APPOINTMENT_URL)
                .addPathParameter("userId", Integer.toString(sharedPreferencesManager.getUser().getId()))
                .addHeaders("access_token", sharedPreferencesManager.getAccessToken())
                .setPriority(Priority.LOW)
                .setTag(R.string.app_name)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            appointments = Appointment.from(response.getJSONArray("data"));
                            appointmentAdapters.setAppointments(appointments);
                            appointmentAdapters.notifyDataSetChanged();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.toString());
                    }
                });*/
    }

}
