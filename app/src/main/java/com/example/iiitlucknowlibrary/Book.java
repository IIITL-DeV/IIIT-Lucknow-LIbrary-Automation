package com.example.iiitlucknowlibrary;

public class Book {
    String name;
    String author;
    String quantity;
    String satus;
    String imageUri;

    public Book() {
    }

    public Book(String name, String author, String quantity, String status, String imageUri) {
        this.author = author;
        this.name = name;
        this.quantity = quantity;
        this.satus = status;
        this.imageUri = imageUri;

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuantity() { return quantity; }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSatus() {
        return satus;
    }

    public void setSatus(String satus) {
        this.satus = satus;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
