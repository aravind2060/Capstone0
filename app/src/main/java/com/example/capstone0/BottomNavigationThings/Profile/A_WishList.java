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
import com.example.capstone0.BottomNavigationThings.MenShoes.EthnicShoe;
import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class A_WishList extends AppCompatActivity {

    interface OnCardViewItemClickListener
    {
        void onItemClickListenerOfCardView(int position);
    }
    RecyclerView recyclerView;
    TextView noOfWishListed;
    MyAdapterForWishList myAdapterForWishList;
    ArrayList<D_PreviousOrdersAndPresentInCartOrders> arrayList=new ArrayList<>();
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__wish_list);
        setNoOfWishListed();
        getNoOfWishListedProducts();
        setToolBar();
        setSwipeRefreshLayout();
        setRecyclerView();
        getArrayListData();
    }
    private void setNoOfWishListed()
    {
        noOfWishListed=findViewById(R.id.NoOf_WishListed_Products);
    }

   private void setSwipeRefreshLayout()
   {
       swipeRefreshLayout=findViewById(R.id.swipe_wishlist);
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
            myAdapterForWishList.notifyDataSetChanged();
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    };
    private void setToolBar() {
     toolbar=findViewById(R.id.Toolbar_WishList);
     toolbar.setNavigationOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             onBackPressed();
         }
     });
    }

    private void setRecyclerView()
    {
        recyclerView=findViewById(R.id.RecyclerView_wishList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        myAdapterForWishList=new MyAdapterForWishList(arrayList);
        recyclerView.setAdapter(myAdapterForWishList);

        myAdapterForWishList.setOnCardViewItemClickListener(new OnCardViewItemClickListener() {
            @Override
            public void onItemClickListenerOfCardView(int position) {
                Intent intent=new Intent(getApplicationContext(), CompleteViewOfProduct.class);
                intent.putExtra("ProductCategoryByGender",arrayList.get(position).ProductCategoryByGender);
                intent.putExtra("ProductCategoryByMaterial",arrayList.get(position).ProductCategoryByMaterial);
                intent.putExtra("ImageLocation",arrayList.get(position).ProductImage);
                intent.putExtra("ProductTitle",arrayList.get(position).ProductTitle);
                intent.putExtra("ProductPrice",arrayList.get(position).ProductPrice);
                intent.putExtra("ProductDescription",arrayList.get(position).ProductDescription);
                intent.putExtra("SizeOfProduct",arrayList.get(position).Size);
                intent.putExtra("Quantity",arrayList.get(position).Quantity);
                startActivity(intent);
            }
        });
    }
    private void getArrayListData()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyWishList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                       String ProductCategoryByGender=dataSnapshot1.child("ProductCategoryByGender").getValue(String.class);
                       String ProductCategoryByMaterial=dataSnapshot1.child("ProductCategoryByMaterial").getValue(String.class);
                       String ImageLocation=dataSnapshot1.child("ImageLocation").getValue(String.class);
                       String ProductTitle=dataSnapshot1.child("ProductTitle").getValue(String.class);
                       String ProductPrice=dataSnapshot1.child("ProductPrice").getValue(String.class);
                       String ProductDescription=dataSnapshot1.child("ProductDescription").getValue(String.class);
                       String SizeOfProduct=dataSnapshot1.child("SizeOfProduct").getValue(String.class);
                       String Quantity=dataSnapshot1.child("Quantity").getValue(String.class);
                       D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders=new D_PreviousOrdersAndPresentInCartOrders(ProductCategoryByGender,ProductCategoryByMaterial,ProductTitle,ProductPrice,ImageLocation,ProductDescription,Quantity,SizeOfProduct);
                       arrayList.add(d_previousOrdersAndPresentInCartOrders);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myAdapterForWishList.notifyDataSetChanged();
    }

    private void getNoOfWishListedProducts()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyWishList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    noOfWishListed.setText("You have "+dataSnapshot.getChildrenCount()+" Products in WishList");
                }
                else
                    noOfWishListed.setText("You have 0 wishListed products");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class MyAdapterForWishList extends RecyclerView.Adapter<MyAdapterForWishList.ViewHolderClass>
    {
        ArrayList<D_PreviousOrdersAndPresentInCartOrders> cartOrdersArrayList;
        OnCardViewItemClickListener onCardViewItemClickListener;

        public MyAdapterForWishList(ArrayList<D_PreviousOrdersAndPresentInCartOrders> arrayList1)
        {
         this.cartOrdersArrayList=arrayList1;
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
            holder.Price.setText(arrayList.get(position).ProductPrice);
            Glide.with(getApplicationContext()).load(arrayList.get(position).ProductImage).into(holder.ProductImage);
        }

        @Override
        public int getItemCount() {
            return cartOrdersArrayList.size();
        }

        private class ViewHolderClass extends RecyclerView.ViewHolder
        {
            TextView Name,Price;
            ImageView ProductImage;
            public ViewHolderClass(@NonNull View itemView)
            {
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
