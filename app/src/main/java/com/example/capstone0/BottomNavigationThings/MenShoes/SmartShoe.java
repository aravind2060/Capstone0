package com.example.capstone0.BottomNavigationThings.MenShoes;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.capstone0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmartShoe extends Fragment {

    ArrayList<D_ShoesDataFromInternet> arrayListSmart=new ArrayList<>();
    Context context;
    MyAdapterSmart myAdapterSmart;
    SmartShoe(Context context)
    {
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_smart_shoe,container,false);
        getArrayListData();
        setListView(view);
        return view;
    }
    public void setListView(View view) {
        ListView listView=view.findViewById(R.id.ListViewInSmartMen);
        myAdapterSmart=new MyAdapterSmart(context,R.layout.single_view_for_label_of_shoe,arrayListSmart,"Smart");
        listView.setAdapter(myAdapterSmart);
    }

    public void getArrayListData()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("MenFootWear").child("Smart");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListSmart.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    if (dataSnapshot1.exists())
                    {
                        String productDescriptionOfShoe=dataSnapshot1.child("ProductDescriptionOfShoe").getValue(String.class);
                        String ProductPrice=dataSnapshot1.child("ProductPriceOfShoe").getValue(String.class);
                        String productTitle=dataSnapshot1.child("ProductTitleOfShoe").getValue(String.class);
                        D_ShoesDataFromInternet dShoesDataFromInternet=new D_ShoesDataFromInternet(productTitle,ProductPrice,productDescriptionOfShoe);
                        arrayListSmart.add(dShoesDataFromInternet);
                    }
                    else
                    {

                    }
                }
                myAdapterSmart.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    class MyAdapterSmart extends ArrayAdapter<D_ShoesDataFromInternet>
    {
        ArrayList<D_ShoesDataFromInternet> d_shoesDataFromInternets;
        Context context1;
        String ImageCategory;
        StorageReference storageReference;
        Uri downloadableuri=null;
         MyAdapterSmart(@NonNull Context context, int resource, @NonNull ArrayList<D_ShoesDataFromInternet> objects, String ImageCategory) {
            super(context, resource, objects);
            d_shoesDataFromInternets=objects;
            context1=context;
            this.ImageCategory=ImageCategory;
            storageReference= FirebaseStorage.getInstance().getReference("MenFootWear").child(ImageCategory);
        }
        private class ViewHolder
        {
            ImageView imageView;
            TextView ProductTitle,PriceOfProduct;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null)
            {
                viewHolder=new ViewHolder();
                convertView=LayoutInflater.from(context1).inflate(R.layout.single_view_for_label_of_shoe,parent,false);
                viewHolder.imageView=convertView.findViewById(R.id.SingleViewForLabelOfShoe_ImageView);
                viewHolder.PriceOfProduct=convertView.findViewById(R.id.SingleViewForLabelOfShoe_Price);
                viewHolder.ProductTitle=convertView.findViewById(R.id.SingleViewForLabelOfShoe_Name);
                convertView.setTag(viewHolder);
            }else
            {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            if(position<d_shoesDataFromInternets.size()) {
                Log.e("DisplayAProduct","DownloadProduct: "+(ImageCategory+(position+1)));
                storageReference.child(ImageCategory+(position+1)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadableuri=uri;
                        Log.e("DisplayAProduct","DownloadProduct: "+downloadableuri);
                    }
                });
                Picasso.get().load(downloadableuri).into(viewHolder.imageView);
                viewHolder.ProductTitle.setText(d_shoesDataFromInternets.get(position).ProductTitleOfShoe);
                viewHolder.PriceOfProduct.setText(d_shoesDataFromInternets.get(position).ProductPriceOfShoe);
            }
            return convertView;
        }

        @Override
        public int getCount() {
            Log.e("Display","Size: "+d_shoesDataFromInternets.size());
            return d_shoesDataFromInternets.size();
        }

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
}