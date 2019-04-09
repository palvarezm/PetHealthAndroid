package pe.edu.upc.pethealth.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.google.android.gms.maps.model.LatLng;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.fragments.AboutVeterinaryFragment;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.models.Veterinary;

/**
 * Created by genob on 26/10/2017.
 */

public class SearchAdapters extends RecyclerView.Adapter<SearchAdapters.ViewHolder> {

    private List<Veterinary> veterinaries;

    public SearchAdapters() {
    }

    public SearchAdapters(List<Veterinary> veterinaries) {
        this.veterinaries = veterinaries;
    }

    public List<Veterinary> getVeterinaries() {
        return veterinaries;
    }

    public SearchAdapters setVeterinaries(List<Veterinary> veterinaries) {
        this.veterinaries = veterinaries;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search,parent,false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Veterinary veterinary = veterinaries.get(position);
        holder.loadingIndicatorView.setVisibility(View.VISIBLE);
        holder.vetANImageView.setVisibility(View.INVISIBLE);
        LatLng latLng = new LatLng(veterinary.getLatitude(),veterinary.getLongitude());
        loadImage(latLng,holder.vetANImageView,holder.loadingIndicatorView);
        holder.nameTextView.setText(veterinary.getName());
        holder.startsRatingBar.setRating(3);
        holder.locationTextView.setText("Jesus Maria");
        holder.forwardImageutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity context = (MainActivity) view.getContext();
                AboutVeterinaryFragment newFragment = new AboutVeterinaryFragment();
                newFragment.setArguments(veterinary.toBundle());
                context.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, newFragment)
                        .addToBackStack(null)
                        .commit();
                /*Intent intent = new Intent(context, AboutVeterinaryFragment.class);
                context.startActivity(intent);*/
            }
        });
    }

    private void loadImage(LatLng latLng, final ImageView imageView, final AVLoadingIndicatorView loadingIndicatorView){
        String url ="https://maps.googleapis.com/maps/api/streetview?size=120x120&location="+String.valueOf(latLng.latitude)+","+String.valueOf(latLng.longitude)+"&heading=100.78&pitch=-1.76&key=AIzaSyBImrJ_aAE2pxTYoMVu5OlTAOMNm1udTCA";
        System.out.println(url);
        AndroidNetworking.get(url)
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
        return veterinaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView vetANImageView;
        TextView nameTextView;
        RatingBar startsRatingBar;
        TextView locationTextView;
        ImageButton forwardImageutton;
        AVLoadingIndicatorView loadingIndicatorView;
        public ViewHolder(View itemView) {
            super(itemView);
            vetANImageView = (ImageView) itemView.findViewById(R.id.vetANImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            startsRatingBar = (RatingBar) itemView.findViewById(R.id.rateRatingBar);
            locationTextView = (TextView) itemView.findViewById(R.id.locationTextView);
            forwardImageutton = (ImageButton) itemView.findViewById(R.id.fordwardImageButton);
            loadingIndicatorView = (AVLoadingIndicatorView) itemView.findViewById(R.id.avi);
        }
    }
}
