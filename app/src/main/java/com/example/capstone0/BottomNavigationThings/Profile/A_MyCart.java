package com.example.capstone0.BottomNavigationThings.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstone0.BottomNavigationThings.MenShoes.CompleteViewOfProduct;
import com.example.capstone0.BottomNavigationThings.MenShoes.D_PreviousOrdersAndPresentInCartOrders;
import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class A_MyCart extends AppCompatActivity {

    interface OnCardViewItemClickListener
    {
        void onItemClickListenerOfCardView(int position);
    }
    RecyclerView recyclerView;
    TextView noOfMyCart;
    MyAdapterForMyCart myAdapterForMyCart;
    ArrayList<D_PreviousOrdersAndPresentInCartOrders> arrayListForMyCart=new ArrayList<>();
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__my_cart);
        findViewByIds();
         setRecyclerView();
         getNoOfAddressOfPastOrders();
         getArrayListData();
         setSwipeRefreshLayout();
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
            Collections.shuffle(arrayListForMyCart);
            myAdapterForMyCart.notifyDataSetChanged();
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
    }

    private void setRecyclerView()
    {
      recyclerView=findViewById(R.id.MyCart_RecyclerView);
      myAdapterForMyCart=new MyAdapterForMyCart(arrayListForMyCart);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapterForMyCart);

        myAdapterForMyCart.setOnCardViewItemClickListener(new OnCardViewItemClickListener() {
            @Override
            public void onItemClickListenerOfCardView(int position) {
                Intent intent=new Intent(getApplicationContext(), CompleteViewOfProduct.class);
                intent.putExtra("ImageLocation",arrayListForMyCart.get(position).ProductImage);
                intent.putExtra("ProductTitle",arrayListForMyCart.get(position).ProductTitle);
                intent.putExtra("ProductPrice",arrayListForMyCart.get(position).PriceDetails);
                intent.putExtra("ProductDescription",arrayListForMyCart.get(position).ProductDescription);
                intent.putExtra("ProductCategoryByMaterial",arrayListForMyCart.get(position).ProductCategoryByMaterial);
                intent.putExtra("ProductCategoryByGender",arrayListForMyCart.get(position).ProductCategoryByGender);
                intent.putExtra("Quantity",arrayListForMyCart.get(position).Quantity);
                intent.putExtra("Size",arrayListForMyCart.get(position).Size);
                startActivity(intent);
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

    private void getArrayListData()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyOrders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListForMyCart.clear();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    if (dataSnapshot1.exists()) {
                        String ProductCategoryByGender=dataSnapshot1.child("ProductCategoryByGender").getValue(String.class);
                        String ProductCategoryByMaterial=dataSnapshot1.child("ProductCategoryByMaterial").getValue(String.class);
                        String ImageLocation=dataSnapshot1.child("ImageLocation").getValue(String.class);
                        String ProductPrice=dataSnapshot1.child("ProductPrice").getValue(String.class);
                        String ProductTitle=dataSnapshot1.child("ProductTitle").getValue(String.class);
                        String ProductDescription=dataSnapshot1.child("ProductDescription").getValue(String.class);
                        String SizeOfProduct=dataSnapshot1.child("SizeOfProduct").getValue(String.class);
                        String Quantity=dataSnapshot1.child("Quantity").getValue(String.class);
                        arrayListForMyCart.add(new D_PreviousOrdersAndPresentInCartOrders(ProductCategoryByGender,ProductCategoryByMaterial,ProductTitle,ProductPrice,ImageLocation,ProductDescription,Quantity,SizeOfProduct));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myAdapterForMyCart.notifyDataSetChanged();
    }
    class MyAdapterForMyCart extends RecyclerView.Adapter<MyAdapterForMyCart.ViewHolderClass>
    {
        ArrayList<D_PreviousOrdersAndPresentInCartOrders> arrayList;
        OnCardViewItemClickListener onCardViewItemClickListener;
        MyAdapterForMyCart(ArrayList<D_PreviousOrdersAndPresentInCartOrders> arrayList1)
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
            holder.Name.setText(arrayList.get(position).ProductTitle);
            holder.Price.setText(arrayList.get(position).PriceDetails);
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
}
