package com.example.mobihealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //making link with buttons
        final Button login_button = (Button) findViewById(R.id.button);
        final Button register_button = (Button) findViewById(R.id.register1);

        //starting new activites when button is clicked
        //login activities


        //Register activities
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });
    }
    public void openRegisterActivity() {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);

    }
}