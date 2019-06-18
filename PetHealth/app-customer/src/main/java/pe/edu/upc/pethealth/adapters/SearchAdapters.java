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

import com.google.android.gms.maps.model.LatLng;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import pe.edu.upc.pethealth.R;

public class SearchAdapters extends RecyclerView.Adapter<SearchAdapters.ViewHolder> {

    private List<pe.edu.upc.lib.Veterinary> veterinaries;

    public SearchAdapters() {
    }

    public SearchAdapters(List<pe.edu.upc.lib.Veterinary> veterinaries) {
        this.veterinaries = veterinaries;
    }

    public List<pe.edu.upc.lib.Veterinary> getVeterinaries() {
        return veterinaries;
    }

    public SearchAdapters setVeterinaries(List<pe.edu.upc.lib.Veterinary> veterinaries) {
        this.veterinaries = veterinaries;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search,parent,false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final pe.edu.upc.lib.Veterinary veterinary = veterinaries.get(position);
        LatLng latLng = new LatLng(veterinary.getLatitude(),veterinary.getLongitude());
        holder.nameTextView.setText(veterinary.getName());
        holder.startsRatingBar.setRating(3);
        holder.locationTextView.setText("Jesus Maria");
        holder.forwardImageutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void loadImage(LatLng latLng, final ImageView imageView, final AVLoadingIndicatorView loadingIndicatorView){
    }

    @Override
    public int getItemCount() {
        return veterinaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        RatingBar startsRatingBar;
        TextView locationTextView;
        ImageButton forwardImageutton;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            startsRatingBar = (RatingBar) itemView.findViewById(R.id.rateRatingBar);
            locationTextView = (TextView) itemView.findViewById(R.id.locationTextView);
            forwardImageutton = (ImageButton) itemView.findViewById(R.id.fordwardImageButton);
        }
    }
}
