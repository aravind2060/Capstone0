package com.example.capstone0.BottomNavigationThings.MenShoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.capstone0.Login.A_SignIn;
import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class A_BuyNow extends AppCompatActivity {

   static ArrayList<D_PreviousOrdersAndPresentInCartOrders> dPreviousOrdersAndPresentInCartOrders=new ArrayList<>();
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent intent=new Intent(getApplicationContext(),A_SignIn.class);
            intent.putExtra("Number",1);
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__buy_now);

    }



}
