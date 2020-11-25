package com.example.mobihealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Login extends AppCompatActivity {
    private EditText password, email;
    private Button login;
    private FirebaseAuth mAuth;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI();
        mAuth = FirebaseAuth.getInstance();
        count = 0;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count < 3){
                    String Email = email.getText().toString().trim();
                    String Password = password.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(Login.this, Mainmenu.class));
                                    }
                                    else{
                                        count++;
                                        Toast.makeText(Login.this, "Sign in Failed Please check \n email or password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }

    void setupUI(){
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.TextPassword);
        login = (Button) findViewById(R.id.button2);
    }

}