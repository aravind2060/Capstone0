package com.example.capstone0.BottomNavigationThings.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.capstone0.BottomNavigationThings.MenShoes.D_ShoesDataFromInternet;
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
    ArrayList<D_ShoesDataFromInternet> arrayList=new ArrayList<>();
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<String> D_ProductCategoryByGender=new ArrayList<>();
    ArrayList<String>D_ProductLink=new ArrayList<>();
    ArrayList<String>D_ProductCategoryByMaterial=new ArrayList<>();
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
        GridLayoutManager linearLayoutManager=new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        myAdapterForWishList=new MyAdapterForWishList(arrayList);
        recyclerView.setAdapter(myAdapterForWishList);

        myAdapterForWishList.setOnCardViewItemClickListener(new OnCardViewItemClickListener() {
            @Override
            public void onItemClickListenerOfCardView(int position) {
                Intent intent=new Intent(getApplicationContext(), CompleteViewOfProduct.class);
                intent.putExtra("ProductCategoryByGender",D_ProductCategoryByGender.get(position));
                intent.putExtra("ProductCategoryByMaterial",D_ProductCategoryByMaterial.get(position));
                intent.putExtra("ProductLink",D_ProductLink.get(position));
                intent.putExtra("ImageLocation",arrayList.get(position).ImageLocation);
                intent.putExtra("ProductTitle",arrayList.get(position).ProductTitleOfShoe);
                intent.putExtra("ProductPrice",arrayList.get(position).ProductPriceOfShoe);
                intent.putExtra("ProductDescription",arrayList.get(position).ProductDescriptionOfShoe);
                intent.putExtra("SizeOfProduct","7");
                intent.putExtra("Quantity","1");
                startActivity(intent);
            }
        });
    }
    private void getArrayListData()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyWishList");
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
                    myAdapterForWishList.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        ArrayList<D_ShoesDataFromInternet> cartOrdersArrayList;
        OnCardViewItemClickListener onCardViewItemClickListener;

        public MyAdapterForWishList(ArrayList<D_ShoesDataFromInternet> arrayList1)
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
            holder.Name.setText(arrayList.get(position).ProductTitleOfShoe);
            holder.Price.setText(arrayList.get(position).ProductPriceOfShoe);
            Glide.with(getApplicationContext()).load(arrayList.get(position).ImageLocation).into(holder.ProductImage);
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
