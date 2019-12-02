package com.example.capstone0.BottomNavigationThings.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capstone0.BottomNavigationThings.CommonUtils.D_PastOrders;
import com.example.capstone0.BottomNavigationThings.MenShoes.CompleteViewOfProduct;
import com.example.capstone0.BottomNavigationThings.MenShoes.D_ShoesDataFromInternet;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class A_MyCart extends AppCompatActivity implements View.OnClickListener {

    private static final String CHANNEL_1_ID = "2";

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.BuyNow_MyCart)
        {
            setUpBuyNow();
        }
    }

    interface OnCardViewItemClickListener
    {
        void onItemClickListenerOfCardView(int position);
    }
    RecyclerView recyclerView;
    TextView noOfMyCart,PriceToDisplay;
    Button buynow;
    MyAdapterForMyCart myAdapterForMyCart;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<D_ShoesDataFromInternet> arrayList=new ArrayList<>();
    ArrayList<D_PastOrders> finalarraylist=new ArrayList<>();
    ArrayList<String> D_ProductCategoryByGender=new ArrayList<>();
    ArrayList<String>D_ProductLink=new ArrayList<>();
    ArrayList<String>D_ProductCategoryByMaterial=new ArrayList<>();
    ArrayList<String>keysarraylist=new ArrayList<>();
    ArrayList<String>ordersarraylist=new ArrayList<>();
    ArrayList<String> addressArrayList=new ArrayList<>();
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    String payeeAddress="9550140561@ybl",payeeName= D_CurrentUser.getName(),transactionNote="First Corporation:",fakeamount="1",currencyUnit="INR";
    int TotalCost=0;
    ArrayList<Integer> purchaseditems=new ArrayList<>();
    String D_address_For_Shipping,D_ProductTitle,D_ProductDescription,D_Image,D_Quantity,D_Size,amount,D_ProductCategoryByGender1,D_ProductCategoryByMaterial1;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date = new Date();
    int j=0;
    String D_Date=formatter.format(date);
    String D_OrderId=D_CurrentUser.getPhone()+""+System.currentTimeMillis();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__my_cart);
        findViewByIds();
         setRecyclerView();
         getNoOfAddressOfPastOrders();
         getArrayListData();
         setSwipeRefreshLayout();
         createNotifications();
         setListViewForAddress();
         getDataOfAddress();
    }
    public void setSwipeRefreshLayout()
    {
        swipeRefreshLayout=findViewById(R.id.swipe_mycart);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        mHandler.sendEmptyMessage(0);
                    }
                }, 1000);
            }
        });
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            Collections.shuffle(arrayList);
            myAdapterForMyCart.notifyDataSetChanged();
            Collections.shuffle(addressArrayList);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    };
    private void findViewByIds() {
        toolbar = findViewById(R.id.Toolbar_MyCart);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        noOfMyCart = findViewById(R.id.NumberOfMyCartOrders);
        PriceToDisplay=findViewById(R.id.Price_MyCart);
        buynow=findViewById(R.id.BuyNow_MyCart);
        buynow.setOnClickListener(this);
    }

    private void setRecyclerView()
    {
      recyclerView=findViewById(R.id.MyCart_RecyclerView);
      myAdapterForMyCart=new MyAdapterForMyCart(arrayList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapterForMyCart);

        myAdapterForMyCart.setOnCardViewItemClickListener(new OnCardViewItemClickListener() {
            @Override
            public void onItemClickListenerOfCardView(int position) {
                if (!purchaseditems.contains(position))
                    purchaseditems.add(position);
            }
        });
    }
    private void getNoOfAddressOfPastOrders()
    {
         DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyCart");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    noOfMyCart.setText(dataSnapshot.getChildrenCount()+" In Your Cart");
                }
                else
                {
                    noOfMyCart.setText("0 Items In Your Cart");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setListViewForAddress()
    {

        listView=findViewById(R.id.ListView_Address_MyCart);
        adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_single_choice,addressArrayList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                D_address_For_Shipping=addressArrayList.get(position);
            }
        });
    }
    private void getArrayListData()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyCart");
        final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        String pg=dataSnapshot1.child("ProductCategoryByGender").getValue(String.class),pm=dataSnapshot1.child("ProductCategoryByMaterial").getValue(String.class),pl=dataSnapshot1.child("ProductLink").getValue(String.class);
                        D_ProductCategoryByGender.add(pg);
                        D_ProductCategoryByMaterial.add(pm);
                        D_ProductLink.add(pl);
                        databaseReference1.child(pg).child(pm).child(pl)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                        {
                                            String productDescriptionOfShoe=dataSnapshot.child("ProductDescriptionOfShoe").getValue(String.class);
                                            String ProductPrice=dataSnapshot.child("ProductPriceOfShoe").getValue(String.class);
                                            String productTitle=dataSnapshot.child("ProductTitleOfShoe").getValue(String.class);
                                            String imageLocation=dataSnapshot.child("ImageLocation").getValue(String.class);
                                            D_ShoesDataFromInternet dShoesDataFromInternet=new D_ShoesDataFromInternet(productTitle,ProductPrice,productDescriptionOfShoe,imageLocation);
                                            arrayList.add(dShoesDataFromInternet);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                    myAdapterForMyCart.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    class MyAdapterForMyCart extends RecyclerView.Adapter<MyAdapterForMyCart.ViewHolderClass>
    {
        ArrayList<D_ShoesDataFromInternet> arrayList;
        OnCardViewItemClickListener onCardViewItemClickListener;
        MyAdapterForMyCart(ArrayList<D_ShoesDataFromInternet> arrayList1)
        {
            this.arrayList=arrayList1;
        }
        public void setOnCardViewItemClickListener(OnCardViewItemClickListener onCardViewItemClickListener1)
        {
            this.onCardViewItemClickListener=onCardViewItemClickListener1;
        }
        @NonNull
        @Override
        public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolderClass(LayoutInflater.from(getApplicationContext()).inflate(R.layout.single_view_for_label_of_shoe,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
            holder.Name.setText(arrayList.get(position).ProductTitleOfShoe);
            holder.Price.setText(arrayList.get(position).ProductPriceOfShoe);
            Glide.with(getApplicationContext()).load(arrayList.get(position).ImageLocation).into(holder.ProductImage);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class ViewHolderClass extends RecyclerView.ViewHolder
        {
            TextView Name,Price;
            ImageView ProductImage;
            public ViewHolderClass(@NonNull View itemView) {
                super(itemView);
                Name=itemView.findViewById(R.id.SingleViewForLabelOfShoe_Name);
                Price=itemView.findViewById(R.id.SingleViewForLabelOfShoe_Price);
                ProductImage=itemView.findViewById(R.id.SingleViewForLabelOfShoe_ImageView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCardViewItemClickListener!=null)
                        {
                            int position=getAdapterPosition();
                            if (position!=RecyclerView.NO_POSITION)
                            {
                                onCardViewItemClickListener.onItemClickListenerOfCardView(position);
                            }
                        }
                    }
                });

            }
        }
    }
 private boolean checkAnySelectedItemsInArrayList()
 {
     if (purchaseditems.size()!=0)
     {
       for (int i=0;i<purchaseditems.size();i++)
       {
           TotalCost=TotalCost+Integer.parseInt(arrayList.get(purchaseditems.get(i)).ProductPriceOfShoe);
       }
        PriceToDisplay.setText(""+TotalCost);
         return true;
     }
         else
     {
         Snackbar.make(swipeRefreshLayout,"Please Select Items",Snackbar.LENGTH_LONG).show();
         return false;
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
                    Snackbar.make(swipeRefreshLayout,"Please Add Address By Going to Profile Section",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter.notifyDataSetChanged();
    }
  private void setUpBuyNow()
  {
    if (checkAnySelectedItemsInArrayList())
    {
        Uri uri = Uri.parse("upi://pay?pa="+payeeAddress+"&pn="+payeeName+"&tn="+transactionNote+
                "&am="+fakeamount+"&cu="+currencyUnit);
        Log.d("A_MyCart", "onClick: uri: "+uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivityForResult(intent,2);
    }
  }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2)
        {
            if (resultCode==RESULT_OK)
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
                        getKeysFromFirebase();
                        storeAllAddressIntoFirebase();
                    } else {
                        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                        Log.e("CompleteViewOfProduct","Payment failed");

                    }

                    snackbarHelp();
                }
            }
        }
    }
    private void snackbarHelp()
    {
        Snackbar.make(swipeRefreshLayout,"Need Help! Please feel free to",Snackbar.LENGTH_LONG).setAction("Contact Us!", new View.OnClickListener() {
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
    private void storeAllAddressIntoFirebase()
    {
       DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child("UsersNormalShoeOrders");
       final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyOrders");

       for (int i=0;i<keysarraylist.size();i++)
       {
         databaseReference.child(keysarraylist.get(i)).setValue(finalarraylist.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 if (task.isSuccessful())
                 {
                     Toast.makeText(A_MyCart.this, "Uploaded This product", Toast.LENGTH_SHORT).show();
                     databaseReference1.child(keysarraylist.get(j)).setValue(keysarraylist.get(j++));
                    TriggerNotifications();
                 }
                 else
                 {
                     Toast.makeText(A_MyCart.this, "Unable to upload product", Toast.LENGTH_SHORT).show();
                 }
             }
         });
       }
       removePresentItemsInCart();
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
            NotificationChannel channel=new NotificationChannel(CHANNEL_1_ID,"Channel1Booking", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Your Order has been Confirmed Successfully");
            channel.setShowBadge(true);
            NotificationManager manager=(NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    private void getKeysFromFirebase()
    {
       DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child("UsersNormalShoeOrders");
       for (int i=0;i<purchaseditems.size();i++)
       {
       String ke= databaseReference.push().getKey();
       keysarraylist.add(ke);
       }
       Random random=new Random();
       for (int i=0;i<purchaseditems.size();i++)
       {
           String ph=D_CurrentUser.getPhone()+""+System.currentTimeMillis()+random.nextInt(1000);
           ordersarraylist.add(ph);
       }

       for (int i=0;i<purchaseditems.size();i++)
       {
        finalarraylist.add(new D_PastOrders(D_CurrentUser.getName(),D_CurrentUser.getPhone(),D_CurrentUser.getEmail(),D_address_For_Shipping,arrayList.get(i).ProductTitleOfShoe,arrayList.get(i).ProductDescriptionOfShoe,arrayList.get(i).ImageLocation,"1","7",D_ProductCategoryByGender.get(i),D_ProductCategoryByMaterial.get(i),arrayList.get(i).ProductPriceOfShoe,ordersarraylist.get(i),formatter.format(date)+"_"+"Pending"));
       }
    }

    private void removePresentItemsInCart()
    {
       DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyCart");
       for (int i=0;i<keysarraylist.size();i++)
       {
          databaseReference.child(keysarraylist.get(i)).removeValue();
       }
    }

}
