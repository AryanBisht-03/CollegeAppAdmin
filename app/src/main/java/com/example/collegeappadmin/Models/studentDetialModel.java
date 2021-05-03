package com.example.collegeappadmin.Models;

public class studentDetialModel {

    String image,name,rollNum;

    public studentDetialModel(String image, String name, String rollNum) {
        this.image = image;
        this.name = name;
        this.rollNum = rollNum;
    }

    public studentDetialModel() {
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

    public String getRollNum() {
        return rollNum;
    }

    public void setRollNum(String rollNum) {
        this.rollNum = rollNum;
    }
}
