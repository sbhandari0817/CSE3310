package com.example.mobihealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Registration extends AppCompatActivity {
    private EditText email, password, word;
    private Button register;
    private FirebaseAuth firebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        Context context = getApplicationContext();
        firebaseauth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    String Email = email.getText().toString().trim();
                    String Password = password.getText().toString().trim();

                    firebaseauth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Registration.this, MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void setupUIViews(){
        email = (EditText)findViewById(R.id.username1);
        password = (EditText)findViewById(R.id.password1);
        word = (EditText)findViewById(R.id.password2);
        register= (Button)findViewById(R.id.register1);

    }
    //Function to validate the usename and password
    private Boolean validate(){
        Boolean result = true;
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Word = word.getText().toString();
        if (Email.isEmpty()){
            Toast toast = Toast.makeText(this, "Email field can't be empty", Toast.LENGTH_SHORT);
            toast.show();
            result = false;
        }
        if (!(Password.matches(Word))){
            Toast toast = Toast.makeText(this, "Password didn't matched", Toast.LENGTH_SHORT);
            toast.show();
            result = false;
        }
        return result;

    }
}