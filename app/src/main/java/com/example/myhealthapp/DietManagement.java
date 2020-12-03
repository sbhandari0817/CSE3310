package com.example.myhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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

public class DietManagement extends AppCompatActivity {



    private EditText edBreakfast_calorie, edLunch_calorie, edDinner_calorie,ed_Weight;

    String b_cal, l_cal, d_cal, set_message, set_total_calorie;
    private TextView setMessage, setTotal_Calorie, tvMessage,edBMI_value;

    private Button btn_done,btn_BMI;
    FirebaseDatabase database;
    DatabaseReference reference;
    Double Weight;
    String CurrentUserID;
    int break_cal, lunch_cal, dinner_cal, total_cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_management);
        ed_Weight = (EditText)findViewById(R.id.ed_weight);
        edBreakfast_calorie = (EditText)findViewById(R.id.edBreakfast_cal);
        edLunch_calorie = (EditText)findViewById(R.id.edLunch_cal);
        edDinner_calorie = (EditText)findViewById(R.id.edDinner_cal);
        setMessage = (TextView)findViewById(R.id.tvSetMessage);
        btn_BMI = (Button)findViewById(R.id.btn_BMI);

        CurrentUserID = (Prevalent.currentOnlineUser.getUserId());
        setTotal_Calorie = (TextView)findViewById(R.id.tvTotal_DataCalorie);
        btn_done = (Button)findViewById(R.id.btn_Done_diet);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        set_message = setMessage.getText().toString();

        TextWatcher textWatcher =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if((!edBreakfast_calorie.getText().toString().equals("")
                        &&(!edLunch_calorie.getText().toString().equals(""))
                        && (!edDinner_calorie.getText().toString().equals("")))){
                    int temp1 = Integer.parseInt(edBreakfast_calorie.getText().toString());
                    int temp2 = Integer.parseInt(edLunch_calorie.getText().toString());
                    int temp3  =Integer.parseInt(edDinner_calorie.getText().toString());
                    setTotal_Calorie.setText("Calorie Consumed: " + String.valueOf(temp1+temp2+temp3));
                    int total = temp1+temp2+temp3;
                    if(total>2500){
                        setMessage.setText("You Have Consumed more than 2500 calories today. The " +
                                "recommended daily calorie intake is 2,000 calories a day for women and 2,500 for men.");
                    }
                    else if(total<=1000){
                        setMessage.setText("You Have Consumed less than 1000 calories today.The " +
                                "recommended daily calorie intake is 2,000 calories a day for women and 2,500 for men.");

                    }
                    else if((total> 1000)&&(total<=2000)){
                        setMessage.setText("You Have Consumed "+   total + "calories today.The " +
                                "recommended daily calorie intake is 2,000 calories a day for women and 2,500 for men.");

                    }
                    //if ((total>2000)&&(total<2500))
                    else {
                        setMessage.setText("                    Congratulations!!! \n You Have Consumed "+   total + "calories today maintaining " +
                                "standard calorie intake per today.");
                    }
                   // System.out.println((temp1+temp2+temp3));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        edDinner_calorie.addTextChangedListener(textWatcher);
        edLunch_calorie.addTextChangedListener(textWatcher);
        edBreakfast_calorie.addTextChangedListener(textWatcher);


        btn_BMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(ed_Weight.getText().toString().isEmpty())){
                    Weight = Double.parseDouble(ed_Weight.getText().toString());
                    dialog_bmi_data();

                }
                else{
                    Toast.makeText(DietManagement.this, "Enter your weight in pound to calculate Body Mass Index ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void dialog_bmi_data(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DietManagement.this);
        View view = LayoutInflater.from(DietManagement.this).inflate(R.layout.bmi_value,null);
        builder.setView(view);
        edBMI_value = view.findViewById(R.id.edBmi_data);
        tvMessage = view.findViewById(R.id.tv_Message);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Portal userPortal = snapshot.child("User").child(CurrentUserID).child("User Portal").getValue(Portal.class);
                double height = Double.parseDouble(userPortal.getHeight());
                Double BMI =calculateBMI(Weight,height);
                edBMI_value.setText(""+BMI);


                if(BMI>= 30){
                    tvMessage.setText("         WARNING!!! \n Please consult with your Doctor as soon as possible since your BMI is in OBSESITY level.");
                }
                else if((BMI>=25)&&(BMI<30)){
                    tvMessage.setText("WARNING!!! \n Please consult with your Doctor since your BMI is OVERWEIGHT");
                }
                else if((BMI>=18.5)&&(BMI<24.9)){
                    tvMessage.setText("GREAT!!!\n YOU HAVE NORMAL WEIGHT");
                }
                else{
                    tvMessage.setText("WARNING!!! \n Please consult with your Doctor since your BMI is UNDERWEIGHT");
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        builder.setPositiveButton("DONE                                 ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }) ;


        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    public double calculateBMI(double weight, double height){
       // weight in pounds and height in
        height = height *12;
        double BMI = (703.0*(weight/(height*height)));


        return BMI;
    }








}