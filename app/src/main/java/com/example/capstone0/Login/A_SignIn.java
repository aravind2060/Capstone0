package com.example.capstone0.Login;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone0.MainActivity;
import com.example.capstone0.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;


import java.util.regex.Pattern;


public class A_SignIn extends AppCompatActivity implements View.OnClickListener {

    Button SignIn;
    TextInputEditText Email,Password;
    TextInputLayout Email1,Password1;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

     SignIn=findViewById(R.id.sign_in_btn_SignIn);
     Email=findViewById(R.id.sign_in_txt_edit_txt_email);
     Password=findViewById(R.id.sign_in_txt_edit_txt_password);
    Email1=findViewById(R.id.sign_in_txt_layout_1);
    Password1=findViewById(R.id.sign_in_txt_layout_2);

    firebaseAuth=FirebaseAuth.getInstance();

     SignIn.setOnClickListener(this);

    }

    //todo use shared preferences here
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if (currentUser!=null && currentUser.isEmailVerified())
        {
           startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }

    @Override
    public void onClick(View v)
    {

      if(v.getId()==R.id.sign_in_btn_SignIn)
      {
          String EmailData=Email.getEditableText().toString(),PasswordData=Password.getEditableText().toString();
         if (  (chkEmailOffline(EmailData)) && (chkPasswordOffline(PasswordData)) )
         {
             firebaseAuth.signInWithEmailAndPassword(EmailData,PasswordData).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful())
                      {
                          if (firebaseAuth.getCurrentUser().isEmailVerified())
                          {
                              Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                              //todo save in shared preference
                              startActivity(new Intent(getApplicationContext(),MainActivity.class));
                          }
                          else
                          {
                              Toast.makeText(getApplicationContext(),"You have registered please verify your mail!!",Toast.LENGTH_LONG).show();
                          }

                      }
                      else
                      {
                          Toast.makeText(getApplicationContext(),"Unable to Login "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                      }

                 }
             });



         }
      }
      else if (v.getId()==R.id.Sign_In_txt_SignUP_Link)
      {
          startActivity(new Intent(getApplicationContext(), A_SignUp.class));
      }
      else if (v.getId()==R.id.Sign_In_txt_Reset_Password_Link)
      {
        startActivity(new Intent(getApplicationContext(), A_ResetPassword.class));
      }

    }

    private boolean chkEmailOffline(String email)
    {
        if(TextUtils.isEmpty(email))
        {
           Email1.setError("Email Cannot be Empty!");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email1.setError("Invalid Email!!");
            return false;
        }
        else
        {
            final boolean[] num = new boolean[]{true};
            firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(this, new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if(task.getResult().getSignInMethods().isEmpty())
                    {
                        Email1.setError("Email doesn't exist!!");
                        num[0] =false;
                    }
                }
            });
            return num[0];
        }
    }





   private boolean chkPasswordOffline(String Passwordz)
   {
      if (TextUtils.isEmpty(Passwordz))
      {
          Password1.setError("Password Cannot be empty!");
          return false;
      }
      else if (!PASSWORD_PATTERN.matcher(Passwordz).matches())
      {
         Password1.setError("Password is too weak!");
          return false;
      }
      else
         return true;
   }



}
