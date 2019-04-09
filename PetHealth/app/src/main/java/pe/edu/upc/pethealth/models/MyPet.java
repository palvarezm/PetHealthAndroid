package pe.edu.upc.pethealth.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by genob on 18/09/2017.
 */

public class MyPet {
    private int id;
    private String name;
    private String race;
    private String birthDate;
    private String description;
    private String image;
    private String animalType;
    private int ownerId;

    public MyPet() {

    }

    public MyPet(int id, String name, String race, String birthDate, String description, String image, String animalType, int ownerId) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.birthDate = birthDate;
        this.description = description;
        this.image = image;
        this.animalType = animalType;
        this.ownerId = ownerId;
    }

    public String getAnimalType() {
        return animalType;
    }

    public MyPet setAnimalType(String animalType) {
        this.animalType = animalType;
        return this;
    }

    public int getId(){
        return id;
    }

    public MyPet setId(int id){
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MyPet setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MyPet setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public MyPet setImage(String image) {
        this.image = image;
        return this;
    }

    public String getRace() {
        return race;
    }

    public MyPet setRace(String race) {
        this.race = race;
        return this;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public MyPet setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public int getOwnerId(){ return  ownerId; }

    public MyPet setOwnerId(int ownerId){
        this.ownerId = ownerId;
        return this;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("name",name);
        bundle.putString("race",race);
        bundle.putString("birthDate", birthDate);
        bundle.putString("description",description);
        bundle.putString("image",image);
        bundle.putString("animalType",animalType);
        bundle.putInt("ownerId", ownerId);
        return bundle;
    }

    public static MyPet from(Bundle bundle){
        MyPet myPet = new MyPet();
        myPet.setId(bundle.getInt("id"))
                .setName(bundle.getString("name"))
                .setRace(bundle.getString("race"))
                .setBirthDate(bundle.getString("birthDate"))
                .setDescription(bundle.getString("description"))
                .setImage(bundle.getString("image"))
                .setAnimalType(bundle.getString("animalType"))
                .setOwnerId(bundle.getInt("ownerId"));
        return myPet;
    }

    public static MyPet from(JSONObject json){
        MyPet myPet = new MyPet();
        try {
            myPet.setId(json.getInt("petId"))
                    .setName(json.getString("name"))
                    .setRace(json.getString("race"))
                    .setBirthDate(json.getString("birthDate"))//TODO fix birthDate
                    .setDescription(json.getString("description"))
                    .setImage(json.getString("photo"))
                    .setAnimalType(json.getString("animalTypeName"))
                    .setOwnerId(json.getInt("ownerId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myPet;
    }

    public static List<MyPet> from(JSONArray jsonpets){
        List<MyPet> myPets = new ArrayList<>();
        for(int i =0; i<jsonpets.length();i++){
            try {
                myPets.add(from(jsonpets.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return myPets;
    }
}
