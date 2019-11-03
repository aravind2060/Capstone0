package com.example.capstone0.BottomNavigationThings.MenShoes;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.capstone0.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/*
  This Fragment holds ScrollTabs of MenFootWear
 */
public class MenFragment extends Fragment {



    public static MenFragment newInstance() {
        return new MenFragment();
    }

    ArrayList<D_ShoesDataFromInternet> arrayList;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.men_fragment, container, false);
        setupViewPagerFunction(view);
     return view;
    }

   private void setupViewPagerFunction(View view)
   {
       ViewPager viewPager=view.findViewById(R.id.ViewPagerInMenFragment);
       viewPager.setAdapter(new MyAdapterForMenFragment(getChildFragmentManager()));
       TabLayout tabLayout=view.findViewById(R.id.TabLayoutInMenFragment);
       tabLayout.setupWithViewPager(viewPager);
   }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class MyAdapterForMenFragment extends FragmentStatePagerAdapter {

        public MyAdapterForMenFragment(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            DisplayAProduct displayAProduct=null;
            switch (position)
            {
                case 0:
                    new AsyncTaskToFetchProductData().execute("Formal");
                    displayAProduct=new DisplayAProduct(getContext(),arrayList,"Formal");
                    break;
                case 1:
                    new AsyncTaskToFetchProductData().execute("Sneakers");
                    displayAProduct=new DisplayAProduct(getContext(),arrayList,"Sneakers");
                    break;
                case 2:
                    new AsyncTaskToFetchProductData().execute("Sports");
                    displayAProduct=new DisplayAProduct(getContext(),arrayList,"Sports");
                    break;
                case 3:
                    new AsyncTaskToFetchProductData().execute("Smart");
                    displayAProduct=new DisplayAProduct(getContext(),arrayList,"Smart");
                    break;
                case 4:
                    new AsyncTaskToFetchProductData().execute("Ethnic");
                    displayAProduct=new DisplayAProduct(getContext(),arrayList,"Ethnic");
                    break;
                case 5:
                    new AsyncTaskToFetchProductData().execute("Casual");
                    displayAProduct=new DisplayAProduct(getContext(),arrayList,"Casual");
                    break;

            }
            return displayAProduct;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String Name=null;
            switch (position) {
                case 0:
                    Name="Formal";
                    break;
                case 1:
                    Name="Sneakers";
                    break;
                case 2:
                    Name="Sports";
                    break;
                case 3:
                    Name="Smart";
                    break;
                case 4:
                    Name="Ethnic";
                    break;
                case 5:
                    Name="Casual";
                    break;
            }
                return Name;
        }
    }



    class AsyncTaskToFetchProductData extends AsyncTask<String,Void, ArrayList<D_ShoesDataFromInternet>> implements ValueEventListener {

         ArrayList<D_ShoesDataFromInternet>arrayList2=new ArrayList<>();
        @Override
        protected ArrayList<D_ShoesDataFromInternet> doInBackground(String... strings) {
            final int[] noOfProducts = new int[1];
            String Category=strings[0];
            DatabaseReference databaseReferenceForNoOfProducts=FirebaseDatabase.getInstance().getReference("MenFootWear").child(Category);
            databaseReferenceForNoOfProducts.child("Size").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        noOfProducts[0] = dataSnapshot.getValue(Integer.class);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            if(noOfProducts[0] ==0)
                return null;
            for (int i = 1; i<= noOfProducts[0]; i++)
            {
                databaseReferenceForNoOfProducts.child(Category+i).addValueEventListener(this);
            }
            return arrayList2;
        }


        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists())
            {
                D_ShoesDataFromInternet d_shoesDataFromInternet=dataSnapshot.getValue(D_ShoesDataFromInternet.class);
                if (d_shoesDataFromInternet!=null)
                    arrayList2.add(d_shoesDataFromInternet);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

        @Override
        protected void onPostExecute(ArrayList<D_ShoesDataFromInternet> d_shoesDataFromInternets) {
            super.onPostExecute(d_shoesDataFromInternets);
            if (d_shoesDataFromInternets==null)
                Toast.makeText(getContext(), "No Products", Toast.LENGTH_SHORT).show();
            else if (d_shoesDataFromInternets.size()>0)
                arrayList=d_shoesDataFromInternets;
        }

    }
}
