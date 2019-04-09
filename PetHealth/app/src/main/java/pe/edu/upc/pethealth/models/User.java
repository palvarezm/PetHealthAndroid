package pe.edu.upc.pethealth.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by genob on 27/10/2017.
 */

public class User {
    private int userId;
    private String username;
    private String password;
    private String mail;
    private String photo;
    private String bio;

    public User() {
    }

    public User(int userId, String username, String password, String mail, String photo, String bio) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.photo = photo;
        this.bio = bio;
    }

    public int getUserId() {
        return userId;
    }

    public User setUserId(int userId) {
        this.userId = userId;
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
        bundle.putInt("user_id",userId);
        bundle.putString("username",username);
        bundle.putString("password",password);
        bundle.putString("mail",mail);
        bundle.putString("photo",photo);
        bundle.putString("bio",bio);
        return bundle;
    }
    public static User from(Bundle bundle){
        User user = new User();
        user.setUserId(bundle.getInt("user_id"))
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
            user.setUserId(jsonUser.getInt("userId"))
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
}
