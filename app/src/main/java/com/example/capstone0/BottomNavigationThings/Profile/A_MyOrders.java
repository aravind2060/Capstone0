 package com.example.capstone0.BottomNavigationThings.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
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
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

 public class A_MyOrders extends AppCompatActivity {

     Toolbar toolbar;
     TextView noOfpastOrders;
     ListView listView;
     ArrayList<D_PreviousOrdersAndPresentInCartOrders> arrayList;
     MyAdapter myAdapter;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_a__my_orders);
         findViewByIds();

         setListView();

     }

     private void setListView() {
         new AsyncTaskToFetchPastOrdersFromFireBase().execute();
         myAdapter = new MyAdapter(getApplicationContext(), R.layout.singleviewforpurchasedorders, arrayList);
         listView = findViewById(R.id.MyOrders_ListView);
         listView.setAdapter(myAdapter);
     }

     private void findViewByIds() {
         toolbar = findViewById(R.id.Toolbar_MyOrders);
         toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
         });
         noOfpastOrders = findViewById(R.id.NumberOfPastOrders);
         listView = findViewById(R.id.MyOrders_ListView);

     }


     class MyAdapter extends ArrayAdapter<D_PreviousOrdersAndPresentInCartOrders> {
         List<D_PreviousOrdersAndPresentInCartOrders> d_previousOrdersAndPresentInCartOrders;

         public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<D_PreviousOrdersAndPresentInCartOrders> objects) {
             super(context, resource, objects);
             d_previousOrdersAndPresentInCartOrders = objects;
         }

         @NonNull
         @Override
         public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
             if (convertView == null) {
                 convertView = LayoutInflater.from(getContext()).inflate(R.layout.singleviewforpurchasedorders, parent, false);
             }
             ImageView imageButton = convertView.findViewById(R.id.ImageOfPurchasedProduct);
             TextView productTitle = convertView.findViewById(R.id.ProductTitle_MyOrders);
             TextView priceTitle = convertView.findViewById(R.id.Price_MyOrders);
             TextView Quantity = convertView.findViewById(R.id.Quantity_MyOrders);
             TextView Price = convertView.findViewById(R.id.Price_MyOrders);

             productTitle.setText(d_previousOrdersAndPresentInCartOrders.get(position).ProductTitle);
             priceTitle.setText(d_previousOrdersAndPresentInCartOrders.get(position).PriceDetails);
             Quantity.setText(d_previousOrdersAndPresentInCartOrders.get(position).Quantity);
             Price.setText(d_previousOrdersAndPresentInCartOrders.get(position).PriceDetails);

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
             return convertView;
         }

         @Override
         public int getCount() {
             return arrayList.size();
         }
     }

     class AsyncTaskToFetchPastOrdersFromFireBase extends AsyncTask<Void, Void, ArrayList<D_PreviousOrdersAndPresentInCartOrders>> {

         ArrayList<D_PreviousOrdersAndPresentInCartOrders> d_previousOrdersAndPresentInCartOrders = new ArrayList<>();

         @Override
         protected ArrayList<D_PreviousOrdersAndPresentInCartOrders> doInBackground(Void... voids) {
             DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyOrders");
             databaseReference.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                     {
                        D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrderss=dataSnapshot1.getValue(D_PreviousOrdersAndPresentInCartOrders.class);
                        if (d_previousOrdersAndPresentInCartOrderss!=null)
                            d_previousOrdersAndPresentInCartOrders.add(d_previousOrdersAndPresentInCartOrderss);
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });

             return d_previousOrdersAndPresentInCartOrders;
         }

         @Override
         protected void onPostExecute(ArrayList<D_PreviousOrdersAndPresentInCartOrders> d_previousOrdersAndPresentInCartOrders) {
             if (d_previousOrdersAndPresentInCartOrders.size() > 0) {
                 arrayList = d_previousOrdersAndPresentInCartOrders;
                 myAdapter.notifyDataSetChanged();
             }
                 else
                 Toast.makeText(A_MyOrders.this, "No Previous Orders", Toast.LENGTH_SHORT).show();
             super.onPostExecute(d_previousOrdersAndPresentInCartOrders);
         }

     }
 }
