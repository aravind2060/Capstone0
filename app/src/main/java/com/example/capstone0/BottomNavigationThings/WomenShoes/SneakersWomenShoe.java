package com.example.capstone0.BottomNavigationThings.WomenShoes;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstone0.BottomNavigationThings.MenShoes.CompleteViewOfProduct;
import com.example.capstone0.BottomNavigationThings.MenShoes.D_ShoesDataFromInternet;
import com.example.capstone0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class SneakersWomenShoe extends Fragment {
    interface OnCardViewItemClickListener
    {
        void onItemClickListenerOfCardView(int position);
    }
    ArrayList<D_ShoesDataFromInternet> arrayListSneakers=new ArrayList<>();
    MyAdapterForSneakersWomen adapterForSneakersWomen;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sneakers_women_shoe, container, false);
        setSwipeRefreshLayout(view);
        setRecyclerView(view);
        getArrayListData();
        return view;
    }
    public void setSwipeRefreshLayout(View view)
    {
        swipeRefreshLayout=view.findViewById(R.id.swipe_women_sneakers);
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
            Collections.shuffle(arrayListSneakers);
            adapterForSneakersWomen.notifyDataSetChanged();
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    };
    private void setRecyclerView(View view)
    {
        RecyclerView recyclerView=view.findViewById(R.id.RecyclerView_Sneakers_Women);
        GridLayoutManager manager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapterForSneakersWomen=new MyAdapterForSneakersWomen(arrayListSneakers);
        recyclerView.setAdapter(adapterForSneakersWomen);
        adapterForSneakersWomen.setOnCardViewItemClickListener(new OnCardViewItemClickListener() {
            @Override
            public void onItemClickListenerOfCardView(int position) {
                Intent intent=new Intent(getContext(), CompleteViewOfProduct.class);
                intent.putExtra("ImageLocation",arrayListSneakers.get(position).ImageLocation);
                intent.putExtra("ProductTitle",arrayListSneakers.get(position).ProductTitleOfShoe);
                intent.putExtra("ProductPrice",arrayListSneakers.get(position).ProductPriceOfShoe);
                intent.putExtra("ProductDescription",arrayListSneakers.get(position).ProductDescriptionOfShoe);
                intent.putExtra("ProductCategoryByGender","WomenFootWear");
                intent.putExtra("ProductCategoryByMaterial","Sneakers");
                startActivity(intent);
            }
        });
    }

    public void getArrayListData()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("WomenFootWear").child("Sneakers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListSneakers.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    if (dataSnapshot1.exists())
                    {
                        String productDescriptionOfShoe=dataSnapshot1.child("ProductDescriptionOfShoe").getValue(String.class);
                        String ProductPrice=dataSnapshot1.child("ProductPriceOfShoe").getValue(String.class);
                        String productTitle=dataSnapshot1.child("ProductTitleOfShoe").getValue(String.class);
                        String imageLocation=dataSnapshot1.child("ImageLocation").getValue(String.class);
                        D_ShoesDataFromInternet dShoesDataFromInternet=new D_ShoesDataFromInternet(productTitle,ProductPrice,productDescriptionOfShoe,imageLocation);
                        arrayListSneakers.add(dShoesDataFromInternet);
                    }
                    else
                    {

                    }
                }
                adapterForSneakersWomen.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    class MyAdapterForSneakersWomen extends RecyclerView.Adapter<MyAdapterForSneakersWomen.ViewHolderClass>
    {

        ArrayList<D_ShoesDataFromInternet> arrayList;
        OnCardViewItemClickListener onCardViewItemClickListener;
        public MyAdapterForSneakersWomen(ArrayList<D_ShoesDataFromInternet> arrayList1)
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

            View view=LayoutInflater.from(getContext()).inflate(R.layout.single_view_for_label_of_shoe,parent,false);
            return new ViewHolderClass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {

            holder.Name.setText(arrayList.get(position).ProductTitleOfShoe);
            holder.Price.setText(arrayList.get(position).ProductPriceOfShoe);
            Glide.with(getContext()).load(arrayList.get(position).ImageLocation).into(holder.ProductImage);
        }


        @Override
        public int getItemCount() {
            return arrayList.size();
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
