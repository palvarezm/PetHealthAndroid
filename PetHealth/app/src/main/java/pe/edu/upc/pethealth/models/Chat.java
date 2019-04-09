package pe.edu.upc.pethealth.models;

import android.os.Bundle;

/**
 * Created by genob on 29/09/2017.
 */

public class Chat {
    private int idUser;
    private int idVeterinario;
    private String contact;
    private String message;

    public Chat() {
    }

    public Chat(int idUser, int idVeterinario, String contact, String message) {
        this.idUser = idUser;
        this.idVeterinario = idVeterinario;
        this.contact = contact;
        this.message = message;
    }

    public int getIdUserr() {
        return idUser;
    }

    public Chat setIdUser(int idUser) {
        this.idUser = idUser;
        return this;
    }

    public int getIdVeterinario() {
        return idVeterinario;
    }

    public Chat setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public Chat setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Chat setMessage(String message) {
        this.message = message;
        return this;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("idUser",idUser);
        bundle.putInt("idVeterinario",idVeterinario);
        bundle.putString("contact",contact);
        bundle.putString("message",message);
        return bundle;
    }

    public static Chat from(Bundle bundle){
        Chat chat = new Chat();
        chat.setIdUser(bundle.getInt("idUser"))
                .setIdVeterinario(bundle.getInt("idVeterinario"))
                .setContact(bundle.getString("contact"))
                .setMessage(bundle.getString("message"));
        return chat;
    }
}
