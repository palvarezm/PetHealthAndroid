package pe.edu.upc.pethealth.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gonzalo on 20/11/2017.
 */

public class Vet {
    int vetId;
    String name;

    public Vet(int vetId, String name) {
        this.vetId = vetId;
        this.name = name;
    }

    public int getVetId() {
        return vetId;
    }

    public Vet setVetId(int vetId) {
        this.vetId = vetId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Vet setName(String name) {
        this.name = name;
        return this;
    }

    public Vet() {

    }

    public String toString(){
        return name;
    }

    public static Vet from(JSONObject jsonObject){
        Vet vet = new Vet();
        try {
            vet.setVetId(jsonObject.getInt("vetid"))
                    .setName(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vet;
    }

    public static List<Vet> from(JSONArray jsonArray){
        List<Vet> vets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                vets.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return vets;
    }
}
