package com.example.capstone0.Login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone0.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    FirebaseAuth firebaseAuth;

    TextInputLayout Name1,Email1,Phone1,Password1,ConfirmPassword1;
    TextInputEditText Name,Email,Phone,Password,ConfirmPassword;
    Spinner spinner;
    Button SignUp;
    String Gender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth=FirebaseAuth.getInstance();

        Name1=findViewById(R.id.Sign_UP_Txt_Layout_1);
        Email1=findViewById(R.id.Sign_UP_Txt_Layout_2);
        Phone1=findViewById(R.id.Sign_UP_Txt_Layout_3);
        Password1=findViewById(R.id.Sign_UP_Txt_Layout_4);
        ConfirmPassword1=findViewById(R.id.Sign_UP_Txt_Layout_5);

        Name=findViewById(R.id.Sign_Up_Txt_EditTxt_Name);
        Email=findViewById(R.id.Sign_Up_Txt_EditTxt_Email);
        Phone=findViewById(R.id.Sign_Up_Txt_EditTxt_Phone);
        Password=findViewById(R.id.Sign_Up_Txt_EditTxt_Password);
        ConfirmPassword=findViewById(R.id.Sign_Up_Txt_EditTxt_Confirm_Password);

        spinner=findViewById(R.id.Sign_Up_Spinner_Gender);
        SignUp=findViewById(R.id.Sign_up_Btn_SignUp_7);
        setSpinner();
        SignUp.setOnClickListener(this);

    }

    public void setSpinner()
    {
        ArrayAdapter<CharSequence>arrayAdapter=ArrayAdapter.createFromResource(this,R.array.Gender_List,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Gender=parent.getItemAtPosition(position).toString();
            }
        });
    }

    @Override
    public void onClick(View v){
        if (v.getId()==R.id.Sign_up_Btn_SignUp_7)
        {

        }
    }

    private boolean checkSpinner(String Data)
    {
        if (TextUtils.isEmpty(Data))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean checkName(String Data)
    {
        if (TextUtils.isEmpty(Data))
            return false;
        else if(Data)
        {

        }
    }
}
