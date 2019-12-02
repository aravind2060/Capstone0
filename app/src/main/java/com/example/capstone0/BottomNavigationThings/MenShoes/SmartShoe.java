package com.example.capstone0.BottomNavigationThings.MenShoes;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
public class SmartShoe extends Fragment {

    interface OnCardViewItemClickListener
    {
        void onItemClickListenerOfCardView(int position);
    }
    ArrayList<D_ShoesDataFromInternet> arrayListSmart=new ArrayList<>();
    ArrayList<String> stringArrayList=new ArrayList<>();
    Context context;
    MyAdapterForSmartMen myAdapterSmart;
    SwipeRefreshLayout swipeRefreshLayout;
    SmartShoe(Context context)
    {
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_smart_shoe,container,false);
        setSwipeRefreshLayout(view);
        getArrayListData();
        setRecyclerView(view);
        return view;
    }
    public void setSwipeRefreshLayout(View view)
    {
        swipeRefreshLayout=view.findViewById(R.id.swipe_men_smart);
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
            Collections.shuffle(arrayListSmart);
            myAdapterSmart.notifyDataSetChanged();
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    };
    public void setRecyclerView(View view)
    {
        RecyclerView recyclerView=view.findViewById(R.id.RecyclerView_Smart_Men);
        GridLayoutManager manager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        myAdapterSmart=new MyAdapterForSmartMen(arrayListSmart);
        recyclerView.setAdapter(myAdapterSmart);
        myAdapterSmart.setOnCardViewItemClickListener(new OnCardViewItemClickListener() {
            @Override
            public void onItemClickListenerOfCardView(int position) {
                Intent intent=new Intent(getContext(),CompleteViewOfProduct.class);
                intent.putExtra("ProductLink",stringArrayList.get(position));
                intent.putExtra("ImageLocation",arrayListSmart.get(position).ImageLocation);
                intent.putExtra("ProductTitle",arrayListSmart.get(position).ProductTitleOfShoe);
                intent.putExtra("ProductPrice",arrayListSmart.get(position).ProductPriceOfShoe);
                intent.putExtra("ProductDescription",arrayListSmart.get(position).ProductDescriptionOfShoe);
                intent.putExtra("ProductCategoryByGender","MenFootWear");
                intent.putExtra("ProductCategoryByMaterial","Smart");
                startActivity(intent);
            }
        });
    }

    public void getArrayListData()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("MenFootWear").child("Smart");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListSmart.clear();
                stringArrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    if (dataSnapshot1.exists())
                    {
                        stringArrayList.add(dataSnapshot1.getKey());
                        String productDescriptionOfShoe=dataSnapshot1.child("ProductDescriptionOfShoe").getValue(String.class);
                        String ProductPrice=dataSnapshot1.child("ProductPriceOfShoe").getValue(String.class);
                        String productTitle=dataSnapshot1.child("ProductTitleOfShoe").getValue(String.class);
                        String imageLocation=dataSnapshot1.child("ImageLocation").getValue(String.class);
                        D_ShoesDataFromInternet dShoesDataFromInternet=new D_ShoesDataFromInternet(productTitle,ProductPrice,productDescriptionOfShoe,imageLocation);
                        arrayListSmart.add(dShoesDataFromInternet);
                    }
                }
                myAdapterSmart.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class AsyncTaskToFetchSmart extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... strings) {
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("MenFootWear").child("Smart");
            //arrayListSmart.clear();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                        D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                        if (dShoesDataFromInternet != null) {
                            arrayListSmart.add(dShoesDataFromInternet);
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

    class MyAdapterForSmartMen extends RecyclerView.Adapter<MyAdapterForSmartMen.ViewHolderClass>
    {

        ArrayList<D_ShoesDataFromInternet> arrayList;
        OnCardViewItemClickListener onCardViewItemClickListener;
        public MyAdapterForSmartMen(ArrayList<D_ShoesDataFromInternet> arrayList1)
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
