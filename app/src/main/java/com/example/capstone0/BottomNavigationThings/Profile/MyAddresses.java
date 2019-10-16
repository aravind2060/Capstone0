package com.example.capstone0.BottomNavigationThings.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MyAddresses extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    ArrayList<D_Address> d_addresseslist=new ArrayList<>();
    TextView addnewAddress;
    TextView noofaddressessshow;
    int noofaddressindb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaddresses);
        listView=findViewById(R.id.MyAddress_Fragment_ListView);
        noofaddressessshow=findViewById(R.id.MyAddress_Fragment_No_of_Address);
        noofaddressessshow.setText(noofAddresses()+" "+noofaddressessshow.getText());
        getAddressesFromFirebase();
        addnewAddress=findViewById(R.id.MyAddress_TxtView_Add_New_Address);
        addnewAddress.setOnClickListener(this);
        listView.setAdapter(new MyAdapter(getApplicationContext(),R.layout.single_address_show,d_addresseslist));
    }

    @Override
    public void onClick(View v) {
         if (v.getId()==R.id.MyAddress_TxtView_Add_New_Address)
        {
            startActivity(new Intent(getApplicationContext(),A_AddNewAddress.class));
        }
    }


    class MyAdapter extends ArrayAdapter<D_Address> implements View.OnClickListener {

        Context context;
        List<D_Address> addressArrayList;
        public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<D_Address> objects) {
            super(context, resource, objects);
            addressArrayList=objects;
            this.context=context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            D_Address d_address=addressArrayList.get(position);
            if (convertView==null)
            {
                convertView= LayoutInflater.from(getContext()).inflate(R.layout.single_address_show,parent,false);
            }
            TextView name=convertView.findViewById(R.id.Single_Address_Name);
            TextView phone=convertView.findViewById(R.id.Single_Address_TxtView_Show_PhoneNumber);
            TextView Showall=convertView.findViewById(R.id.Single_Address_TxtView_ShowAll_Data_Here);
            TextView AddressType=convertView.findViewById(R.id.Single_Address_Address_Type);
            ImageView imageView=convertView.findViewById(R.id.Single_Address_ImgView_OverFlowMenu);
            name.setText(d_address.Name);
            phone.setText(d_address.Phone);
            Showall.setText(d_address.HouseNo+","+d_address.Road_Area_Colony+","+d_address.City+",\n"+d_address.State+"-"+d_address.PinCode);
            imageView.setOnClickListener(this);
            return convertView;
        }


        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.Single_Address_ImgView_OverFlowMenu)
            {

            }

        }

    }

    public int noofAddresses()
    {
        final int count[]=new int[1];
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("noOfAddress").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    count[0]=dataSnapshot.getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        noofaddressindb=count[0];
        return count[0];
    }

    public void getAddressesFromFirebase()
    {
        for (int i=1;i<=noofaddressindb;i++) {
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Addresses").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if (dataSnapshot.exists())
                     {
                         d_addresseslist.add(dataSnapshot.getValue(D_Address.class));
                     }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


}
