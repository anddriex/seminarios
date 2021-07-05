package com.eapp.seminariosistemas;

import com.orm.SugarRecord;

public class Contact extends SugarRecord<Contact> {
    private String image;
    private String name;
    private String phone;
    private String address;

    public Contact(){}

    public Contact(String image, String name, String phone, String address) {
        this.image = image;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
