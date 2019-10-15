package com.example.capstone0.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.capstone0.BroadcastReceiver.InternetConnection;
import com.example.capstone0.MainActivity;
import com.example.capstone0.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.regex.Pattern;

import static com.example.capstone0.BroadcastReceiver.InternetConnection.IS_NETWORK_AVAILABLE;


public class A_SignIn extends AppCompatActivity implements View.OnClickListener {

    Button SignIn;
    TextInputEditText Email,Password;
    TextInputLayout Email1,Password1;
    SignInButton GoogleSignInBtn;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");

    public static final int RC_SIGN_IN=9001;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

     SignIn=findViewById(R.id.sign_in_btn_SignIn);
     Email=findViewById(R.id.sign_in_txt_edit_txt_email);
     Password=findViewById(R.id.sign_in_txt_edit_txt_password);
    Email1=findViewById(R.id.sign_in_txt_layout_1);
    Password1=findViewById(R.id.sign_in_txt_layout_2);

    GoogleSignInBtn=findViewById(R.id.Sign_In_Btn_Google_SignIn_4);
    GoogleSignInBtn.setOnClickListener(this);

    firebaseAuth=FirebaseAuth.getInstance();

     SignIn.setOnClickListener(this);
     configureGoogleSignIn();
    }
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
           checkEmailAndPasswordInFireabseDatabase(EmailData,PasswordData);
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
      else if (v.getId()==R.id.Sign_In_Btn_Google_SignIn_4)
      {
        SetGoogleSignIn();
      }
    }

    /*
      @configureGoogleSignIn
      is to configure googlesigin
      @web_client_id is generated automatically i.e it is downloaded when you turn on googlesignin option in firebase
     */
    private void configureGoogleSignIn()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

   /*
     @SetGoogleSignIn
     ask for to signin via google
    */

    private void SetGoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
                Toast.makeText(getApplicationContext(),"Google Signin Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
       @firebaseAuthWithGoogle
       SignIn Via Google
       and pass the reference to @storeGoogleSigInedUsersData
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(),"Google signin Success",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            storeGoogleSigInedUsersData(user);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Signed in failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*
      @storeGoogleSiginedUsersData
      Who ever signed in using google store there information in database
     */
    private void storeGoogleSigInedUsersData(final FirebaseUser firebaseUser)
    {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            //String personId = acct.getId(); //google account's unique id created by google
            //Uri personPhoto = acct.getPhotoUrl();

           String Email=personEmail;
           String Phone=null;
           String Gender=null;
           String Name;
           if (TextUtils.isEmpty(personGivenName))
           {
               if (TextUtils.isEmpty(personFamilyName))
               {
                   if (TextUtils.isEmpty(personName))
                       Name=null;
                   else
                       Name=personName;
               }
               else
                   Name=personFamilyName;
           }
           else
               Name=personGivenName;
           final D_UserDataToStoreInFirebase d_userDataToStoreInFirebase=new D_UserDataToStoreInFirebase(Name,Email,Phone,Gender);
          DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
          databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if (!dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid()))
                  {
                      FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid())
                              .setValue(d_userDataToStoreInFirebase).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                                 if (task.isSuccessful())
                                 {
                                     Toast.makeText(getApplicationContext(),"Your data stored successfully",Toast.LENGTH_LONG).show();
                                 }
                          }
                      });
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });

        }

    }

    /*
      @checkEmailAndPasswordInFireabseDatabase
       is to check email and password from firebase database

     */
    public void checkEmailAndPasswordInFireabseDatabase(String EmailData, String PasswordData)
    {
      firebaseAuth.signInWithEmailAndPassword(EmailData,PasswordData).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful())
               {
                  if (firebaseAuth.getCurrentUser().isEmailVerified())
                  {
                      Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                      startActivity(new Intent(getApplicationContext(),MainActivity.class));
                  }
                  else
                  {
                      Toast.makeText(getApplicationContext(),"Email Not verified please Verify Your Email",Toast.LENGTH_LONG).show();
                      Log.i("A_SIGIN","Email Not verified");
                  }
               }
               else
               {
                   try {
                       throw task.getException();
                   }
                   catch (Exception e)
                   {
                       if (e instanceof FirebaseAuthInvalidCredentialsException)
                       {
                           Toast.makeText(getApplicationContext(),"Invalid Credentials ",Toast.LENGTH_LONG).show();
                           Log.e("A_SIGNIN","Invalid credentials");
                       }
                       else if (e instanceof FirebaseAuthInvalidUserException)
                       {
                           Toast.makeText(getApplicationContext(),"Account does not exist",Toast.LENGTH_LONG).show();
                       }
                       else if (e instanceof FirebaseNetworkException) {
                           Toast.makeText(getApplicationContext(), "Unable to reach firebase", Toast.LENGTH_LONG).show();
                       }
                       else if (e instanceof FirebaseAuthProvider)
                       {
                           Toast.makeText(getApplicationContext(),"Login Via GoogleSignIN",Toast.LENGTH_LONG).show();
                       }
                   }
               }
          }
      });
    }


    /*
      @chkEmailOffline
       is to check email offline
     */
    private boolean chkEmailOffline(final String email)
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
          return true;
        }
    }


/*
    @chkPasswordOffline
   is to validate password i.e whether empty or it followed standard
 */
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
