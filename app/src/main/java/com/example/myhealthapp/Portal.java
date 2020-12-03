package com.example.myhealthapp;

public class Portal {
    private String Address,Age, Annual_Checkup, Dr_Name, Dr_Visit_Date,
            Height, Phone,Relative_Email,Relative_Name,Relative_Phone, Sex,Weight;

    public Portal(String address, String age, String annual_checkup, String dr_Name, String dr_Visit_Date, String height, String phone, String relative_Email, String relative_Name, String relative_Phone, String sex, String weight) {
        Address = address;
        Age = age;
        Annual_Checkup = annual_checkup;
        Dr_Name = dr_Name;
        Dr_Visit_Date = dr_Visit_Date;
        Height = height;
        Phone = phone;
        Relative_Email = relative_Email;
        Relative_Name = relative_Name;
        Relative_Phone = relative_Phone;
        Sex = sex;
        Weight = weight;
    }

    public Portal() {
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getAnnual_checkup() {
        return Annual_Checkup;
    }

    public void setAnnual_checkup(String annual_checkup) {
        Annual_Checkup = annual_checkup;
    }

    public String getDr_Name() {
        return Dr_Name;
    }

    public void setDr_Name(String dr_Name) {
        Dr_Name = dr_Name;
    }

    public String getDr_Visit_Date() {
        return Dr_Visit_Date;
    }

    public void setDr_Visit_Date(String dr_Visit_Date) {
        Dr_Visit_Date = dr_Visit_Date;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRelative_Email() {
        return Relative_Email;
    }

    public void setReletive_Email(String relative_Email) {
        Relative_Email = relative_Email;
    }

    public String getRelative_Name() {
        return Relative_Name;
    }

    public void setRelative_Name(String relative_Name) {
        Relative_Name = relative_Name;
    }

    public String getRelative_Phone() {
        return Relative_Phone;
    }

    public void setRelative_Phone(String relative_Phone) {
        Relative_Phone = relative_Phone;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }
}
