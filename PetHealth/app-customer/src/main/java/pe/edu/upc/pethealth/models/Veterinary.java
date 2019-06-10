package pe.edu.upc.pethealth.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by genob on 26/10/2017.
 */

public class Veterinary {
    private String id;
    private String name;
    private String preVideo;
    private String phoneNumber;
    private Double longitude;
    private Double latitude;
    private String openingHours;
    private Float rating;

    public Veterinary() {
    }

    public String toString(){
        return name;
    }

    public Veterinary(String id, String name, String preVideo, String phoneNumber, Double longitude, Double latitude, String openingHours, Float rating) {
        this.id = id;
        this.name = name;
        this.preVideo = preVideo;
        this.phoneNumber = phoneNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.openingHours = openingHours;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public Veterinary setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Veterinary setName(String name) {
        this.name = name;
        return this;
    }

    public String getPreVideo() {
        return preVideo;
    }

    public Veterinary setPreVideo(String preVideo) {
        this.preVideo = preVideo;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Veterinary setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Veterinary setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Veterinary setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public Veterinary setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
        return this;
    }

    public Float getRating(){ return rating; }

    public Veterinary setRating(Float rating){
        this.rating = rating;
        return this;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("name",name);
        bundle.putString("pre_video",preVideo);
        bundle.putString("phone_number",phoneNumber);
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude",latitude);
        bundle.putString("opening_hours",openingHours);
        bundle.putFloat("rating",rating);
        return bundle;
    }
    public static Veterinary from(Bundle bundle){
        Veterinary veterinary = new Veterinary();
        veterinary.setId(bundle.getString("id"))
                .setName(bundle.getString("name"))
                .setPreVideo(bundle.getString("pre_video"))
                .setPhoneNumber(bundle.getString("phone_number"))
                .setLongitude(bundle.getDouble("longitude"))
                .setLatitude(bundle.getDouble("latitude"))
                .setOpeningHours(bundle.getString("opening_hours"))
                .setRating(bundle.getFloat("rating"));
        return veterinary;
    }
    public static Veterinary from(JSONObject json){
        Veterinary veterinary = new Veterinary();
        try {
            veterinary.setId(json.getString("VeterinaryId"))
                    .setName(json.getString("Name"))
                    .setPreVideo(json.getString("DescripVid"))
                    .setPhoneNumber(json.getString("PhoneNumber"))
                    .setLongitude(json.getDouble("Longitude"))
                    .setLatitude(json.getDouble("Latitude"))
                    .setOpeningHours(json.getString("OpeningHours"))
                    .setRating(Float.valueOf(json.getString("Rating")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return veterinary;
    }

    public static List<Veterinary> from(JSONArray jsonVeterinries){
        List<Veterinary> veterinaries = new ArrayList<>();
        for(int i =0; i<jsonVeterinries.length();i++){
            try {
                veterinaries.add(from(jsonVeterinries.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return veterinaries;
    }
}
