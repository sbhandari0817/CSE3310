package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.HashMap;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edName, edEmail, edPhone, edUserID, edPassword;
    private EditText edCon_Password, edSecurity_Answer;
    private TextView tvCancel, tvExit;
    private Button btnSubmit;
    private Spinner spin_SecurityQuestion;
    private String strSecurityQuestion, security_question;
   // private String name, email, phone, userId,password,confirm_password,security_ans;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setInfo();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, phone, userId,password,confirm_password,security_ans;
                name = edName.getText().toString();
                email = edEmail.getText().toString();
                userId = edUserID.getText().toString();
                phone = edPhone.getText().toString();
                password = edPassword.getText().toString();
                confirm_password = edCon_Password.getText().toString();
                security_ans = edSecurity_Answer.getText().toString();
                security_question = strSecurityQuestion;


                if (TextUtils.isEmpty(name) || !(name.matches("^[a-zA-Z ]*$"))) {
                    Toast.makeText(Register.this, "Name cannot be empty and It should not\n " +
                            "contain any character or digit", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email) || !(email.contains("@") && (email.contains(".")))) {
                    Toast.makeText(Register.this, "Please enter valid email.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userId)||!(userId.length()>=8)||!(userId.matches("^[a-zA-Z0-9_]*$"))) {
                    Toast.makeText(Register.this, "Please check your user Id.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)
                        ||!(password.equals(confirm_password))
                        ||(!(password.matches("^[a-zA-Z0-9]*$"))
                        &&!(password.matches(".*[`~!@#$%^&*()\\\\-_=+\\\\\\\\|\\\\[{\\\\]};:'\\\",<.>/?].*"))))
                {
                    Toast.makeText(Register.this, "Password must include letters, at least one number\n at least one capital letter and special character", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(confirm_password)) {
                    Toast.makeText(Register.this, "Please Enter your Confirm Password", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(phone)||!(phone.matches("^[0-9]*"))||!(phone.length()==10)){

                    Toast.makeText(Register.this, "Please enter your phone number correctly.\n it only contains 10 digit. ", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(security_ans)){
                    Toast.makeText(Register.this, "Please enter security answer", Toast.LENGTH_SHORT).show();

                }
                else{
                    loadingBar.setTitle("Registration");
                    loadingBar.setMessage("Please wait while checking provided credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    validateUserRegistration(name,email,phone,userId,password,security_ans,security_question);
                }
            } });


        tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edName.setText(null);
                        edEmail.setText(null);
                        edPhone.setText(null);
                        edUserID.setText(null);
                        edPassword.setText(null);
                        edCon_Password.setText(null);
                        edSecurity_Answer.setText(null);

                    }
                });
        tvExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private void validateUserRegistration(final String name, final String email, final String phone, final String userId, final String password, final String security_ans, final String security_question) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myReference = database.getReference();

        myReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("User").child("Login").child(userId).exists()){
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("name", name);
                    userDataMap.put("email", email);
                    userDataMap.put("phone",phone);
                    userDataMap.put("userId",userId );
                    userDataMap.put("password", password);
                    userDataMap.put("security_Answer", security_ans);
                    userDataMap.put("security_Question", security_question);

                    myReference.child("User").child(userId).child("Login").updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Congratulation, your account has been created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(Register.this, "Failed to create your account", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
                else{
                    Toast.makeText(Register.this, "There is an account with this user Id.\n Please use different Id", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void setInfo() {
                spin_SecurityQuestion = (Spinner) findViewById(R.id.spinner_SecurityQuestion);
                spin_SecurityQuestion.setOnItemSelectedListener(this);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Security_question, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_SecurityQuestion.setAdapter(adapter);
                loadingBar = new ProgressDialog(this);
                edName = (EditText) findViewById(R.id.edName);
                edEmail = (EditText) findViewById(R.id.edEmail);
                edPhone = (EditText) findViewById(R.id.edPhone);
                edPassword = (EditText) findViewById(R.id.edPassword);
                edUserID = (EditText) findViewById(R.id.edUserId);
                edCon_Password = (EditText) findViewById(R.id.edConfirmPassword);
                edSecurity_Answer = (EditText) findViewById(R.id.edSecurityAnswer);
                btnSubmit = (Button) findViewById(R.id.btnSubmit);
                tvExit = (TextView) findViewById(R.id.tvReg_Exit);
                tvCancel = (TextView) findViewById(R.id.tvReg_Cancel);


            }


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strSecurityQuestion = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
}

