package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class MedicationHome extends AppCompatActivity {
    private Button btnAddMedication,btnUpdateMedication,btnViewMedication,btnMenuMedication;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    String CurrentUserId, selected_Morning_time, selected_Afternoon_time,selected_night_time;
    private EditText edMorning, edMed_name,edMed_total_quantity,edMed_quantity,edAfternoon, edNight;
    private TextView tvNight;
    boolean checkIfUserIsNew = false;
    int hour, min;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_home);
        CurrentUserId = (Prevalent.currentOnlineUser.getUserId());
        btnAddMedication = (Button)findViewById(R.id.btnAddMedication);
        btnUpdateMedication = (Button)findViewById(R.id.btnUpdateMedication);
        btnViewMedication = (Button)findViewById(R.id.btnViewMedication);
        btnMenuMedication = (Button)findViewById(R.id.btnMenuMedication);
        tvNight = (TextView)findViewById(R.id.tvNight);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        loadingBar=new ProgressDialog(this);


        checkIfNewUser();
        if(checkIfUserIsNew){
            btnUpdateMedication.setVisibility(View.INVISIBLE);
            btnViewMedication.setVisibility(View.INVISIBLE);
        }

        btnAddMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddMedication();

            }
        });
        btnUpdateMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicationHome.this,DisplayMedicineList.class);
                intent.putExtra("Message","True");
                startActivity(intent);

            }
        });
        btnViewMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicationHome.this,DisplayMedicineList.class);
                intent.putExtra("Message","False");
                startActivity(intent);

            }
        });
        btnMenuMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicationHome.this,Home.class);
                startActivity(intent);
            }
        });

    }

    private void dialogAddMedication() {
        androidx.appcompat.app.AlertDialog.Builder builder  = new AlertDialog.Builder(MedicationHome.this);
        View view  = LayoutInflater.from(MedicationHome.this).inflate(R.layout.add_update_medication_dialog, null);
        builder.setView(view);


        edMorning = view.findViewById(R.id.edMorning_time);
        edAfternoon = view.findViewById(R.id.edAfternoon_time);
        edNight = view.findViewById(R.id.edNight_time);
        edNight.setVisibility(View.INVISIBLE);
        edMed_quantity= view.findViewById(R.id.edMedication_Quantity);
        edMed_total_quantity = view.findViewById(R.id.edMedication_TotalQuantity);
        edMed_name = view.findViewById(R.id.edMedicine_Name);
        tvNight.setVisibility(View.INVISIBLE);
        setMorningTime();
        setAfternoonTime();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MedicationHome.this, "thank you", Toast.LENGTH_SHORT).show();

                String medName= edMed_name.getEditableText().toString();
                String medQuantity = edMed_quantity.getEditableText().toString();
                String medTotalQuantity = edMed_total_quantity.getEditableText().toString();

                if(TextUtils.isEmpty(medName)){
                    Toast.makeText(MedicationHome.this, "Please enter the name of the medicine.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(medQuantity)){
                    Toast.makeText(MedicationHome.this, "Please enter the QUANTITY that need to be taken each day.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(medTotalQuantity)){
                    Toast.makeText(MedicationHome.this, "Please enter the TOTAL QUANTITY of the medicine.", Toast.LENGTH_SHORT).show();
                }
                else{

                loadingBar.setTitle("Updating.....");
                loadingBar.setMessage("Adding medicatio...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                addMedication(medName,medQuantity,medTotalQuantity,selected_Morning_time,selected_Afternoon_time,selected_night_time);
                }

            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private void addMedication(final String medName, final String medQuantity, final String medTotalQuantity, final String selected_morning_time, final String selected_afternoon_time, final String selected_night_time) {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("User").child(CurrentUserId).child("Medication").child(medName).exists()){
                    HashMap<String,Object>medicationData = new HashMap<>();
                    medicationData.put("Medicine", medName);
                    medicationData.put("Total_Quantity",medTotalQuantity);
                    medicationData.put("Quantity",medQuantity);
                    medicationData.put("M_med_time",selected_morning_time);
                    medicationData.put("A_med_time",selected_afternoon_time);
                    medicationData.put("N_med_time",selected_night_time);

                    reference.child("User").child(CurrentUserId).child("Medication").child(medName).updateChildren(medicationData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MedicationHome.this, "Succesfully Updated", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(MedicationHome.this, "Failed to update!\n Please try again!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

                else{
                    Toast.makeText(MedicationHome.this, "There is an medicine with the same name in your list. " +
                            "\n Please try with different name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    public void setMorningTime(){

        edMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Use the current time as the default values for the picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        MedicationHome.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hr, int minute) {
                        //Initialize hour and minute
                        hour = hr;
                        min = minute;
                        // Initialize Calander
                        Calendar cal = Calendar.getInstance();

                        cal.set(0,0,0,hour,min);
                        edMorning.setText(DateFormat.format("hh:mm aa",cal));
                        selected_Morning_time = edMorning.getText().toString();
                    }
                },12,0,false);
                //Displaying selectd time
                timePickerDialog.updateTime(hour,min);
                timePickerDialog.show();
            }
        });

    }

    public void setAfternoonTime(){

        edAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Use the current time as the default values for the picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        MedicationHome.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hr, int minute) {
                        //Initialize hour and minute
                        hour = hr;
                        min = minute;
                        // Initialize Calander
                        Calendar cal = Calendar.getInstance();

                        cal.set(0,0,0,hour,min);
                        edAfternoon.setText(DateFormat.format("hh:mm aa",cal));
                        selected_Afternoon_time = edAfternoon.getText().toString();
                    }
                },12,0,false);
                //Displaying selectd time
                timePickerDialog.updateTime(hour,min);
                timePickerDialog.show();
            }
        });

    }







    public void checkIfNewUser(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("User").child(CurrentUserId).child("Medication").exists()) {
                    checkIfUserIsNew= false;
                }
                else {
                    checkIfUserIsNew= true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case (R.id.Logout):
                Intent intent = new Intent(MedicationHome.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;

            default:
                break;

        }
        return true;


    }
}