package pe.edu.upc.pethealth.models;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 9/29/2017.
 */

public class Person  extends User{
        private String name;
        private String lastName;
        private String dni;
        private String address;
        private String phone;
        public Person() {
        }

    public Person(String name, String lastName, String dni, String address, String phone) {
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getDni() {
        return dni;
    }

    public Person setDni(String dni) {
        this.dni = dni;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Person setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Person setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("last_name", lastName);
        bundle.putString("dni", dni);
        bundle.putString("address", address);
        bundle.putString("phone", phone);

        return bundle;
    }

    public static Person from(Bundle bundle){
        Person person = new Person();
        person.setName(bundle.getString("name"))
                .setLastName(bundle.getString("last_name"))
                .setDni(bundle.getString("dni"))
                .setAddress(bundle.getString("address"))
                .setPhone(bundle.getString("phone"));

        return person;
    }

    public static Person from(JSONObject jsonPerson){
        Person person = new Person();
        try {
            person.setName(jsonPerson.getString("name"))
                    .setLastName(jsonPerson.getString("last_name"))
                    .setPhone(jsonPerson.getString("phone"))
                    .setDni(jsonPerson.getString("dni"))
                    .setAddress(jsonPerson.getString("address"));
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return person;
    }

}
