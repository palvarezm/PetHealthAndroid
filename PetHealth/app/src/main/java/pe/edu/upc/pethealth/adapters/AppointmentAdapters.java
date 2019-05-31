package pe.edu.upc.pethealth.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.AddAppointmentActivity;
import pe.edu.upc.pethealth.activities.AddPetActivity;
import pe.edu.upc.pethealth.fragments.ChatsFragment;
import pe.edu.upc.pethealth.models.Appointment;

/**
 * Created by Usuario on 17/11/2017.
 */

public class AppointmentAdapters extends RecyclerView.Adapter<AppointmentAdapters.ViewHolder>{
    private Fragment fragment;
    private JsonArray cardInfo;

    public AppointmentAdapters(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_appointment,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JsonObject jsonCardInfo = cardInfo.get(position).getAsJsonObject();
        String fullDate = jsonCardInfo.get("appointment").getAsJsonObject().get("appt_date").getAsString();
        String fullStartTime = jsonCardInfo.get("appointment").getAsJsonObject().get("start_t").getAsString();

        holder.veterinaryTextView.setText(jsonCardInfo.get("veterinary").getAsJsonObject().get("name").getAsString());
        holder.vetTextView.setText(jsonCardInfo.get("veterinarian").getAsJsonObject().get("name").getAsString());
        holder.dateTextView.setText(fullDate.substring(0, Math.min(fullDate.length(), 10)));
        holder.descriptionTextView.setText(jsonCardInfo.get("appointment").getAsJsonObject().get("desc").getAsString());
        holder.startTimeTextView.setText(fullStartTime.substring(11, Math.min(fullDate.length(), 16)));
        /*holder.veterinarianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content,new ChatsFragment()).commit();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return cardInfo.size();
    }

    public void setAppointments(JsonArray cardInfo){
        this.cardInfo = cardInfo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView vetTextView;
        TextView veterinaryTextView;
        TextView dateTextView;
        TextView startTimeTextView;
        TextView descriptionTextView;
        Button veterinarianButton;

        public ViewHolder(View itemView) {
            super(itemView);
            vetTextView = (TextView) itemView.findViewById(R.id.vetTextView);
            veterinaryTextView = (TextView) itemView.findViewById(R.id.veterinaryTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            startTimeTextView = (TextView) itemView.findViewById(R.id.startTimeTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            veterinarianButton = (Button) itemView.findViewById(R.id.veterinarianButton);
        }
    }
}
