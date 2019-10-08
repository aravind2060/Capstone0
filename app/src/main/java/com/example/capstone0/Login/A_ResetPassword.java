package com.example.capstone0.Login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone0.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class A_ResetPassword extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;

    TextInputLayout ResetPassword1;
    TextInputEditText ResetPassword;

    Button check;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

         firebaseAuth=FirebaseAuth.getInstance();
         check=findViewById(R.id.Reset_Password_Btn_Check_3);
         ResetPassword1=findViewById(R.id.Reset_Password_Txt_Layout_1);
         ResetPassword=findViewById(R.id.Reset_Password_EditText_Email);

         check.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.Reset_Password_Btn_Check_3)
        {
                if (checkEmail(ResetPassword.getEditableText().toString()))
                {
                   firebaseAuth.fetchSignInMethodsForEmail(ResetPassword.getEditableText().toString())
                           .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                               @Override
                               public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                   if (task.isSuccessful())
                                   {
                                        sendResetPasswordLink();
                                   }
                                   else
                                   {
                                       Toast.makeText(getApplicationContext(),"seem's like you don't have account"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                   }
                               }
                           });

                }

        }
    }

    public boolean checkEmail(String Data)
    {
        if (TextUtils.isEmpty(Data)) {
           ResetPassword1.setError("Email cannot be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Data).matches())
        {
            ResetPassword1.setError("Enter Mail properly!!");
            return false;
        }
        else
        {
            return true;
        }
    }

    public void sendResetPasswordLink()
    {
        firebaseAuth.sendPasswordResetEmail(ResetPassword.getEditableText().toString()).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Reset Linked has been sent to mail",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"unable to send reset link"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }
}
