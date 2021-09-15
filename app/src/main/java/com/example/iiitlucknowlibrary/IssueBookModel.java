package com.example.iiitlucknowlibrary;

public class IssueBookModel {
    String author;
    String name;
    String issueDate;
    String returnDate;

    public IssueBookModel() {
    }

    public IssueBookModel(String author, String name, String issueDate, String returnDate) {
        this.author = author;
        this.name = name;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
