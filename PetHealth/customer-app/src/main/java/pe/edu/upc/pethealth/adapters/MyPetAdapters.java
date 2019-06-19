package pe.edu.upc.pethealth.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pe.edu.upc.pethealth.R;
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
        holder.petNameTextView.setText(myPets.get(position).getName());
        holder.petDescriptionTextView.setText(myPets.get(position).getDescription());
        Picasso.get().load(myPets.get(position).getImage()).error(R.mipmap.ic_launcher).into(holder.petPhoto);
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
        ImageView petPhoto;
        TextView petDescriptionTextView;
        TextView moreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            petNameTextView = (TextView) itemView.findViewById(R.id.petTittleTextView);
            petPhoto = (ImageView) itemView.findViewById(R.id.myPetImageView);
            petDescriptionTextView = (TextView) itemView.findViewById(R.id.myPetDescriptionTextView);
            moreTextView = (TextView) itemView.findViewById(R.id.moreTextView);
        }
    }
}
