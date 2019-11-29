package com.example.capstone0.BottomNavigationThings.MenShoes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capstone0.BottomNavigationThings.Profile.A_MyOrders;
import com.example.capstone0.D_CurrentUser;
import com.example.capstone0.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CompleteViewOfProduct extends AppCompatActivity implements View.OnClickListener {
    private static final String CHANNEL_1_ID ="1" ;
    // todo use imageswitcher
    ImageView ProductImage,Bookmark;
    TextView Price,ProductTitle,ProductDescription,QuantityCounter,SizeLabel;
    Button AddToCart;
    ImageButton Quantity_Increment,Quantity_Decrement;
    RadioGroup radioGroup;
   RelativeLayout relativeLayout;
   String D_ProductTitle,D_ProductCategoryByGender,D_ProductCategoryByMaterial,D_Price,D_ProductDescription,D_Image;
   int D_Quantity=1,D_Size=0,ActualPrice=1200;
   Toolbar toolbar;
   boolean isBookMarked=false;
   String CurrentBookMarkedId;
    String payeeAddress = "9866772522@ybl";
    String payeeName = "First Step Corporation";
    String transactionNote = "FirstStep :";
    String amount = "11";
    String currencyUnit = "INR";
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    String D_Date=formatter.format(date);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_view_of_product);
        findViewByIds();
        gettingDataFromIntent();
        settingDataIntoViews();
        createNotifications();
    }
    private void findViewByIds()
    {
        relativeLayout=findViewById(R.id.relativeLayout_CompleteProduct);
        ProductImage=findViewById(R.id.ImageOfProduct_Custom);
        Price=findViewById(R.id.Price);
        ProductTitle=findViewById(R.id.NameOfProduct);
        ProductDescription=findViewById(R.id.ProductDescription);
        QuantityCounter=findViewById(R.id.Quantity_Counter);
        Quantity_Increment=findViewById(R.id.Quantity_increment);
        Quantity_Decrement=findViewById(R.id.Quantity_decrement);
        Bookmark=findViewById(R.id.Bookmark_Image);
        AddToCart=findViewById(R.id.AddToCart);
        toolbar=findViewById(R.id.Toolbar_complete_view_Of_Product);
        radioGroup=findViewById(R.id.Sizes);
        SizeLabel=findViewById(R.id.SizeLabel);
        AddToCart.setOnClickListener(this);
        Bookmark.setOnClickListener(this);
        Quantity_Increment.setOnClickListener(this);
        Quantity_Decrement.setOnClickListener(this);
        QuantityCounter.setText("1");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=group.findViewById(checkedId);
                if (radioButton!=null)
                    D_Size=Integer.parseInt(radioButton.getText().toString());
            }
        });
    }
    private void gettingDataFromIntent()
    {
       Intent intent=getIntent();
      D_ProductCategoryByGender=intent.getStringExtra("ProductCategoryByGender");
      D_ProductCategoryByMaterial=intent.getStringExtra("ProductCategoryByMaterial");
      D_ProductTitle=intent.getStringExtra("ProductTitle");
      D_Price=intent.getStringExtra("ProductPrice");
      D_ProductDescription=intent.getStringExtra("ProductDescription");
      D_Image=intent.getStringExtra("ImageLocation");
      D_Quantity=Integer.parseInt(intent.getExtras().getString("Quantity","1"));
      D_Size=Integer.parseInt(intent.getExtras().getString("SizeOfProduct","0"));
    }
   private void settingDataIntoViews()
   {
     Glide.with(getApplicationContext()).load(D_Image).into(ProductImage);
     ProductTitle.setText(D_ProductTitle);
     ProductDescription.setText(D_ProductDescription);
     QuantityCounter.setText(""+D_Quantity);
     Price.setText(D_Price);
   }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.AddToCart)
        {
         AddToCart();
        }
        else if (v.getId()==R.id.Quantity_increment)
        {
           setQuantity_Increment();
        }
        else if (v.getId()==R.id.Quantity_decrement)
        {
            setQuantity_Decrement();
        }
        else if (v.getId()==R.id.Bookmark_Image)
        {
           if (isBookMarked)
           {
            setBookmark(null);
            isBookMarked=false;
           }
           else
           {
            D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders=new D_PreviousOrdersAndPresentInCartOrders(D_ProductCategoryByGender,D_ProductCategoryByMaterial,D_Image,D_Price,null,null,D_ProductTitle,D_ProductDescription,null,null,null);
            setBookmark(d_previousOrdersAndPresentInCartOrders);
            isBookMarked=true;
           }
        }
        else if (v.getId()==R.id.BuyNow)
        {
         BuyNowPage();
        }
        else if (v.getId()==R.id.Share)
        {
            Intent shareintent=new Intent(Intent.ACTION_SEND);
            shareintent.setType("text/plain");
            shareintent.putExtra(Intent.EXTRA_TEXT,"Check Out this Awesome Product "+D_ProductTitle+" In FirstStep");
            shareintent.putExtra(Intent.EXTRA_SUBJECT,"Download this  app");
            startActivity(Intent.createChooser(shareintent,"choose one among this to share"));
        }
    }

    private void setQuantity_Increment() {
        int n1 = Integer.parseInt(QuantityCounter.getText().toString());
        if (n1 >= 3) {
            QuantityCounter.setText("3");
            D_Quantity =3;
        } else {
            n1++;
            QuantityCounter.setText(""+n1);
            D_Quantity=n1;
        }
    }
    private void setQuantity_Decrement() {
        int n2 = Integer.parseInt(QuantityCounter.getText().toString());
        if (n2>1) {
            n2--;
            QuantityCounter.setText(""+n2);
            D_Quantity=1;
        } else {
            QuantityCounter.setText("1");
            D_Quantity=1;
        }
    }

    private boolean checkSizeChecked()
    {
       RadioButton radioButton= (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
       if (radioButton==null)
           return false;
       else
           return true;
    }
    private void PaymentViaPaytm()
    {
        if (checkSizeChecked())
        {
            ActualPrice=D_Quantity*Integer.parseInt(D_Price);
            Intent paytmIntent=new Intent();
            Bundle bundle=new Bundle();
            bundle.putDouble("nativeSdkForMerchantAmount", ActualPrice);
            bundle.putString("orderid", "1111");
            bundle.putString("txnToken", "123");
            bundle.putString("mid", "");
            paytmIntent.setComponent(new ComponentName("net.one97.paytm", "net.one97.paytm.AJRJarvisSplash"));
            paytmIntent.putExtra("paymentmode", 2); // You must have to pass hard coded 2 here, Else your transaction would not proceed.
            paytmIntent.putExtra("bill", bundle);
            startActivityForResult(paytmIntent,1);
        }
        else
            Toast.makeText(this, "please select Size!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            if (resultCode==0)
            {
                Toast.makeText(getApplicationContext(),"Payment Failed",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            else if (resultCode==1)
            {
                Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode==2)
        {
            Log.d("CompleteViewOfProduct", "onActivityResult: requestCode: " + requestCode);
            Log.d("CompleteViewOfProduct", "onActivityResult: resultCode: " + resultCode);
            //txnId=UPI20b6226edaef4c139ed7cc38710095a3&responseCode=00&ApprovalRefNo=null&Status=SUCCESS&txnRef=undefined
            //txnId=UPI608f070ee644467aa78d1ccf5c9ce39b&responseCode=ZM&ApprovalRefNo=null&Status=FAILURE&txnRef=undefined

            if (data != null) {
                Log.d("CompleteViewOfProduct", "onActivityResult: data: " + data.getStringExtra("response"));
                String res = data.getStringExtra("response");
                String search = "SUCCESS";
                if (res.toLowerCase().contains(search.toLowerCase())) {
                    Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                    Log.e("CompleteViewOfProduct","Payment Successful");
                    TriggerNotifications();
                } else {
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    Log.e("CompleteViewOfProduct","Payment failed");

                }

                snackbarHelp();
            }
        }
    }

    private void snackbarHelp()
    {
        Snackbar.make(relativeLayout,"Need Help! Please feel free to",Snackbar.LENGTH_LONG).setAction("Contact Us!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
                    String number="+919866772522";
                    Intent intent4=new Intent(Intent.ACTION_CALL);
                    intent4.setData(Uri.parse("tel:"+number));
                    startActivity(intent4);
                }
            }
        }).show();
    }
    private void AddToCart() {
        if (D_Size != 0) {
            D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders = new D_PreviousOrdersAndPresentInCartOrders(D_ProductCategoryByGender, D_ProductCategoryByMaterial,D_ProductTitle,D_Price,D_Image,D_ProductDescription,""+D_Quantity,""+D_Size);
            new AsyncTaskToStoreAddToCart().execute(d_previousOrdersAndPresentInCartOrders);
        }
        else
        {
            SizeLabel.setTextColor(Color.RED);
            Snackbar.make(relativeLayout, "Please Select Size", Snackbar.LENGTH_SHORT).show();
        }
    }
   class AsyncTaskToStoreAddToCart extends AsyncTask<D_PreviousOrdersAndPresentInCartOrders,Void,Void>
   {
       @Override
       protected Void doInBackground(D_PreviousOrdersAndPresentInCartOrders... d_previousOrdersAndPresentInCartOrders) {
           D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders1=d_previousOrdersAndPresentInCartOrders[0];
           DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
           databaseReference.child("MyCart").push().setValue(d_previousOrdersAndPresentInCartOrders1).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                       Log.e("CompleteViewOfProduct","Order stored in database");
                   else
                       Log.e("CompleteViewOfProduct","Unable to store details of purchased order");
               }
           });
           return null;
       }
   }

  private void BuyNowPage() {
      if (D_Size != 0)
      {
          int p=Integer.parseInt(D_Price);
          amount="10";
          Uri uri = Uri.parse("upi://pay?pa="+payeeAddress+"&pn="+payeeName+"&tn="+transactionNote+
                  "&am="+amount+"&cu="+currencyUnit);
          Log.d("CompleteViewOfProduct", "onClick: uri: "+uri);
          Intent intent = new Intent(Intent.ACTION_VIEW, uri);
          startActivityForResult(intent,2);

      }
      else {
          SizeLabel.setTextColor(Color.RED);
          Snackbar.make(relativeLayout, "Please Select Size", Snackbar.LENGTH_SHORT).show();
      }

  }

  private void setBookmark(D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders)
  {
      DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyWishList");
     if (isBookMarked && !TextUtils.isEmpty(CurrentBookMarkedId))
     {
       databaseReference.child(CurrentBookMarkedId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful())
                  Bookmark.setImageResource(R.drawable.ic_wishlist);
               else
                   Bookmark.setImageResource(R.drawable.ic_wishlist_full);
           }
       });
     }
     else
     {
         CurrentBookMarkedId=databaseReference.push().getKey();
         databaseReference.child(CurrentBookMarkedId).setValue(d_previousOrdersAndPresentInCartOrders).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 if (task.isSuccessful())
                     Bookmark.setImageResource(R.drawable.ic_wishlist_full);
                 else
                     Bookmark.setImageResource(R.drawable.ic_wishlist);
             }
         });
     }
  }

    public void TriggerNotifications() {
        Intent intent=new Intent(this, A_MyOrders.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_1_ID)
                .setContentTitle("Payment")
                .setContentText("You have purchased "+D_ProductTitle+" You will soon receive this Product")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Payment is Success"))
                .setChannelId(CHANNEL_1_ID)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_payment_success);
        NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(0,builder.build());
    }

    public void createNotifications()
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel(CHANNEL_1_ID,"Channel1Booking",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Your Order has been Confirmed Successfully");
            channel.setShowBadge(true);
            NotificationManager manager=(NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    class AsyncTaskToStoreBuyedProduct extends AsyncTask<D_PreviousOrdersAndPresentInCartOrders,Void,Void>
    {
        @Override
        protected Void doInBackground(D_PreviousOrdersAndPresentInCartOrders... d_previousOrdersAndPresentInCartOrders) {
            D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders1=d_previousOrdersAndPresentInCartOrders[0];
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyOrders");
            databaseReference.push().setValue(d_previousOrdersAndPresentInCartOrders1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        Snackbar.make(relativeLayout,"We will delivery Your Product Soon",Snackbar.LENGTH_SHORT).show();
                }
            });

            return null;
        }
    }

}
