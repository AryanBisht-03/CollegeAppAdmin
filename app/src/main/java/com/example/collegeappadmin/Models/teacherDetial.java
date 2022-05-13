package com.example.collegeappadmin.Models;

public class teacherDetial {
    String phone,gender,image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public teacherDetial(String phone, String gender) {
        this.phone = phone;
        this.gender = gender;
    }

    public teacherDetial() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
