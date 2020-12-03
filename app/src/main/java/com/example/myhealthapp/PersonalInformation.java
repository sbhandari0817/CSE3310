package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalInformation extends AppCompatActivity {


    private Button btnAddInfo, btnUpdateInfo, btnViewInfo, btnMainmenu;
    private FirebaseDatabase database;
    DatabaseReference reference;
    String currentUserId;
    boolean CheckIfNewUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        database  = FirebaseDatabase.getInstance();
        reference = database.getReference();

        currentUserId = Prevalent.currentOnlineUser.getUserId();
        btnAddInfo = (Button)findViewById(R.id.btnAdd_Info);
        btnViewInfo = (Button)findViewById(R.id.btnView);
        btnUpdateInfo = (Button)findViewById(R.id.btnUpdate);
        btnMainmenu= (Button)findViewById(R.id.btnMainmenu);
        checkIfNewUser();

        if(!CheckIfNewUser){
            btnAddInfo.setVisibility(View.INVISIBLE);

        }
        else{
            btnUpdateInfo.setVisibility(View.INVISIBLE);
            btnViewInfo.setVisibility(View.INVISIBLE);
        }



        btnAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PersonalInformation.this,UserPortal.class);
                intent.putExtra("Category","Add");
                startActivity(intent);
            }
        });

        btnViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInformation.this,UserPortal.class);
                intent.putExtra("Category","View");
                startActivity(intent);
            }
        });
        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInformation.this,UserPortal.class);
                intent.putExtra("Category","Update");
                startActivity(intent);
            }
        });
        btnMainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInformation.this,Home.class);
                startActivity(intent);
            }
        });
    }

    public void checkIfNewUser(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("User").child(currentUserId).child("User Portal").exists()) {
                    CheckIfNewUser = false;
                }
                else {
                    CheckIfNewUser = true;
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
                Intent intent = new Intent(PersonalInformation.this, MainActivity.class);
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