package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class displayData extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<String> vitalList;
    ArrayAdapter<String> adapter;
    ListView listView;
    String Current_userId;
    private EditText update_data,update_name;
    String selected_vitalsign;
    String old_data,old_name;
    String Category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        database = FirebaseDatabase.getInstance();

        //reference = database.getReference("User").child("Vital Signs")
        Current_userId= (Prevalent.currentOnlineUser.getUserId());
        listView = (ListView)findViewById(R.id.list);
        Intent intent = getIntent();
        Category= intent.getStringExtra("Category");
        reference = database.getReference().child("User").child(Current_userId).child("Vital Signs");
        vitalList = new ArrayList<>();
        adapter = new ArrayAdapter<>(displayData.this,R.layout.display_data,R.id.tvDataList,vitalList);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    // APPI = item.getValue(AppointmentInformation.class);
                    vitalData data = item.getValue(vitalData.class);
                    vitalList.add(" " + data.getName()+"\n"+data.getData());
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
                    selected_vitalsign = listView.getItemAtPosition(i).toString().trim();
                    dialogEditVitalSign();
                }
            });

    }


    private void dialogEditVitalSign() {
        AlertDialog.Builder builder = new AlertDialog.Builder(displayData.this);
        View view = LayoutInflater.from(displayData.this).inflate(R.layout.add_vital_dialog, null);
        builder.setView(view);

        splitString(selected_vitalsign);
        update_data = view.findViewById(R.id.edVital_data);
        update_name = view.findViewById(R.id.edVital_Name);

        if (Category.equals("View")) {
            update_data.setText(old_data);
            update_name.setText(old_name);
            update_data.setKeyListener(null);
            update_name.setKeyListener(null);
            builder.setPositiveButton("                 Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        } else if(Category.equals("Edit")) {

            update_data.setText(old_data);
            update_name.setText(old_name);
            update_name.setKeyListener(null);

            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    String new_data = update_data.getEditableText().toString().trim();

                    if (new_data.equals(old_data)) {
                        Toast.makeText(displayData.this, "Nothing changed", Toast.LENGTH_SHORT).show();
                    } else {
                        reference.child(old_name).child("Data").setValue(new_data);
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
    }
    private void splitString(String str){

        String[] allStr = str.split("\n");
        old_name= allStr[0];
        old_data = allStr[1];
    }
}