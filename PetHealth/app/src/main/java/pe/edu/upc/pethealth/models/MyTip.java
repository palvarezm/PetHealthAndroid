package pe.edu.upc.pethealth.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by genob on 28/09/2017.
 */

public class MyTip {
    private int id;
    private String image;
    private String tittle;
    private String description;

    public MyTip(int id, String image, String tittle, String description) {
        this.id = id;
        this.image = image;
        this.tittle = tittle;
        this.description = description;
    }

    public MyTip() {
    }

    public int getId() {
        return id;
    }

    public MyTip setId(int id) {
        this.id = id;
        return this;
    }

    public String getImage() {
        return image;
    }

    public MyTip setImage(String image) {
        this.image = image;
        return this;
    }

    public String getTittle() {
        return tittle;
    }

    public MyTip setTittle(String tittle) {
        this.tittle = tittle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MyTip setDescription(String description) {
        this.description = description;
        return this;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("image",image);
        bundle.putString("tittle",tittle);
        bundle.putString("description",description);
        return bundle;
    }
    public static MyTip from(Bundle bundle){
        MyTip myTip = new MyTip();
        myTip.setId(bundle.getInt("id"))
                .setImage(bundle.getString("image"))
                .setTittle(bundle.getString("tittle"))
                .setDescription(bundle.getString("description"));
        return myTip;
    }

    public static MyTip from(JSONObject jsonTip){
        MyTip myTip = new MyTip();
        try {
            myTip.setId(jsonTip.getInt("TipId"))
                    .setTittle(jsonTip.getString("OwnerUsername"))
                    .setDescription(jsonTip.getString("Content"))
                    .setImage(jsonTip.getString("Image"));
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return myTip;
    }

    public static List<MyTip> from(JSONArray jsonTips){
        List<MyTip> myTips = new ArrayList<>();
        for(int i =0; i<jsonTips.length();i++){
            try {
                myTips.add(from(jsonTips.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return myTips;
    }
}
