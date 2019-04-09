package pe.edu.upc.pethealth.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.AddAppointmentActivity;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.adapters.AppointmentAdapters;
import pe.edu.upc.pethealth.models.Appointment;
import pe.edu.upc.pethealth.network.PetHealthApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {
    private RecyclerView appointmentRecyclerView;
    private AppointmentAdapters appointmentAdapters;
    private RecyclerView.LayoutManager appointmentLayoutManager;
    private FloatingActionButton addAppointmentFloatingActionButton;
    List<Appointment> appointments;
    int petId;
    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final Bundle bundle = getArguments();
        int ownerId = bundle.getInt("ownerId");
        petId = bundle.getInt("petId");
        String fragmentName = bundle.getString("pet")+"'s Appointments";
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        ((MainActivity)getActivity()).setFragmentToolbar(fragmentName,true,getFragmentManager());
        appointmentRecyclerView = (RecyclerView) view.findViewById(R.id.appointmentRecyclerView);
        appointments = new ArrayList<>();
        appointmentAdapters = new AppointmentAdapters(appointments);
        appointmentRecyclerView.setAdapter(appointmentAdapters);
        appointmentLayoutManager = new GridLayoutManager(view.getContext(), 1);
        appointmentRecyclerView.setLayoutManager(appointmentLayoutManager);
        addAppointmentFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.addAppointmentFloatingActionButton);
        addAppointmentFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddAppointmentActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        updateAppointment();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAppointment();
    }

    private void updateAppointment(){
        AndroidNetworking.get(PetHealthApiService.APPOINTMENT_URL+"/"+petId)
                .setPriority(Priority.LOW)
                .setTag(R.string.app_name)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            appointments = Appointment.from(response.getJSONArray("content"));
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
                });
    }

}
