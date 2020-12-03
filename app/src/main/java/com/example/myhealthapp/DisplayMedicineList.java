package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;

public class DisplayMedicineList extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    ListView listView;
    TextView tvNight;
    ArrayList<String>medicineList;
    ArrayAdapter<String>adapter;
    String CurrentUserId;
    String displayed_string, displayed_medicine_name,intentValue, selected_Morning_time,selected_Afternoon_time,selected_night_time;
    medicationData medication_Data;
    int hour, min;


    private EditText medName,tot_quantity,quantity,morning_time,afternoon_time,night_time;
    private String st_medName,st_tot_quantity,st_quantity, st_morning_time,st_afternoon_time, st_night_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_medicine_list);
        CurrentUserId = (Prevalent.currentOnlineUser.getUserId());
        Intent intent = getIntent();
        intentValue = intent.getStringExtra("Message");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("User").child(CurrentUserId).child("Medication");
        listView = (ListView)findViewById(R.id.list1);
        medicineList = new ArrayList<>();


        adapter = new ArrayAdapter<>(DisplayMedicineList.this,
                R.layout.display_data,R.id.tvDataList,medicineList);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    medication_Data = item.getValue(medicationData.class);

                     medicineList.add(medication_Data.getMedicine()+"\nDose per day: "+medication_Data.getQuantity());

                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               displayed_string = adapterView.getItemAtPosition(i).toString();
               editMedicineDataDialog();
            }
        });

    }

    private void editMedicineDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayMedicineList.this);
        View view = LayoutInflater.from(DisplayMedicineList.this).inflate(R.layout.add_update_medication_dialog, null);
        builder.setView(view);

        splitString(displayed_string);
        System.out.println(displayed_string);
        System.out.println(displayed_medicine_name);

        medName = view.findViewById(R.id.edMedicine_Name);
        tot_quantity = view.findViewById(R.id.edMedication_TotalQuantity);
        quantity = view.findViewById(R.id.edMedication_Quantity);
        morning_time = view.findViewById(R.id.edMorning_time);
        tvNight = view.findViewById(R.id.tvNight);
        tvNight.setVisibility(View.INVISIBLE);
        afternoon_time = view.findViewById(R.id.edAfternoon_time);
        night_time = view.findViewById(R.id.edNight_time);
        night_time.setVisibility(View.INVISIBLE);
        setAfternoonTime();
        setMorningTime();


        if(intentValue.equals("False")){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    medicationData medData = snapshot.child(displayed_medicine_name).getValue(medicationData.class);

                    st_medName = medData.getMedicine();
                    medName.setText(st_medName);
                    st_quantity = medData.getQuantity();
                    quantity.setText(st_quantity);
                    st_tot_quantity = medData.getTotal_Quantity();
                    tot_quantity.setText(st_tot_quantity);
                    st_morning_time = medData.getM_med_time();
                    morning_time.setText(st_morning_time);
                    st_afternoon_time = medData.getA_med_time();
                    afternoon_time.setText(st_afternoon_time);
                    st_night_time = medData.getN_med_time();
                    night_time.setText(st_night_time);


                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            medName.setKeyListener(null);
            tot_quantity.setKeyListener(null);
            morning_time.setKeyListener(null);
            quantity.setKeyListener(null);
            afternoon_time.setKeyListener(null);
            night_time.setKeyListener(null);

            builder.setPositiveButton("Done                                             ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }) ;

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(intentValue.equals("True")){

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    medicationData medData = snapshot.child(displayed_medicine_name).getValue(medicationData.class);

                    st_medName = medData.getMedicine();
                    medName.setText(st_medName);
                    st_quantity = medData.getQuantity();
                    quantity.setText(st_quantity);
                    st_tot_quantity = medData.getTotal_Quantity();
                    tot_quantity.setText(st_tot_quantity);
                    st_morning_time = medData.getM_med_time();
                    morning_time.setText(st_morning_time);
                    st_afternoon_time = medData.getA_med_time();
                    afternoon_time.setText(st_afternoon_time);
                    st_night_time = medData.getN_med_time();
                    night_time.setText(st_night_time);


                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {


                    if(!st_medName.equals(medName.getEditableText().toString())){
                        reference.child(displayed_medicine_name).child("Medicine").setValue(medName.getEditableText().toString());
                    }
                    else  if(!st_quantity.equals(quantity.getEditableText().toString())){
                        reference.child(displayed_medicine_name).child("Quantity").setValue(quantity.getEditableText().toString());
                    }
                    else  if(!st_tot_quantity.equals(tot_quantity.getEditableText().toString())){
                        reference.child(displayed_medicine_name).child("Total_Quantity").setValue(tot_quantity.getEditableText().toString());
                    }
                    else if(!st_afternoon_time.equals(selected_Afternoon_time)){
                        reference.child(displayed_medicine_name).child("A_med_time").setValue(afternoon_time.getEditableText().toString());
                    }
                    else  if(!st_morning_time.equals(selected_Morning_time)){
                        reference.child(displayed_medicine_name).child("M_med_time").setValue(morning_time.getEditableText().toString());
                    }
                    else{
                        Toast.makeText(DisplayMedicineList.this, "Nothing has changed", Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.dismiss();

                    }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }


    }

    public void setMorningTime(){

        morning_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Use the current time as the default values for the picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        DisplayMedicineList.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hr, int minute) {
                        //Initialize hour and minute
                        hour = hr;
                        min = minute;
                        // Initialize Calander
                        Calendar cal = Calendar.getInstance();

                        cal.set(0,0,0,hour,min);
                        morning_time.setText(DateFormat.format("hh:mm aa",cal));
                        selected_Morning_time = morning_time.getText().toString();
                    }
                },12,0,false);
                //Displaying selectd time
                timePickerDialog.updateTime(hour,min);
                timePickerDialog.show();
            }
        });

    }

    public void setAfternoonTime(){

        afternoon_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Use the current time as the default values for the picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        DisplayMedicineList.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hr, int minute) {
                        //Initialize hour and minute
                        hour = hr;
                        min = minute;
                        // Initialize Calander
                        Calendar cal = Calendar.getInstance();

                        cal.set(0,0,0,hour,min);
                        afternoon_time.setText(DateFormat.format("hh:mm aa",cal));
                        selected_Afternoon_time = afternoon_time.getText().toString();
                    }
                },12,0,false);
                //Displaying selectd time
                timePickerDialog.updateTime(hour,min);
                timePickerDialog.show();
            }
        });

    }




    private void splitString(String str){

        String[] allStr = str.split("\n");
        displayed_medicine_name= allStr[0];
    }
}