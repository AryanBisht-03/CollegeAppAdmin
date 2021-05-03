package com.example.collegeappadmin.Models;

public class itemModel {
    String name,issueDate,returnDate,quantity,Uid,type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public itemModel() {

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public itemModel(String name, String issueDate, String returnDate, String quantity) {
        this.name = name;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.quantity = quantity;
    }
}
