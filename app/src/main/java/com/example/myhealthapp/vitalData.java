package com.example.myhealthapp;

public class vitalData {
    private String Name,Data;

    public vitalData() {
    }

    public vitalData(String name, String data) {
        Name = name;
        Data = data;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
