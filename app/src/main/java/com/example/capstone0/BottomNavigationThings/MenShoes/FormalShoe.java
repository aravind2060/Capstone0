package com.example.capstone0.BottomNavigationThings.MenShoes;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class FormalShoe extends Fragment {


    ArrayList<D_ShoesDataFromInternet> arrayListFormal=new ArrayList<>();

    Context context;
    MyAdapterForFormal myAdapterForFormal;
    FormalShoe(Context context)
    {
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_formal_shoe,container,false);
        getArrayListData();
        setRecyclerView(view);
        return view;
    }


    public void setRecyclerView(View view)
    {
        RecyclerView recyclerView=view.findViewById(R.id.RecyclerView_Formal_Men);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        myAdapterForFormal=new MyAdapterForFormal(arrayListFormal);
        recyclerView.setAdapter(myAdapterForFormal);
    }

    public void getArrayListData()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("MenFootWear").child("Formal");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListFormal.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    if (dataSnapshot1.exists())
                    {
                        String productDescriptionOfShoe=dataSnapshot1.child("ProductDescriptionOfShoe").getValue(String.class);
                        String ProductPrice=dataSnapshot1.child("ProductPriceOfShoe").getValue(String.class);
                        String productTitle=dataSnapshot1.child("ProductTitleOfShoe").getValue(String.class);
                        String imageLocation=dataSnapshot1.child("ImageLocation").getValue(String.class);
                        D_ShoesDataFromInternet dShoesDataFromInternet=new D_ShoesDataFromInternet(productTitle,ProductPrice,productDescriptionOfShoe,imageLocation);
                        arrayListFormal.add(dShoesDataFromInternet);
                    }
                    else
                    {

                    }
                }
                myAdapterForFormal.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class AsyncTaskToFetchFormal extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... strings) {
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("MenFootWear").child("Formal");
            //arrayListFormal.clear();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                        D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                        if (dShoesDataFromInternet != null) {
                            arrayListFormal.add(dShoesDataFromInternet);
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

    class MyAdapterForFormal extends RecyclerView.Adapter<MyAdapterForFormal.ViewHolderClass>
    {

        ArrayList<D_ShoesDataFromInternet> arrayList;
        StorageReference storageReference=FirebaseStorage.getInstance().getReference("MenFootWear").child("Formal");
        public MyAdapterForFormal(ArrayList<D_ShoesDataFromInternet> arrayList1)
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
            StorageReference storageReference1=storageReference.child(arrayList.get(position).ImageLocation);
            Glide.with(getContext()).load(storageReference1).into(holder.ProductImage);
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
            }
        }
    }


   private void getImagesFromFireBase()
   {
      StorageReference storageReference=FirebaseStorage.getInstance().getReference("MenFootWear").child("Formal");

   }


    }
