package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    private EditText edUsername, edPassword;
    private Button btnLogin;
    private TextView tvForgetPassword, tvRegisterHere;
    String Username, Password;
    ProgressDialog loadingBar;
    FirebaseDatabase database;
    String temp;

    DatabaseReference myReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        myReference = database.getReference();

        callNotification();



        /*Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,21);
        c.set(Calendar.MINUTE,38);
        c.set(Calendar.SECOND,30);*/
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), broadcast);
        if(!Prevalent.intentValue.equals("intent")){
            Toast.makeText(this, "Thank you", Toast.LENGTH_SHORT).show();

        }

        setInfo();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if Username is empty
                Username = edUsername.getText().toString();
                Password = edPassword.getText().toString();
                if(TextUtils.isEmpty(Username)){
                    Toast.makeText(MainActivity.this, "Please Enter Your Username.", Toast.LENGTH_SHORT).show();
                }
                //Check if Password is empty
                else if(TextUtils.isEmpty(Password)){
                    Toast.makeText(MainActivity.this, "Please Enter Your Password.", Toast.LENGTH_SHORT).show();
                }
                //else pass the Username and password to validate login user
                else{
                    loadingBar.setTitle("                       Authorizing     ");
                    loadingBar.setMessage("Authorizing...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    validate(Username,Password);
                }
            }
        });

        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);

            }
        });
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setInfo(){
        edUsername  = (EditText)findViewById(R.id.edLogin_Username);
        edPassword = (EditText)findViewById(R.id.edLogin_Password);
        btnLogin= (Button)findViewById(R.id.btnLogin);
        tvForgetPassword = (TextView)findViewById(R.id.tvForgetPassword);
        tvRegisterHere = (TextView)findViewById(R.id.tvRegisterHere);

        loadingBar = new ProgressDialog(this);
    }

    public void validate(final String Username, final String Password){

        myReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("User").child(Username).child("Login").exists()){
                    User user = snapshot.child("User").child(Username).child("Login").getValue(User.class);
                    if(Username.equals(user.getUserId())&&(Password.equals(user.getPassword()))){
                        loadingBar.dismiss();


            //ALARM MANAGER ACTION TO NOTIFY DOCTOR GO HERE



                       Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,Home.class);
                        loadingBar.dismiss();
                        Prevalent.currentOnlineUser=user;
                        startActivity(intent);
                    }
                    else{
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "Username and Password is incorrect", Toast.LENGTH_SHORT).show();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void callNotification(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,20);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,05);
        //cal.add(Calendar.SECOND, 5);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

    }









}
