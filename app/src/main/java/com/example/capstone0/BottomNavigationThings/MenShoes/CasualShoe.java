package com.example.capstone0.BottomNavigationThings.MenShoes;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capstone0.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CasualShoe extends Fragment {
    interface OnCardViewItemClickListener
    {
        void onItemClickListenerOfCardView(int position);
    }

    ArrayList<D_ShoesDataFromInternet> arrayListCasual=new ArrayList<>();
    Context context;
     MyAdapterForCasualmen myAdapterCasual;
     SwipeRefreshLayout swipeRefreshLayout;
    CasualShoe(Context context)
    {
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_casual_shoe,container,false);
        setSwipeRefreshLayout(view);
        getArrayListData();
        setRecyclerView(view);
        return view;
    }

    public void setSwipeRefreshLayout(View view)
    {
        swipeRefreshLayout=view.findViewById(R.id.swipe_men_casual);
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
            Collections.shuffle(arrayListCasual);
            myAdapterCasual.notifyDataSetChanged();
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    };
    public void setRecyclerView(View view) {
        RecyclerView recyclerView=view.findViewById(R.id.RecyclerViewInCasualMen);
        GridLayoutManager manager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
       myAdapterCasual=new MyAdapterForCasualmen(arrayListCasual);
        recyclerView.setAdapter(myAdapterCasual);

        myAdapterCasual.setOnCardViewItemClickListener(new OnCardViewItemClickListener() {
            @Override
            public void onItemClickListenerOfCardView(int position) {
                Intent intent=new Intent(getContext(),CompleteViewOfProduct.class);
                intent.putExtra("ImageLocation",arrayListCasual.get(position).ImageLocation);
                intent.putExtra("ProductTitle",arrayListCasual.get(position).ProductTitleOfShoe);
                intent.putExtra("ProductPrice",arrayListCasual.get(position).ProductPriceOfShoe);
                intent.putExtra("ProductDescription",arrayListCasual.get(position).ProductDescriptionOfShoe);
                intent.putExtra("ProductCategoryByGender","MenFootWear");
                intent.putExtra("ProductCategoryByMaterial","Casual");
                startActivity(intent);
            }
        });
    }

    public void getArrayListData()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("MenFootWear").child("Casual");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCasual.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    if (dataSnapshot1.exists())
                    {
                        String productDescriptionOfShoe=dataSnapshot1.child("ProductDescriptionOfShoe").getValue(String.class);
                        String ProductPrice=dataSnapshot1.child("ProductPriceOfShoe").getValue(String.class);
                        String productTitle=dataSnapshot1.child("ProductTitleOfShoe").getValue(String.class);
                        String imageLocation=dataSnapshot1.child("ImageLocation").getValue(String.class);
                        D_ShoesDataFromInternet dShoesDataFromInternet=new D_ShoesDataFromInternet(productTitle,ProductPrice,productDescriptionOfShoe,imageLocation);
                        arrayListCasual.add(dShoesDataFromInternet);
                    }
                }
                myAdapterCasual.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    class AsyncTaskToFetchCasual extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... strings) {
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("MenFootWear").child("Casual");
           // arrayListCasual.clear();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                        D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                        if (dShoesDataFromInternet != null) {
                            arrayListCasual.add(dShoesDataFromInternet);
                            Log.e("MenFragment","Going inside");
                        }
                        else
                        {
                            Log.e("MenFragment","Unable To go inside");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    class MyAdapterForCasualmen extends RecyclerView.Adapter<MyAdapterForCasualmen.ViewHolderClass> {

        ArrayList<D_ShoesDataFromInternet> arrayList;
        OnCardViewItemClickListener onCardViewItemClickListener;
        public void setOnCardViewItemClickListener(OnCardViewItemClickListener onCardViewItemClickListener1)
        {
            this.onCardViewItemClickListener=onCardViewItemClickListener1;
        }
        public MyAdapterForCasualmen(ArrayList<D_ShoesDataFromInternet> arrayList1)
        {
            this.arrayList=arrayList1;
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
