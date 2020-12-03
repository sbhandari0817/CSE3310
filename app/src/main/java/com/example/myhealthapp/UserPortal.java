package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserPortal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView edVisitDate, tvGender ;
    private EditText edName,edEmail, edPhone, edAddress,edAge, edWeight,edHeight, edDname,
           edAnnual_checkup,edRel_name, edRel_phone,edRel_email;
    String Dr_Vist_Date, currentUserId;
    private Button btnSubmit, btnDone;
    private ProgressDialog loadingBar;
    Spinner spin_gender;
    String gender, strSex;
    FirebaseDatabase database;
    DatabaseReference reference;
    private String menuType;
    private String name, email,phone,address,age,weight, height,dr_name, dr_visit_date,
                    annual_check, rel_name, rel_phone, rel_email, sex;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_portal);
        final Intent intent = getIntent();
        menuType = intent.getStringExtra("Category");

        setInfo();

        currentUserId = (Prevalent.currentOnlineUser.getUserId());
        edName.setText(name);
        edEmail.setText(email);
        edPhone.setText(phone);



        if(menuType.equals("Add")) {
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     name = edName.getText().toString();
                     phone = edPhone.getText().toString();
                     email = edEmail.getText().toString();
                    String address = edAddress.getText().toString();
                    String age = edAge.getText().toString();
                    String weight = edWeight.getText().toString();
                    String height = edHeight.getText().toString();
                    String drName = edDname.getText().toString();
                    String AnnualCheckup = edAnnual_checkup.getText().toString();
                    String rel_Name = edRel_name.getText().toString();
                    String rel_Phone = edRel_phone.getText().toString();
                    String rel_email = edRel_email.getText().toString();
                    String drVisitDates = Dr_Vist_Date;
                    String sex = strSex;
                    if ((TextUtils.isEmpty(name)) || (TextUtils.isEmpty(phone))
                            || (TextUtils.isEmpty(address)) || (TextUtils.isEmpty(email))
                            || (TextUtils.isEmpty(age))|| (TextUtils.isEmpty(sex)) || (TextUtils.isEmpty(weight)) || (TextUtils.isEmpty(height))
                            || (TextUtils.isEmpty(drName)) || (TextUtils.isEmpty(AnnualCheckup))
                            || (TextUtils.isEmpty(rel_email)) || (TextUtils.isEmpty(rel_Name)) || (TextUtils.isEmpty(rel_Phone))) {
                        Toast.makeText(UserPortal.this, "Please Enter All the Credentials above", Toast.LENGTH_SHORT).show();
                    } else if (phone.length() != 10) {
                        Toast.makeText(UserPortal.this, "Phone Number is incorrect.", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(phone) || !(phone.matches("^[0-9]*")) || !(phone.length() == 10)) {

                        Toast.makeText(UserPortal.this, "Please enter your phone number correctly.\n it only contains 10 digit. ", Toast.LENGTH_SHORT).show();
                    } else if (!(email.contains("@")) && (email.contains("."))) {
                        Toast.makeText(UserPortal.this, "Email ID is incorrect.", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingBar.setTitle("Registration");
                        loadingBar.setMessage("Please wait while checking provided credentials");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        updateEmailchanged();
                        updateNameChanged();
                        updatePhonechanged();
                        updatePersonalInfoToDatabase(address, age, sex, weight, height, drName, drVisitDates, AnnualCheckup, rel_Name, rel_Phone, rel_email);
                    }


                }
            });
        }
        else if(menuType.equals("Update")){
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateNameChanged();
                    //updatePhonechanged();
                    updateEmailchanged();
                    updateAddresschanged();
                    updateAgechanged();
                    updateAnnualCheckupchanged();
                    updateHeightchanged();
                    updateWeightChanged();
                    updateDrNamechanged();
                    updatekinEmailChanged();
                    updatekinNameChanged();
                    updatekinPhoneChanged();
                    Intent intent = new Intent(UserPortal.this,PersonalInformation.class);
                    startActivity(intent);
                }
            });
        }
        else{
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserPortal.this,PersonalInformation.class);
                    startActivity(intent);
                }
            });
        }

        edVisitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int YEAR = c.get(Calendar.YEAR);
                int MONTH = c.get(Calendar.MONTH);
                int DAY_0F_MONTH = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UserPortal.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, day);
                        Dr_Vist_Date = DateFormat.getDateInstance().format(c.getTime());
                        edVisitDate.setText(Dr_Vist_Date);

                    }
                }, YEAR, MONTH, DAY_0F_MONTH);
                datePickerDialog.show();
            }
        });
    }

    private void updatePersonalInfoToDatabase(final String address, final String age, final String sex, final String weight, final String height, final String drName, final String drVisitDates, final String annualCheckup, final String rel_name, final String rel_phone, final String rel_email) {

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.child("User").child(currentUserId).child("User Portal").exists()) {
                        HashMap<String, Object> userPortal = new HashMap<>();
                        userPortal.put("Address", address);
                        userPortal.put("Age", age);
                        userPortal.put("Sex", sex);
                        userPortal.put("Weight", weight);
                        userPortal.put("Height", height);
                        userPortal.put("Dr_Name", drName);
                        userPortal.put("Dr_Visit_Date", drVisitDates);
                        userPortal.put("Annual_Checkup", annualCheckup);
                        userPortal.put("Relative_Name", rel_name);
                        userPortal.put("Relative_Email", rel_email);
                        userPortal.put("Relative_Phone", rel_phone);

                        reference.child("User").child(currentUserId).child("User Portal").updateChildren(userPortal)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Toast.makeText(UserPortal.this, "Congratulation, your account has been created", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                            Intent intent = new Intent(UserPortal.this, Home.class);
                                            startActivity(intent);
                                        } else {
                                            loadingBar.dismiss();
                                            Toast.makeText(UserPortal.this, "Failed to submit.", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void updateEmailchanged() {
        if (!email.equals(edEmail.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("Login").
                    child("email").setValue(edEmail.getEditableText().toString());
        }
    }
    private void updatePhonechanged() {
        if (!phone.equals(edPhone.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("Login").
                    child("phone").setValue(edPhone.getEditableText().toString());
        }
    }
    private void updateNameChanged() {
        if (!name.equals(edName.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("Login").
                    child("name").setValue(edName.getEditableText().toString());
        }
    }
    private void updateAddresschanged() {
        if (!address.equals(edAddress.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("User Portal").
                    child("Address").setValue(edAddress.getEditableText().toString());
        }
    }

    private void updateAgechanged() {
        if (!age.equals(edAge.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("User Portal").
                    child("Age").setValue(edAge.getEditableText().toString());
        }
    }
    private void updateWeightChanged() {

        if (!weight.equals(edWeight.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("User Portal").
                    child("Weight").setValue(edWeight.getEditableText().toString());
        }
    }
    private void updateHeightchanged() {
        if (!height.equals(edHeight.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("User Portal").
                    child("Height").setValue(edHeight.getEditableText().toString());
        }
    }
    private void updateDrNamechanged() {

        if (!dr_name.equals(edDname.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("User Portal").
                    child("Dr_Name").setValue(edDname.getEditableText().toString());
        }
    }


    private void updateAnnualCheckupchanged() {
        if (!annual_check.equals(edAnnual_checkup.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("User Portal").
                    child("Annual_Checkup").setValue(edAnnual_checkup.getEditableText().toString());
        }
    }


    private void updatekinNameChanged() {
        if (!rel_name.equals(edRel_name.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("User Portal").
                    child("Relative_Name").setValue(edRel_name.getEditableText().toString());
        }
    }


    private void updatekinPhoneChanged() {
        if (!rel_phone.equals(edRel_phone.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("User Portal").
                    child("Relative_phone").setValue(edRel_phone.getEditableText().toString());
        }
    }
    private void updatekinEmailChanged() {
        if (!rel_email.equals(edRel_phone.getEditableText().toString())) {
            reference.child("User").child(currentUserId).child("User Portal").
                    child("Relative_phone").setValue(edRel_email.getEditableText().toString());
        }
    }








    public void setInfo(){
        edVisitDate = (TextView) findViewById(R.id.edVisit_Date);
        spin_gender = (Spinner) findViewById(R.id.spinner_sex);
        spin_gender.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Sex, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_gender.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        btnSubmit = (Button)findViewById(R.id.btn_Submit);
        tvGender = (TextView)findViewById(R.id.tvGender);
        btnDone = (Button)findViewById(R.id.btn_Done);
        edName = (EditText)findViewById(R.id.edName);
        edEmail = (EditText)findViewById(R.id.edEmail);
        edPhone = (EditText)findViewById(R.id.edPhone);
        edAddress = (EditText)findViewById(R.id.edAddress);
        edAge = (EditText)findViewById(R.id.edAge);
        edWeight = (EditText)findViewById(R.id.edWeight);
        edHeight = (EditText)findViewById(R.id.edHeight);
        edDname = (EditText)findViewById(R.id.edDname);
        edAnnual_checkup = (EditText)findViewById(R.id.edAnnual_checkup);
        edRel_name = (EditText)findViewById(R.id.ed_Kin_Name) ;
        edRel_email = (EditText)findViewById(R.id.ed_Kin_email) ;
        edRel_phone= (EditText)findViewById(R.id.ed_Kin_Phone) ;
        loadingBar = new ProgressDialog(this);

        if(menuType.equals("Update")){

            getUserPortal();
            btnSubmit.setVisibility(View.INVISIBLE);
            edVisitDate.setKeyListener(null);
        }
        if(menuType.equals("View")){
            getUserPortal();
            edName.setKeyListener(null);
            edPhone.setKeyListener(null);
            edEmail.setKeyListener(null);
            edAnnual_checkup.setKeyListener(null);
            edAddress.setKeyListener(null);
            edAge.setKeyListener(null);
            edWeight.setKeyListener(null);
            edHeight.setKeyListener(null);
            edRel_name.setKeyListener(null);
            edRel_email.setKeyListener(null);
            edRel_phone.setKeyListener(null);
            edVisitDate.setKeyListener(null);
            edDname.setKeyListener(null);
            spin_gender.setOnKeyListener(null);
            tvGender.setKeyListener(null);
            btnSubmit.setVisibility(View.INVISIBLE);
        }


    }

    public void getUserPortal(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("User").child(currentUserId).child("User Portal").exists()) {
                    Portal userPortal = snapshot.child("User").child(currentUserId).child("User Portal").getValue(Portal.class);
                    Prevalent.currentUserPortal = userPortal;
                    phone = Prevalent.currentOnlineUser.getPhone();
                    edPhone.setText(phone);

                    name = Prevalent.currentOnlineUser.getName();
                    edName.setText(name);

                    email = Prevalent.currentOnlineUser.getEmail();
                    edEmail.setText(email);

                    address = Prevalent.currentUserPortal.getAddress();
                    edAddress.setText(address);
                    age = Prevalent.currentUserPortal.getAge();
                    edAge.setText(age);
                    weight = Prevalent.currentUserPortal.getWeight();
                    edWeight.setText(weight);
                    height = Prevalent.currentUserPortal.getHeight();
                    edHeight.setText(height);
                    dr_name = Prevalent.currentUserPortal.getDr_Name();
                    edDname.setText(dr_name);

                    dr_visit_date = Prevalent.currentUserPortal.getDr_Visit_Date();
                    edVisitDate.setText(dr_visit_date);
                    annual_check = Prevalent.currentUserPortal.getAnnual_checkup();
                    edAnnual_checkup.setText(annual_check);
                    rel_name = Prevalent.currentUserPortal.getRelative_Name();
                    edRel_name.setText(rel_name);
                    rel_email = Prevalent.currentUserPortal.getRelative_Email();
                    edRel_email.setText(rel_email);
                    rel_phone = Prevalent.currentUserPortal.getRelative_Phone();
                    edRel_phone.setText(rel_phone);
                    sex = (Prevalent.currentUserPortal.getSex());
                    tvGender.setText(sex);


                    /*edAge.setText(Prevalent.currentUserPortal.getAge());
                    edWeight.setText(Prevalent.currentUserPortal.getWeight());
                    edHeight.setText(Prevalent.currentUserPortal.getHeight());
                    edDname.setText(Prevalent.currentUserPortal.getDr_Name());
                    edVisitDate.setText(Prevalent.currentUserPortal.getDr_Visit_Date());
                    edAnnual_checkup.setText(Prevalent.currentUserPortal.getAnnual_checkup());
                    edRel_phone.setText(Prevalent.currentUserPortal.getRelative_Phone());
                    edRel_email.setText(Prevalent.currentUserPortal.getRelative_Email());
                    edRel_name.setText(Prevalent.currentUserPortal.getRelative_Name());
                    tvGender.setText(Prevalent.currentUserPortal.getSex());*/
                }
                else{
                    Toast.makeText(UserPortal.this, "Please add your information first", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gender = adapterView.getItemAtPosition(i).toString();
        strSex= gender;
        tvGender.setText(strSex);
        if(menuType.equals("Update")||(menuType.equals("View"))){
            tvGender.setText(Prevalent.currentUserPortal.getSex());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                Intent intent = new Intent(UserPortal.this, MainActivity.class);
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