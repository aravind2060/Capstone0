package com.example.capstone0.BottomNavigationThings.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.capstone0.D_CurrentUser;
import com.example.capstone0.Login.D_UserDataToStoreInFirebase;
import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
interface TaskCompletedFetchingAddress
{
    void TaskCompletedListenerForFetchingAddress(ArrayList<D_Address> d_addressArrayList);
}
public class A_MyAddresses extends AppCompatActivity implements View.OnClickListener, TaskCompletedFetchingAddress {

    ListView listView;
    ArrayList<D_Address> d_addresseslist=new ArrayList<>();
    TextView addnewAddress;
    TextView noofaddressessshow;
    int noofaddressindb= D_CurrentUser.getNoOfAddress();
    Toolbar toolbar;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaddresses);
        findViewByIds();
        getAddressFromFirebase();
    }

    public void findViewByIds()
    {
        toolbar=findViewById(R.id.Toolbar_MyAddress_Fragment);
        addnewAddress=findViewById(R.id.MyAddress_TxtView_Add_New_Address);
        listView=findViewById(R.id.MyAddress_Fragment_ListView);
        noofaddressessshow=findViewById(R.id.MyAddress_Fragment_No_of_Address);
        addnewAddress.setOnClickListener(this);
        noofaddressessshow.setText(noofaddressindb+" "+noofaddressessshow.getText());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setListView()
    {
        myAdapter=new MyAdapter(A_MyAddresses.this,R.layout.single_address_show,d_addresseslist);
        listView.setAdapter(myAdapter);
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
                 convertView= LayoutInflater.from(context).inflate(R.layout.single_address_show,parent,false);
            TextView name=convertView.findViewById(R.id.Single_Address_Name);
            TextView phone=convertView.findViewById(R.id.Single_Address_TxtView_Show_PhoneNumber);
            TextView Showall=convertView.findViewById(R.id.Single_Address_TxtView_ShowAll_Data_Here);
            TextView AddressType=convertView.findViewById(R.id.Single_Address_Address_Type);
            ImageView imageView=convertView.findViewById(R.id.Single_Address_ImgView_OverFlowMenu);
            name.setText(d_address.Name);
            phone.setText(d_address.Phone);
            AddressType.setText(d_address.AddressType);
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


    @Override
    public void TaskCompletedListenerForFetchingAddress(ArrayList<D_Address> d_addressArrayList) {
        d_addresseslist=d_addressArrayList;
        setListView();
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

             DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
              for (int i=1;i<=D_CurrentUser.getNoOfAddress();i++)
              {
                  databaseReference.child("Addresses"+i).addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           D_Address d_address=dataSnapshot.getValue(D_Address.class);
                           if (d_address!=null)
                               d_addressArrayList.add(d_address);
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });
              }
           return d_addressArrayList;
       }

       @Override
       protected void onPostExecute(ArrayList<D_Address> d_addresses) {
             taskCompletedFetchingAddress.TaskCompletedListenerForFetchingAddress(d_addresses);
           super.onPostExecute(d_addresses);
       }
   }

}
