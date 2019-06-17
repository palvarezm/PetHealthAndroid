package pe.edu.upc.pethealth.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/30/2017.
 */

public class DocumentType {
    private int id;
    private String name;
    private String shortening;

    public DocumentType() {
    }

    public DocumentType(int id, String name, String shortening) {
        this.setId(id);
        this.setName(name);
        this.setShortening(shortening);
    }


    public int getId() { return id; }

    public DocumentType setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() { return name; }

    public DocumentType setName(String name) {
        this.name = name;
        return this;
    }

    public String getShortening() { return shortening;}

    public DocumentType setShortening(String shortening) {
        this.shortening = shortening;
        return this;
    }

    public static DocumentType from(JSONObject jsonDocumentType){
        DocumentType documentType = new DocumentType();
        try{
            documentType.setId(jsonDocumentType.getInt("id"))
                    .setName(jsonDocumentType.getString("name"))
                    .setShortening(jsonDocumentType.getString("abreviation"));
            return documentType;
        }catch (JSONException e ){
            e.printStackTrace();
        }
        return null;
    }

    public static List<DocumentType> from(JSONArray jsonDocumentTypeList) {
        List<DocumentType> documentTypeList = new ArrayList<>();
        int length = jsonDocumentTypeList.length();
        for (int i=0; i <length; i++){
            try{
                documentTypeList.add(DocumentType.from(jsonDocumentTypeList.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return documentTypeList;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("name", name);
        bundle.putString("shortening", shortening);
        return bundle;
    }

    public static DocumentType from(Bundle bundle){
        DocumentType documentType = new DocumentType();

        documentType.setId(bundle.getInt("id"))
                .setName(bundle.getString("name"))
                .setShortening(bundle.getString("shortening"));
        return documentType;
    }
}
