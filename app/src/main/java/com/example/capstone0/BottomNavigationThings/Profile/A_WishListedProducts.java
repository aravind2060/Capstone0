package com.example.capstone0.BottomNavigationThings.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone0.BottomNavigationThings.MenShoes.CompleteViewOfProduct;
import com.example.capstone0.BottomNavigationThings.MenShoes.D_PreviousOrdersAndPresentInCartOrders;
import com.example.capstone0.D_CurrentUser;
import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class A_WishListedProducts extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    MyAdapter myAdapter;
    ArrayList<D_PreviousOrdersAndPresentInCartOrders> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__wish_listed_products);
     findViewByIds();
     setListView();
     }

    private void findViewByIds() {
     toolbar=findViewById(R.id.Toolbar_MyWishList);
     toolbar.setNavigationOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             onBackPressed();
         }
     });
     listView=findViewById(R.id.MyWishList_ListView);
    }
 private void setListView()
 {

    myAdapter=new MyAdapter(getApplicationContext(),R.layout.single_view_for_label_of_shoe,arrayList);
    listView.setAdapter(myAdapter);
 }
    class MyAdapter extends ArrayAdapter<D_PreviousOrdersAndPresentInCartOrders>
    {
        ArrayList<D_PreviousOrdersAndPresentInCartOrders>d_previousOrdersAndPresentInCartOrders;
        public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<D_PreviousOrdersAndPresentInCartOrders> objects) {
            super(context, resource, objects);
            d_previousOrdersAndPresentInCartOrders=objects;
        }

        @Override
        public int getCount() {
            return d_previousOrdersAndPresentInCartOrders.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView==null)
            {
                convertView= LayoutInflater.from(getContext()).inflate(R.layout.single_view_for_label_of_shoe,parent,false);
            }
            ImageButton imageButton=convertView.findViewById(R.id.SingleViewForLabelOfShoe_ImageView);
            TextView Name=convertView.findViewById(R.id.SingleViewForLabelOfShoe_Name);
            TextView price=convertView.findViewById(R.id.SingleViewForLabelOfShoe_Price);
            Name.setText(d_previousOrdersAndPresentInCartOrders.get(position).ProductTitle);
            price.setText(d_previousOrdersAndPresentInCartOrders.get(position).PriceDetails);

            String Gender = d_previousOrdersAndPresentInCartOrders.get(position).ProductCategoryByGender;
            String ImageCategory = d_previousOrdersAndPresentInCartOrders.get(position).ProductCategory;
            String Image = d_previousOrdersAndPresentInCartOrders.get(position).ProductImage;
            String Gen;
            if (Gender.equals("Male"))
                Gen = "MenFootWear";
            else
                Gen = "WomenFootWear";
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(Gen).child(ImageCategory).child(Image);
            //Glide.with(getContext()).load(storageReference).into(imageButton);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoCompleteProduct=new Intent(getContext(), CompleteViewOfProduct.class);
                    gotoCompleteProduct.putExtra("Title",d_previousOrdersAndPresentInCartOrders.get(position).ProductTitle);
                    gotoCompleteProduct.putExtra("ProductDescription",d_previousOrdersAndPresentInCartOrders.get(position).ProductDescription);
                    gotoCompleteProduct.putExtra("Price",d_previousOrdersAndPresentInCartOrders.get(position).PriceDetails);
                    gotoCompleteProduct.putExtra("Image",d_previousOrdersAndPresentInCartOrders.get(position).ProductImage);
                    gotoCompleteProduct.putExtra("ImageCategory",d_previousOrdersAndPresentInCartOrders.get(position).ProductCategory);
                    gotoCompleteProduct.putExtra("GenderCategory",d_previousOrdersAndPresentInCartOrders.get(position).ProductCategoryByGender);
                    startActivity(gotoCompleteProduct);
                }
            });
            Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoCompleteProduct=new Intent(getContext(), CompleteViewOfProduct.class);
                    gotoCompleteProduct.putExtra("Title",d_previousOrdersAndPresentInCartOrders.get(position).ProductTitle);
                    gotoCompleteProduct.putExtra("ProductDescription",d_previousOrdersAndPresentInCartOrders.get(position).ProductDescription);
                    gotoCompleteProduct.putExtra("Price",d_previousOrdersAndPresentInCartOrders.get(position).PriceDetails);
                    gotoCompleteProduct.putExtra("Image",d_previousOrdersAndPresentInCartOrders.get(position).ProductImage);
                    gotoCompleteProduct.putExtra("ImageCategory",d_previousOrdersAndPresentInCartOrders.get(position).ProductCategory);
                    gotoCompleteProduct.putExtra("GenderCategory",d_previousOrdersAndPresentInCartOrders.get(position).ProductCategoryByGender);
                    startActivity(gotoCompleteProduct);
                }
            });

            return convertView;
        }
    }

    class AsyncTaskToFetchWishListedProducts extends AsyncTask<Void,Void,ArrayList<D_PreviousOrdersAndPresentInCartOrders>>
    {
        ArrayList<D_PreviousOrdersAndPresentInCartOrders>arrayList1=new ArrayList<>();
        @Override
        protected ArrayList<D_PreviousOrdersAndPresentInCartOrders> doInBackground(Void... voids) {

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyWishList");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders=dataSnapshot1.getValue(D_PreviousOrdersAndPresentInCartOrders.class);
                        if (d_previousOrdersAndPresentInCartOrders!=null)
                            arrayList1.add(d_previousOrdersAndPresentInCartOrders);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return arrayList1;

        }

        @Override
        protected void onPostExecute(ArrayList<D_PreviousOrdersAndPresentInCartOrders> arrayLista) {
            super.onPostExecute(arrayList);
            if (arrayLista.size() > 0) {
                arrayList = arrayLista;
                myAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(A_WishListedProducts.this, "Empty WishList", Toast.LENGTH_SHORT).show();
        }
    }
}
