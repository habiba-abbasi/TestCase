package com.example.habibaabbasii.testcase.model;

/**
 * Created by Roshaann 2.7 gpa on 15/04/2018.
 */

public class UserModel {
    String userNAme;
    String contact;
    String image;
    String uId;

    public UserModel() {
    }

    public UserModel(String userNAme, String contact, String image, String uId) {
        this.userNAme = userNAme;
        this.contact = contact;
        this.image = image;
        this.uId = uId;
    }

    public String getUserNAme() {
        return userNAme;
    }

    public void setUserNAme(String userNAme) {
        this.userNAme = userNAme;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
