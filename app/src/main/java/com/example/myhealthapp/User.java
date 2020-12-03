package com.example.myhealthapp;

import android.app.Application;

public class User {
   private String name, email,phone, userId, password, security_Answer,security_Question;

    public User() {
    }

    public User(String email, String name,String phone, String password, String security_Answer, String security_Question, String userId) {
        this.email = email;
        this.name = name;
        this.phone = phone;
       this.userId = userId;
        this.password = password;
        this.security_Answer = security_Answer;
        this.security_Question = security_Question;
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

    public void setPhone(String phone){this.phone= phone;}

    public String getPhone(){
        return phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurity_Answer() {
        return security_Answer;
    }

    public void setSecurity_Answer(String security_Answer) {
        this.security_Answer = security_Answer;
    }

    public String getSecurity_Question() {
        return security_Question;
    }

    public void setSecurity_Question(String security_Question) {
        this.security_Question = security_Question;
    }
}
