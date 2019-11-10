package com.example.capstone0.BottomNavigationThings.CustomShoes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone0.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CustomDesignBuyNowPage extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    Toolbar toolbar;
    TextView Name,Description,price,Quantity;
    Button size_7,size_8,size_9,size_10,AddToCartCustom,BuyNowCustom;
    ImageButton QuantityIncrement,QuantityDecrement;
    private int ActualPrice;
    private int D_Size;
    private int D_Quantity=1;
    private String D_Price="10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_design_buy_now_page);
        toolbar = findViewById(R.id.Toolbar_BuyNow_Custom);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

     findViewByIds();
     setData();
    }
    private void findViewByIds()
    {
      imageView=findViewById(R.id.ImageOfProductCustom);
      Name=findViewById(R.id.NameOfProductCustom);
      Description=findViewById(R.id.ProductDescriptionCustom);
      size_7=findViewById(R.id.Size_7_Custom);
      size_8=findViewById(R.id.Size_8_Custom);
      size_9=findViewById(R.id.Size_9_Custom);
      size_10=findViewById(R.id.Size_10_Custom);
      AddToCartCustom=findViewById(R.id.AddToCart_Custom);
      BuyNowCustom=findViewById(R.id.BuyNow_Custom);
      QuantityIncrement=findViewById(R.id.Quantity_increment_Custom);
      QuantityDecrement=findViewById(R.id.Quantity_decrement_Custom);
      price=findViewById(R.id.PriceLabel_Name_Custom);
      Quantity=findViewById(R.id.Quantity_Counter_Custom);

      BuyNowCustom.setOnClickListener(this);
      AddToCartCustom.setOnClickListener(this);
      size_7.setOnClickListener(this);
      size_8.setOnClickListener(this);
      size_9.setOnClickListener(this);
      size_10.setOnClickListener(this);
      QuantityIncrement.setOnClickListener(this);
      QuantityDecrement.setOnClickListener(this);
    }


    private void setData()
    {
        Intent intent=getIntent();
//       Bitmap bitmap=intent.getParcelableExtra("BitMapImage");
//       imageView.setImageBitmap(bitmap);
       Name.setText("First Step Carbon");
       price.setText("2200");
       Description.setText("First Step is known for quality and great assurance after buying");
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.BuyNow_Custom)
        {
            PaymentViaPaytm();
        }
        else if (v.getId()==R.id.Quantity_increment)
        {
            setQuantity_Increment();
        }
        else if (v.getId()==R.id.Quantity_decrement)
        {
            setQuantity_Decrement();
        }
        else if (v.getId()==R.id.Size_7)
        {
            //todo Change color of button
            D_Size=7;
        }
        else if (v.getId()==R.id.Size_8)
        {
            D_Size=8;
        }
        else if (v.getId()==R.id.Size_9)
        {
            D_Size=9;
        }
        else if (v.getId()==R.id.Size_10)
        {
            D_Size=10;
        }
    }

    private void setQuantity_Increment() {
        int n1 = Integer.parseInt(Quantity.getText().toString());
        if (n1 >= 3) {
            Quantity.setText("3");
            D_Quantity =3;
            QuantityIncrement.setEnabled(false);
        } else {
            Quantity.setText("" + n1);
            D_Quantity=n1;
        }
    }
    private void setQuantity_Decrement() {
        int n2 = Integer.parseInt(Quantity.getText().toString());
        if (n2 <= 0) {
            Quantity.setText("1");
            Quantity.setEnabled(false);
            D_Quantity=1;
        } else {
            Quantity.setText("" + n2);
            D_Quantity=n2;
        }
    }

    private void PaymentViaPaytm()
    {
        //todo generate random numbers
        ActualPrice=D_Quantity*1200;
        Intent paytmIntent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putDouble("nativeSdkForMerchantAmount", ActualPrice);
        bundle.putString("orderid", "1111");
        bundle.putString("txnToken", "123");
        bundle.putString("mid", "5s#kTvx5kPZBx6Si");

        paytmIntent.setComponent(new ComponentName("net.one97.paytm", "net.one97.paytm.AJRJarvisSplash"));
        paytmIntent.putExtra("paymentmode", 2); // You must have to pass hard coded 2 here, Else your transaction would not proceed.
        paytmIntent.putExtra("bill", bundle);
        startActivityForResult(paytmIntent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            if (resultCode==0)
            {
                Toast.makeText(getApplicationContext(),"Payment Failed ",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            else if (resultCode==1)
            {
                Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show();
               // storePurchaseDetailsOfCustomShoeInFireBase();
            }
        }
    }



}
