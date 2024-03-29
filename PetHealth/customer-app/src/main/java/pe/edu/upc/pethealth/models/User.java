package pe.edu.upc.pethealth.models;

import android.os.Bundle;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by genob on 27/10/2017.
 */

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String mail;
    private String photo;
    private String bio;

    public User() {
    }

    public User(int id, String username, String password, String mail, String photo, String bio) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.photo = photo;
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public User setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public User setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public User setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("username",username);
        bundle.putString("password",password);
        bundle.putString("mail",mail);
        bundle.putString("photo",photo);
        bundle.putString("bio",bio);
        return bundle;
    }
    public static User from(Bundle bundle){
        User user = new User();
        user.setId(bundle.getInt("id"))
                .setUsername(bundle.getString("username"))
                .setPassword(bundle.getString("password"))
                .setMail(bundle.getString("mail"))
                .setPhoto(bundle.getString("photo"))
                .setBio(bundle.getString("bio"));
        return user;
    }

    public static User from(JSONObject jsonUser){
        User user = new User();
        try {
            user.setId(jsonUser.getInt("id"))
                    .setUsername(jsonUser.getString("username"))
                    .setPassword(jsonUser.getString("password"))
                    .setMail(jsonUser.getString("mail"))
                    .setPhoto(jsonUser.getString("photo"))
                    .setBio(jsonUser.getString("bio"));
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public static List<User> from(JSONArray jsonUsers){
        List<User> users = new ArrayList<>();
        for (int i=0; i<jsonUsers.length();i++){
            try {
                users.add(from(jsonUsers.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return users;
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
