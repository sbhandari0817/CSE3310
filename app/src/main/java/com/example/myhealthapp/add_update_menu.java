package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class add_update_menu extends AppCompatActivity {

    private Button btn_Vital_add, btn_Vital_Update, btn_Vital_View,btnMenu;
    private EditText vital_name, vital_data;
    String Current_userId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_menu);


        btn_Vital_add = (Button)findViewById(R.id.btnAdd_Vital);
        btn_Vital_Update = (Button)findViewById(R.id.btnUpdateVital);
        btn_Vital_View = (Button)findViewById(R.id.btnViewVital);
        btnMenu = (Button)findViewById(R.id.btnMenuVital);
        Current_userId= (Prevalent.currentOnlineUser.getUserId());



        btn_Vital_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogAddVital();

            }
        });
        btn_Vital_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(add_update_menu.this,displayData.class);
                intent.putExtra("Category","Edit");
               startActivity(intent);
            }
        });
        btn_Vital_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_update_menu.this,displayData.class);
                intent.putExtra("Category","View");
                startActivity(intent);
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_update_menu.this,Home.class);
                startActivity(intent);
            }
        });
    }

    public void dialogAddVital(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(add_update_menu.this);
        View view  = LayoutInflater.from(add_update_menu.this).inflate(R.layout.add_vital_dialog, null);
        builder.setView(view);
        vital_data = view.findViewById(R.id.edVital_data);
        vital_name = view.findViewById(R.id.edVital_Name);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String data = vital_data.getEditableText().toString().trim();
                String name = vital_name.getEditableText().toString().trim();

                if(data.isEmpty()){
                    Toast.makeText(add_update_menu.this,"Please Enter data", Toast.LENGTH_SHORT).show();
                }
                else if (name.isEmpty()){
                    Toast.makeText(add_update_menu.this,"Please Enter Vital sign name ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(add_update_menu.this,"Thanks", Toast.LENGTH_SHORT).show();
                    add(name,data);

                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel                                            ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void add(final String name, final String data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference addVital = database.getReference();

        addVital.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("User").child(Current_userId).child("Vital Signs").child(name).exists()){
                    HashMap<String,Object> addVitalSign = new HashMap<>();
                    addVitalSign.put("Name", name);
                    addVitalSign.put("Data",data);

                    addVital.child("User").child(Current_userId).child("Vital Signs").child(name).updateChildren(addVitalSign).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(add_update_menu.this, "Added", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(add_update_menu.this, "Failed to Add", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(add_update_menu.this, "There is an Vital sign added already with this name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}