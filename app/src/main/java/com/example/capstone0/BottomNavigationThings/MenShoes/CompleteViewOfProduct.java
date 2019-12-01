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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capstone0.BottomNavigationThings.CommonUtils.D_Bookmarkshoe;
import com.example.capstone0.BottomNavigationThings.CommonUtils.D_PastOrders;
import com.example.capstone0.BottomNavigationThings.Profile.A_MyOrders;
import com.example.capstone0.BottomNavigationThings.Profile.D_Address;
import com.example.capstone0.D_CurrentUser;
import com.example.capstone0.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    String payeeAddress = "9491006254@ybl";
    String payeeName = "First Step Corporation";
    String transactionNote = "FirstStep corporation:";
    String amount;
    String currencyUnit = "INR";
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date = new Date();
    String D_Date=formatter.format(date);
    String D_OrderId=D_CurrentUser.getPhone()+""+System.currentTimeMillis();
    Boolean isStored=false;
    ListView listView;
    String D_address_For_Shipping;
    ArrayList<String> addressArrayList=new ArrayList<>();
    ArrayAdapter adapter;
    String ProductLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_view_of_product);
        setListViewForAddress();
        findViewByIds();
        gettingDataFromIntent();
        checkAlreadyBookMarked();
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
        ProductLink=intent.getStringExtra("ProductLink");
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
            D_Bookmarkshoe d_bookmarkshoe=new D_Bookmarkshoe(ProductLink,D_ProductCategoryByGender,D_ProductCategoryByMaterial);
            setBookmark(d_bookmarkshoe);
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
       if (radioButton==null) {
           Snackbar.make(relativeLayout,"Please Choose Dimension's of product",Snackbar.LENGTH_LONG).show();
           return false;
       }else
           return true;
    }
    private boolean checkAddressChecked()
    {
        if (!TextUtils.isEmpty(D_address_For_Shipping))
            return true;
        else
        {
            Snackbar.make(relativeLayout,"Please Choose Address",Snackbar.LENGTH_LONG).show();
            return false;
        }
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
                    D_PastOrders dPastOrders=new D_PastOrders(D_CurrentUser.getName(),D_CurrentUser.getPhone(),D_CurrentUser.getEmail(),D_address_For_Shipping,D_ProductTitle,D_ProductDescription,D_Image,""+D_Quantity,""+D_Size,D_ProductCategoryByGender,D_ProductCategoryByMaterial,amount,D_OrderId,D_Date+"_"+"Pending");
                    new AsyncTaskToStorePurchasedProduct().execute(dPastOrders);
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

    private void setListViewForAddress()
    {

      listView=findViewById(R.id.ListView_Address);
         adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_single_choice,addressArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                D_address_For_Shipping=addressArrayList.get(position);
                view.setBackgroundColor(Color.MAGENTA);
            }
        });
        getDataOfAddress();
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
      if (checkSizeChecked() && checkAddressChecked())
      {
          int p=Integer.parseInt(D_Price)*D_Quantity;
          amount=""+p;
         String fakeamount="1";
          Uri uri = Uri.parse("upi://pay?pa="+payeeAddress+"&pn="+payeeName+"&tn="+transactionNote+
                  "&am="+fakeamount+"&cu="+currencyUnit);
          Log.d("CompleteViewOfProduct", "onClick: uri: "+uri);
          Intent intent = new Intent(Intent.ACTION_VIEW, uri);
          startActivityForResult(intent,2);
      }

  }

  private void setBookmark(D_Bookmarkshoe dBookmarkshoe)
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
         CurrentBookMarkedId=ProductLink;
         databaseReference.child(CurrentBookMarkedId).setValue(dBookmarkshoe).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    class AsyncTaskToStorePurchasedProduct extends AsyncTask<D_PastOrders,Void,Void>
    {

        @Override
        protected Void doInBackground(D_PastOrders... d_pastOrders) {
            D_PastOrders d_pastOrders1=d_pastOrders[0];
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child("UsersNormalShoeOrders");
            final String key=databaseReference.push().getKey();
            databaseReference.child(key).setValue(d_pastOrders1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.e("CompleteViewOfProduct","Purchased order details stored successfully");

                        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference1.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyOrders").push().setValue(key);
                        TriggerNotifications();
                    }
                    else
                    {
                        Log.e("CompleteViewOfProduct","Unable to store purchased order details in firebase");
                        isStored=false;
                    }
                }
            });

            return null;
        }

    }

    private void getDataOfAddress()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Addresses");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                  for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                  {
                      D_Address dAddress=dataSnapshot1.getValue(D_Address.class);
                      addressArrayList.add(dAddress.toString());
                  }
                }
                else
                {
                    Log.e("CompleteViewOfProduct","Empty Snapshot of Address");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter.notifyDataSetChanged();
    }

    private void checkAlreadyBookMarked()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.child("MyWishList").child(ProductLink).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                   isBookMarked=true;
                   CurrentBookMarkedId=ProductLink;
                   Bookmark.setImageResource(R.drawable.ic_wishlist_full);
                }
                else
                {
                     isBookMarked=false;
                     Bookmark.setImageResource(R.drawable.ic_wishlist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
