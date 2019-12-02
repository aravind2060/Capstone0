 package com.example.capstone0.BottomNavigationThings.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstone0.BottomNavigationThings.CommonUtils.D_PastOrders;
import com.example.capstone0.BottomNavigationThings.MenShoes.CompleteViewOfProduct;
import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

 public class A_MyOrders extends AppCompatActivity {
     interface OnCardViewItemClickListener
     {
         void onItemClickListenerOfCardView(int position);
     }
     Toolbar toolbar;
     TextView noOfpastOrders;
     RecyclerView recyclerView;
     ArrayList<String> keys=new ArrayList<>();
     ArrayList<D_PastOrders> arrayListForPastOrders=new ArrayList<>();
     MyAdapterForPastOrders myAdapter;
      SwipeRefreshLayout swipeRefreshLayout;
      DatabaseReference databaseReferenceforkey=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyOrders");
      DatabaseReference databaseReferencefordata=FirebaseDatabase.getInstance().getReference("Users").child("UsersNormalShoeOrders");
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_a__my_orders);
         setRecyclerView();
         getSeriesOfMyOrdersData();
         getNoOfAddressOfPastOrders();
         findViewByIds();
         setSwipeRefreshLayout();
     }

     public void setSwipeRefreshLayout()
     {
         swipeRefreshLayout=findViewById(R.id.swipe_myOrders);
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
             Collections.shuffle(arrayListForPastOrders);
             myAdapter.notifyDataSetChanged();
             swipeRefreshLayout.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     swipeRefreshLayout.setRefreshing(false);
                 }
             }, 2000);
         }
     };
     private void setRecyclerView() {
         recyclerView=findViewById(R.id.MyOrders_RecyclerView);
         myAdapter = new MyAdapterForPastOrders(arrayListForPastOrders);
         GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
         recyclerView.setLayoutManager(gridLayoutManager);
         recyclerView.setHasFixedSize(true);
         recyclerView.setAdapter(myAdapter);
         myAdapter.setOnCardViewItemClickListener(new OnCardViewItemClickListener() {
             @Override
             public void onItemClickListenerOfCardView(int position) {
                 Intent intent=new Intent(getApplicationContext(), CompleteViewOfProduct.class);
                 intent.putExtra("ImageLocation",arrayListForPastOrders.get(position).ProductImage);
                 intent.putExtra("ProductTitle",arrayListForPastOrders.get(position).ProductName);
                 intent.putExtra("ProductPrice",arrayListForPastOrders.get(position).AmountPaid);
                 intent.putExtra("ProductDescription",arrayListForPastOrders.get(position).ProductDescription);
                 intent.putExtra("ProductCategoryByMaterial",arrayListForPastOrders.get(position).ProductCategoryByMaterial);
                 intent.putExtra("ProductCategoryByGender",arrayListForPastOrders.get(position).ProductCategoryByGender);
                 intent.putExtra("Quantity",arrayListForPastOrders.get(position).NoOfOrdersPurchased);
                 intent.putExtra("Size",arrayListForPastOrders.get(position).ProductSize);
                 startActivity(intent);
             }
         });
     }

     private void findViewByIds() {
         toolbar = findViewById(R.id.Toolbar_MyOrders);
         toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
         });
         noOfpastOrders = findViewById(R.id.NumberOfPastOrders);
     }
    private void getNoOfAddressOfPastOrders()
    {
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyOrders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    noOfpastOrders.setText(dataSnapshot.getChildrenCount()+" Past Orders");
                }
                else
                {
                    noOfpastOrders.setText("0 "+"Past Orders");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



     class MyAdapterForPastOrders extends RecyclerView.Adapter<MyAdapterForPastOrders.ViewHolderClass>
     {
         ArrayList<D_PastOrders> arrayList;
         OnCardViewItemClickListener onCardViewItemClickListener;
         MyAdapterForPastOrders(ArrayList<D_PastOrders> arrayList1)
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
             holder.Name.setText(arrayList.get(position).ProductName);
             holder.Price.setText(arrayList.get(position).AmountPaid);
             Glide.with(getApplicationContext()).load(arrayList.get(position).ProductImage).into(holder.ProductImage);

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


     private void getSeriesOfMyOrdersData() {
       databaseReferenceforkey.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists())
               {
                  for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                  {
                     if (dataSnapshot1.exists())
                     {
                         databaseReferencefordata.child(dataSnapshot1.getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                             {
                                 if (dataSnapshot.exists()) {
                                     String CustomerName = dataSnapshot.child("CustomerName").getValue(String.class);
                                     String CustomerMobileNumber = dataSnapshot.child("CustomerMobileNumber").getValue(String.class);
                                     String CustomerEmail = dataSnapshot.child("CustomerEmail").getValue(String.class);
                                     String ProductName = dataSnapshot.child("ProductName").getValue(String.class);
                                     String ProductDescription = dataSnapshot.child("ProductDescription").getValue(String.class);
                                     String ProductImage = dataSnapshot.child("ProductImage").getValue(String.class);
                                     String NoOfOrdersPurchased = dataSnapshot.child("NoOfOrdersPurchased").getValue(String.class);
                                     String ProductSize = dataSnapshot.child("ProductSize").getValue(String.class);
                                     String ProductCategoryByGender = dataSnapshot.child("ProductCategoryByGender").getValue(String.class);
                                     String ProductCategoryByMaterial = dataSnapshot.child("ProductCategoryByMaterial").getValue(String.class);
                                     String AmountPaid = dataSnapshot.child("AmountPaid").getValue(String.class);
                                     String CustomerAddress = dataSnapshot.child("CustomerAddress").getValue(String.class);
                                     String OrderId = dataSnapshot.child("OrderId").getValue(String.class);
                                     String DateOfPurchase_DeliveryStatus = dataSnapshot.child("DateOfPurchase_DeliveryStatus").getValue(String.class);
                                     arrayListForPastOrders.add(new D_PastOrders(CustomerName, CustomerMobileNumber, CustomerEmail, ProductName, ProductDescription, ProductImage, NoOfOrdersPurchased, ProductSize, ProductCategoryByGender, ProductCategoryByMaterial, AmountPaid, CustomerAddress, OrderId, DateOfPurchase_DeliveryStatus));
                                 }
                                 else
                                     Log.e("A_MyOrders","Unable to get data form exact location");
                                 }
                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });
                     }
                     else
                     {
                         Log.e("A_MyOrders"," Datasnapshot does not exist Unable to key");
                     }
                  }
               }
               else
               {
                  Log.e("A_MyOrders"," Datasnapshot does not exist Unable to key in myorders ");
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
       myAdapter.notifyDataSetChanged();
     }
 }
