package com.example.iiitlucknowlibrary;

public class Book {
    String BookID;
    String name;
    String author;
    String quantity;
    String status;
    String imageUri;
    String category;

    public Book() {
    }

    public Book(String BookID, String name, String author, String category, String quantity, String status, String imageUri) {
        this.BookID = BookID;
        this.author = author;
        this.category = category;
        this.name = name;
        this.quantity = quantity;
        this.status = status;
        this.imageUri = imageUri;

    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String BookID) {
        this.BookID = BookID;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
