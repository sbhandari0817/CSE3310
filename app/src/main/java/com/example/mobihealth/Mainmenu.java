package com.example.mobihealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Mainmenu extends AppCompatActivity {
    private ImageView PI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        setupUI();

        PI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Mainmenu.this,Personal_Information.class));
            }
        });
    }

    void setupUI(){
        PI = (ImageView) findViewById(R.id.PIclick);
    }
}