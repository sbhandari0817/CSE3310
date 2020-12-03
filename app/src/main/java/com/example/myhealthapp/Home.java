package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    private Button btnPersonal_Info, btnVital, btnMedication, btnDiet, btnMonitoring, btnCommunication,btnSearch, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnPersonal_Info = (Button)findViewById(R.id.btnPer_Info);
        btnVital = (Button)findViewById(R.id.btnVital);
        btnMedication = (Button)findViewById(R.id.btnMedication);
        btnDiet = (Button)findViewById(R.id.btnDiet);
        btnMonitoring = (Button)findViewById(R.id.btnMonitoring);
        btnCommunication = (Button)findViewById(R.id.btnCommunication);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnLogout = (Button)findViewById(R.id.btnLogout);

        // getting button from dialog box






        btnPersonal_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,PersonalInformation.class);
                startActivity(intent);

            }
        });
        btnVital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,add_update_menu.class);
                startActivity(intent);
            }
        });
        btnMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAsk();
            }
        });

        btnDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,DietManagement.class);
                startActivity(intent);
            }
        });



       btnLogout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Home.this, MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                       Intent.FLAG_ACTIVITY_CLEAR_TASK |
                       Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
               finish();

           }
       });

    }

    private void dialogAsk() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("WARNING!!!");
        builder.setMessage("Are you currently taking any new medicine?");


      builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
              Intent intent = new Intent(Home.this,MedicationHome.class);
              startActivity(intent);

          }
      });
        builder.setNegativeButton("No                           ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });



        AlertDialog alertDialog = builder.create();
        alertDialog.show();



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
                Intent intent = new Intent(Home.this, MainActivity.class);
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