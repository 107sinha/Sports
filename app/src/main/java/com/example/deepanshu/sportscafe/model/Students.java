package com.example.deepanshu.sportscafe.model;

/**
 * Created by deepanshu on 19/7/16.
 */
public class Students {

    private String id;
    private String name;
    private String address;
    private String email;
    private String number;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "{\"data\": [{\"name\":\"" + name + "\",\"address\":\"" + address + "\",\"email\":\""
                + email + "\",\"phone\":\"" + number + "\",\"image\":\"" + image + "\"}]}";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
