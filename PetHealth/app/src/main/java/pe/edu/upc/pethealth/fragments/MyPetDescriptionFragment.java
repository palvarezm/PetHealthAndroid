package pe.edu.upc.pethealth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.androidnetworking.widget.ANImageView;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.activities.MainActivity;
import pe.edu.upc.pethealth.models.MyPet;

public class MyPetDescriptionFragment extends Fragment {

    TextView nameTextView;
    ANImageView myPetImageView;
    TextView descriptionValueTextView;
    TextView raceValueTextView;
    TextView ageValueTextView;
    TextView animalTypeValueTextView;
    TextView descriptionTextView;
    TextView raceTextView;
    TextView ageTextView;
    TextView animalTypeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyPet myPet = MyPet.from(getArguments());
        String petName = myPet.getName()+"'s Description";
        ((MainActivity)getActivity()).setFragmentToolbar(petName,true,getFragmentManager());
        View view = inflater.inflate(R.layout.fragment_my_pet_description, container, false);

        nameTextView = (TextView) view.findViewById(R.id.petTittleTextView);
        myPetImageView  =(ANImageView) view.findViewById(R.id.myPetImageView);
        raceValueTextView = (TextView) view.findViewById(R.id.petRaceValueTextView);
        ageValueTextView = (TextView) view.findViewById(R.id.petAgeValueTextView);
        descriptionValueTextView = (TextView) view.findViewById(R.id.petDescriptionValueTextView);
        animalTypeValueTextView = (TextView) view.findViewById(R.id.petAnimalTypeValueTextView);
        raceTextView = (TextView) view.findViewById(R.id.petRaceTextView);
        ageTextView = (TextView) view.findViewById(R.id.petAgeTextView);
        descriptionTextView = (TextView) view.findViewById(R.id.petDescriptionTextView);
        animalTypeTextView = (TextView) view.findViewById(R.id.petAnimalTypeTextView);

        nameTextView.setText(myPet.getName());
        myPetImageView.setImageUrl(myPet.getImage());
        raceValueTextView.setText(myPet.getRace());
        ageValueTextView.setText((myPet.getBirthDate()));
        descriptionValueTextView.setText(myPet.getDescription());
        animalTypeValueTextView.setText(myPet.getAnimalType());
        return view;
    }
}
