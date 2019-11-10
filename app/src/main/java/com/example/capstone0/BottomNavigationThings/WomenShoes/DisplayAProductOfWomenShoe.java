package com.example.capstone0.BottomNavigationThings.WomenShoes;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.capstone0.BottomNavigationThings.MenShoes.D_ShoesDataFromInternet;
import com.example.capstone0.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayAProductOfWomenShoe extends Fragment {

    ArrayList<D_ShoesDataFromInternet> dataFromInternetsFormal=new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet> dataFromInternetsSneakers=new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet> dataFromInternetsEthnic=new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet> dataFromInternetsSports=new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet> dataFromInternetsSmart=new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet> dataFromInternetsCasual=new ArrayList<>();

    String Type=null;
    MyAdapterForProductOfListView myAdapterForProductOfListView;
    public DisplayAProductOfWomenShoe(String type) {
        // Required empty public constructor
        Type=type;
    }
   public DisplayAProductOfWomenShoe(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_display_aproduct_of_women_shoe, container, false);
       if (Type.contentEquals("Formal"))
           getArrayListDataFormal("Formal");


       return view;
     }

    private void getArrayListDataFormal(String type) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("WomenFootWear").child("Formal");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataFromInternetsFormal.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    if (dataSnapshot1.exists())
                    {
                        String productDescriptionOfShoe=dataSnapshot1.child("ProductDescriptionOfShoe").getValue(String.class);
                        String ProductPrice=dataSnapshot1.child("ProductPriceOfShoe").getValue(String.class);
                        String productTitle=dataSnapshot1.child("ProductTitleOfShoe").getValue(String.class);
                        D_ShoesDataFromInternet dShoesDataFromInternet=new D_ShoesDataFromInternet(productTitle,ProductPrice,productDescriptionOfShoe);
                        dataFromInternetsFormal.add(dShoesDataFromInternet);
                    }
                    else
                    {
                        Log.e("DisplayWomenShoe","Null returned");
                    }
                }
                notifyDataChangedForListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void notifyDataChangedForListView()
    {
       myAdapterForProductOfListView.notifyDataSetChanged();
    }
    class MyAdapterForProductOfListView extends ArrayAdapter<D_ShoesDataFromInternet>
    {
       ArrayList<D_ShoesDataFromInternet> arrayList;
        public MyAdapterForProductOfListView(@NonNull Context context, int resource, @NonNull ArrayList<D_ShoesDataFromInternet> objects) {
            super(context, resource, objects);
            arrayList=objects;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }
    }

}
