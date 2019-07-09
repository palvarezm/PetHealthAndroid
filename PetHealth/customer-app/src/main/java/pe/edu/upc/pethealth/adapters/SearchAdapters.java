package pe.edu.upc.pethealth.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pe.edu.upc.lib.models.VeterinaryModel.Veterinary;
import pe.edu.upc.pethealth.R;

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
        Picasso.get().load("https://static.interbankbenefit.pe/public/web/images/evales/ficha-tecnica/groomers-ficha-tecnica.jpg").into(holder.ivVet);
        holder.nameTextView.setText(veterinary.getName());
        holder.locationTextView.setText("Jesus Maria");
        holder.forwardImageutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return veterinaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVet;
        TextView nameTextView;
        TextView locationTextView;
        ImageButton forwardImageutton;
        public ViewHolder(View itemView) {
            super(itemView);
            ivVet = (ImageView) itemView.findViewById(R.id.ivVet);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            locationTextView = (TextView) itemView.findViewById(R.id.locationTextView);
            forwardImageutton = (ImageButton) itemView.findViewById(R.id.fordwardImageButton);
        }
    }
}
