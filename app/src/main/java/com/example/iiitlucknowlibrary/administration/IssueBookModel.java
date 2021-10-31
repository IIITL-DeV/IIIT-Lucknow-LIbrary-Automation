package com.example.iiitlucknowlibrary.administration;

public class IssueBookModel {
    String issueDate;
    String returnDate;
    String issueId;
    String enrollment;
   public IssueBookModel(){}
    public IssueBookModel(String issueId,String issueDate, String returnDate ,String Enrollment ) {
        this.issueId=issueId;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.enrollment = Enrollment;
    }
    public String getEnrollment() {
        return enrollment;
    }
    public void setEnrollment(String Enrollment) {
        this.enrollment = Enrollment;
    }
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
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
