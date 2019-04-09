package pe.edu.upc.pethealth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.models.Appointment;

/**
 * Created by Usuario on 17/11/2017.
 */

public class AppointmentAdapters extends RecyclerView.Adapter<AppointmentAdapters.ViewHolder>{

    private List<Appointment> appointments;

    public AppointmentAdapters(List<Appointment> myPets) {
        this.appointments = myPets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_appointment,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Appointment appointment = appointments.get(position);
        holder.veterinaryTextView.setText(appointment.getVeterinaryName());
        holder.vetTextView.setText(appointment.getVetName());
        holder.dateTextView.setText(appointment.getDate());
        holder.descriptionTextView.setText(appointment.getDescription());
        holder.prescriptionTextView.setText(appointment.getPrescription());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void setAppointments(List<Appointment>appointments){
        this.appointments = appointments;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView vetTextView;
        TextView veterinaryTextView;
        TextView dateTextView;
        TextView descriptionTextView;
        TextView prescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            vetTextView = (TextView) itemView.findViewById(R.id.vetTextView);
            veterinaryTextView = (TextView) itemView.findViewById(R.id.veterinaryTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            prescriptionTextView = (TextView) itemView.findViewById(R.id.prescriptionTextView);
        }
    }
}
