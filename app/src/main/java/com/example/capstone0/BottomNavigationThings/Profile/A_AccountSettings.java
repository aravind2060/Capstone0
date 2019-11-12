package com.example.capstone0.BottomNavigationThings.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capstone0.BottomNavigationThings.MenShoes.D_ShoesDataFromInternet;
import com.example.capstone0.D_CurrentUser;
import com.example.capstone0.Login.A_SignIn;
import com.example.capstone0.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class A_AccountSettings extends AppCompatActivity implements View.OnClickListener {

    Button deleteAccount,upload,getimage;
    FirebaseAuth firebaseAuth;
     FirebaseUser firebaseUser;
     EditText gender,type;
     ImageView imageView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__account_settings);

      deleteAccount=findViewById(R.id.Account_Settings_btn_Delete_Account);
      deleteAccount.setOnClickListener(this);
      gender=findViewById(R.id.gendertype);
      type=findViewById(R.id.type);
      upload=findViewById(R.id.upload);
      upload.setOnClickListener(this);
      imageView=findViewById(R.id.imageview);
      getimage=findViewById(R.id.getimage);
      getimage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/capstone0-51d51.appspot.com/o/WomenFootWear%2FSports%2F-LtQvTV2DBviB5ST7TCM.jpg?alt=media&token=1dfd895b-56c6-4c57-8123-e38836e2f125").into(imageView);

          }
      });

      firebaseAuth=FirebaseAuth.getInstance();
      firebaseUser=firebaseAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.Account_Settings_btn_Delete_Account)
        {
            String uid=firebaseUser.getUid();
            clearAll();
           deleteuserData(uid);
           firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()) {
                       Toast.makeText(getApplicationContext(), "Account deleted successfully", Toast.LENGTH_LONG).show();
                       removeCurrentUser();
                       Intent intent=new Intent(getApplicationContext(),A_SignIn.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                   }
               }
           });
        }
        else if (v.getId()==R.id.upload)
            uploadToFirebase();

    }
  public void clearAll()
  {
      D_CurrentUser.setName("");
      D_CurrentUser.setEmail("");
      D_CurrentUser.setPhone("");
      D_CurrentUser.setGender("");
  }

  public void deleteuserData(String uid)
  {
     DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");

     databaseReference.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
         @Override
         public void onComplete(@NonNull Task<Void> task) {
             if (task.isSuccessful())
                 Toast.makeText(getApplicationContext(),"Your application data deleted successfully",Toast.LENGTH_SHORT).show();
                Log.e("A_Account","Data deleted");
         }
     });
  }
  public void removeCurrentUser()
  {
      firebaseAuth.updateCurrentUser(null);
      Toast.makeText(getApplicationContext(),"Current user set to null",Toast.LENGTH_LONG).show();
      Log.e("A_Account","Current user set to null");
  }

  private void uploadToFirebase()
  {
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(gender.getText().toString()).child(type.getText().toString());
    String refernce=databaseReference.push().getKey();
      Toast.makeText(this, ""+refernce, Toast.LENGTH_SHORT).show();
    D_ShoesDataFromInternet d_shoesDataFromInternet=new D_ShoesDataFromInternet("Formal","1200","First Step provides best shoes",refernce);
    databaseReference.child(refernce).setValue(d_shoesDataFromInternet).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful())
                Toast.makeText(A_AccountSettings.this, "Uploaded bro", Toast.LENGTH_SHORT).show();
        }
    });
  }


}
