package com.example.myhealthapp;

public class medicationData {
    private String Medicine;
    private String Total_Quantity;
    private String Quantity;
    private String M_med_time;
    private String A_med_time;
    private String N_med_time;


    public medicationData() {
    }



    public medicationData(String medicine, String total_Quantity, String quantity, String m_med_time, String a_med_time, String n_med_time) {
        Medicine = medicine;
        Total_Quantity = total_Quantity;
        Quantity = quantity;
        M_med_time = m_med_time;
        A_med_time = a_med_time;
        N_med_time = n_med_time;
    }

    public String getMedicine() {
        return Medicine;
    }

    public void setMedicine(String medicine) {
        Medicine = medicine;
    }

    public String getTotal_Quantity() {
        return Total_Quantity;
    }

    public void setTotal_Quantity(String total_Quantity) {
        Total_Quantity = total_Quantity;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getM_med_time() {
        return M_med_time;
    }

    public void setM_med_time(String m_med_time) {
        M_med_time = m_med_time;
    }

    public String getA_med_time() {
        return A_med_time;
    }

    public void setA_med_time(String a_med_time) {
        A_med_time = a_med_time;
    }

    public String getN_med_time() {
        return N_med_time;
    }

    public void setN_med_time(String n_med_time) {
        N_med_time = n_med_time;
    }
}



