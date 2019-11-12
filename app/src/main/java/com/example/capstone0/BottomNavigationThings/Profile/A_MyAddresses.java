package com.example.capstone0.BottomNavigationThings.Profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

interface TaskCompletedFetchingAddress
{
    void TaskCompletedListenerForFetchingAddress(ArrayList<D_Address> d_addressArrayList);
}
public class A_MyAddresses extends AppCompatActivity implements View.OnClickListener, TaskCompletedFetchingAddress {

    @Override
    protected void onStart() {
        super.onStart();
        getAddressFromFirebase();
        getNumberOfAddressesFromFireBase();
    }

    RecyclerView recyclerview;
    ArrayList<D_Address> d_addresseslist=new ArrayList<>();
    TextView addnewAddress;
    TextView noofaddressessshow;
    Toolbar toolbar;
    MyAdapterToSetAddressData myAdapter;
    long noofaddressindb=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaddresses);
        findViewByIds();
    }

    public void findViewByIds()
    {
        toolbar=findViewById(R.id.Toolbar_MyAddress_Fragment);
        addnewAddress=findViewById(R.id.MyAddress_TxtView_Add_New_Address);
        recyclerview =findViewById(R.id.MyAddress_Fragment_RecyclerView);
        noofaddressessshow=findViewById(R.id.MyAddress_Fragment_No_of_Address);
        addnewAddress.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setRecyclerView()
    {
        myAdapter=new MyAdapterToSetAddressData(d_addresseslist);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(manager);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(myAdapter);
    }
    @Override
    public void onClick(View v) {
         if (v.getId()==R.id.MyAddress_TxtView_Add_New_Address)
        {
            startActivity(new Intent(getApplicationContext(),A_AddNewAddress.class));
        }
    }





    @Override
    public void TaskCompletedListenerForFetchingAddress(ArrayList<D_Address> d_addressArrayList) {
        d_addresseslist=d_addressArrayList;
        setRecyclerView();
        myAdapter.notifyDataSetChanged();
    }

    public void getAddressFromFirebase()
    {
      new AsyncTaskToFetchAddressFromFireBase(A_MyAddresses.this).execute();
    }

   class AsyncTaskToFetchAddressFromFireBase extends AsyncTask<Void,Void,ArrayList<D_Address>>
   {
       TaskCompletedFetchingAddress taskCompletedFetchingAddress;
       AsyncTaskToFetchAddressFromFireBase(TaskCompletedFetchingAddress taskCompletedFetchingAddress)
       {
           this.taskCompletedFetchingAddress=taskCompletedFetchingAddress;
       }
       ArrayList<D_Address> d_addressArrayList=new ArrayList<>();

       @Override
       protected ArrayList<D_Address> doInBackground(Void... integers) {

             DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Addresses");
             databaseReference.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                     {
                         D_Address d_address=dataSnapshot1.getValue(D_Address.class);
                         if (d_address!=null)
                             d_addressArrayList.add(d_address);
                         Log.e("MyAddress","Inside Asynctask");
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });

           return d_addressArrayList;
       }

       @Override
       protected void onPostExecute(ArrayList<D_Address> d_addresses) {
             taskCompletedFetchingAddress.TaskCompletedListenerForFetchingAddress(d_addresses);
           super.onPostExecute(d_addresses);
       }
   }

   private void getNumberOfAddressesFromFireBase()
   {
       DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Addresses");
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               noofaddressindb=dataSnapshot.getChildrenCount();
               noofaddressessshow.setText(noofaddressindb+" Saved Address");
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
   }


   class MyAdapterToSetAddressData extends RecyclerView.Adapter<MyAdapterToSetAddressData.ViewHolderClass>
   {
       ArrayList<D_Address>addressArrayList;
       public MyAdapterToSetAddressData(ArrayList<D_Address> addresses)
       {
          this.addressArrayList=addresses;
       }

       @NonNull
       @Override
       public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           return new ViewHolderClass(LayoutInflater.from(getApplicationContext()).inflate(R.layout.single_address_show,parent,false));
       }

       @Override
       public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {

         holder.Name.setText(addressArrayList.get(position).Name);
         holder.Phone.setText(addressArrayList.get(position).Phone);
         holder.AddressType.setText(addressArrayList.get(position).AddressType);
         holder.ShowAll.setText(addressArrayList.get(position).HouseNo+"-"+addressArrayList.get(position).Road_Area_Colony+"\n"+addressArrayList.get(position).City+"-"+addressArrayList.get(position).State);

       }

       @Override
       public int getItemCount() {
           return addressArrayList.size();
       }

       private class ViewHolderClass extends RecyclerView.ViewHolder
       {
           TextView Name,AddressType,ShowAll,Phone;
           ImageView OverFlowMenu;
           public ViewHolderClass(@NonNull View itemView) {
               super(itemView);

               Name=itemView.findViewById(R.id.Single_Address_Name);
               AddressType=itemView.findViewById(R.id.Single_Address_Address_Type);
               ShowAll=itemView.findViewById(R.id.Single_Address_TxtView_ShowAll_Data_Here);
               Phone=itemView.findViewById(R.id.Single_Address_TxtView_Show_PhoneNumber);
               OverFlowMenu=itemView.findViewById(R.id.Single_Address_ImgView_OverFlowMenu);
           }
       }

   }
}
