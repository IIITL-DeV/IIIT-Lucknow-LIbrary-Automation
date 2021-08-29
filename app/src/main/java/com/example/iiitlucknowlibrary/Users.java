package com.example.iiitlucknowlibrary;

public class Users {
    String uid;
    String name;
    String email;
    String imageUri;
    String status;

    public Users() {
    }

    public Users(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
