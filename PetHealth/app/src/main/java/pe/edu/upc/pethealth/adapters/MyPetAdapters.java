package pe.edu.upc.pethealth.adapters;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.fragments.AppointmentFragment;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.fragments.MyPetDescriptionFragment;
import pe.edu.upc.pethealth.models.MyPet;

/**
 * Created by genob on 18/09/2017.
 */

public class MyPetAdapters extends RecyclerView.Adapter<MyPetAdapters.ViewHolder>{

    private List<MyPet> myPets;

    public MyPetAdapters(List<MyPet> myPets) {
        this.myPets = myPets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pets,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyPet myPet = myPets.get(position);
        holder.loadingIndicatorView.setVisibility(View.VISIBLE);
        holder.petANImageView.setVisibility(View.INVISIBLE);
        holder.petNameTextView.setText(myPets.get(position).getName());
        holder.petDescriptionTextView.setText(myPets.get(position).getDescription());
        loadImage(myPets.get(position).getImage(),holder.petANImageView,holder.loadingIndicatorView);
        holder.moreTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MainActivity context = (MainActivity) view.getContext();
                MyPetDescriptionFragment newFragment = new MyPetDescriptionFragment();
                newFragment.setArguments(myPet.toBundle());
                context.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.clinicHystoryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity context = (MainActivity) view.getContext();
                AppointmentFragment newFragment = new AppointmentFragment();
                //TODO add the bundle of HistoryClinic
                Bundle bundle = new Bundle();
                bundle.putInt("userId",myPet.getOwnerId());
                bundle.putInt("petId",myPet.getId());
                bundle.putString("pet",myPet.getName());
                newFragment.setArguments(bundle);
                context.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private void loadImage(String imageUrl, final ImageView imageView, final AVLoadingIndicatorView loadingIndicatorView){
        AndroidNetworking.get(imageUrl)
                .setTag("imageRequest")
                .setPriority(Priority.MEDIUM)
                .setBitmapMaxHeight(240)
                .setBitmapMaxWidth(240)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        imageView.setVisibility(View.VISIBLE);
                        loadingIndicatorView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.toString());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return myPets.size();
    }

    public List<MyPet> getMyPets() {
        return myPets;
    }

    public MyPetAdapters setMyPets(List<MyPet> myPets) {
        this.myPets = myPets;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView petNameTextView;
        ImageView petANImageView;
        TextView petDescriptionTextView;
        TextView moreTextView;
        ImageButton clinicHystoryImageButton;
        AVLoadingIndicatorView loadingIndicatorView;

        public ViewHolder(View itemView) {
            super(itemView);
            petNameTextView = (TextView) itemView.findViewById(R.id.petTittleTextView);
            petANImageView = (ImageView) itemView.findViewById(R.id.myPetImageView);
            petDescriptionTextView = (TextView) itemView.findViewById(R.id.myPetDescriptionTextView);
            moreTextView = (TextView) itemView.findViewById(R.id.moreTextView);
            clinicHystoryImageButton = (ImageButton) itemView.findViewById(R.id.historyClinicImageButton);
            loadingIndicatorView = (AVLoadingIndicatorView) itemView.findViewById(R.id.avi);
        }
    }
}
