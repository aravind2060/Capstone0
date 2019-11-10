package com.example.capstone0.BottomNavigationThings.MenShoes;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
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

import com.example.capstone0.D_CurrentUser;
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

public class MenFragment extends Fragment{



    public static MenFragment newInstance() {
        return new MenFragment();
    }

    ArrayList<D_ShoesDataFromInternet> dShoesDataFromInternetsFormal =new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet> dShoesDataFromInternetsCasual=new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet>dShoesDataFromInternetsSports=new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet>dShoesDataFromInternetsSneakers=new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet>dShoesDataFromInternetsSmart=new ArrayList<>();
    ArrayList<D_ShoesDataFromInternet>dShoesDataFromInternetsEthnic=new ArrayList<>();
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




    private class MyAdapterForMenFragment extends FragmentPagerAdapter {

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
//                    new AsyncTaskToFetchProductDetails().execute("Formal");
//                    displayAProduct=new DisplayAProduct(getContext(), dShoesDataFromInternetsFormal,"Formal");
//                    break;
                    return new FormalShoe(getContext());
                case 1:
//                    new AsyncTaskToFetchProductDetails().execute("Sneakers");
//
//                    displayAProduct=new DisplayAProduct(getContext(), dShoesDataFromInternetsSneakers,"Sneakers");
//                    break;
                    return new SneakersShoe(getContext());
                case 2:
//                    new AsyncTaskToFetchProductDetails().execute("Sports");
//                    displayAProduct=new DisplayAProduct(getContext(), dShoesDataFromInternetsSports,"Sports");
//                    break;
                    return new SportsShoe(getContext());
                case 3:
//                    new AsyncTaskToFetchProductDetails().execute("Smart");
//                    displayAProduct=new DisplayAProduct(getContext(), dShoesDataFromInternetsSmart,"Smart");
//                    break;
                    return new SmartShoe(getContext());
                case 4:
//                    new AsyncTaskToFetchProductDetails().execute("Ethnic");
//                    displayAProduct=new DisplayAProduct(getContext(), dShoesDataFromInternetsEthnic,"Ethnic");
//                    break;
                    return new EthnicShoe(getContext());
                case 5:
//                    new AsyncTaskToFetchProductDetails().execute("Casual");
//                    displayAProduct=new DisplayAProduct(getContext(), dShoesDataFromInternetsCasual,"Casual");
//                    break;
                    return new CasualShoe(getContext());

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


    class AsyncTaskToFetchProductDetails extends AsyncTask<String,Void,ArrayList<D_ShoesDataFromInternet>>
    {


        ArrayList<D_ShoesDataFromInternet> arrayList=new ArrayList<>();

        @Override
        protected void onPostExecute(ArrayList<D_ShoesDataFromInternet> arrayList) {
            super.onPostExecute(arrayList);
            dShoesDataFromInternetsFormal=arrayList;
        }

        @Override
        protected ArrayList<D_ShoesDataFromInternet> doInBackground(String...strings) {
            String ImageCategory=strings[0];
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("MenFootWear").child(ImageCategory);
            if (ImageCategory.contentEquals("Formal"))
            {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                            if (dShoesDataFromInternet != null) {
                                arrayList.add(dShoesDataFromInternet);
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
            }
            else if (ImageCategory.contentEquals("Casual"))
            {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                            if (dShoesDataFromInternet != null) {
                                dShoesDataFromInternetsCasual.add(dShoesDataFromInternet);
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
            }
            else if (ImageCategory.contentEquals("Sports"))
            {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                            if (dShoesDataFromInternet != null) {
                                dShoesDataFromInternetsSports.add(dShoesDataFromInternet);
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
            }
            else if (ImageCategory.contentEquals("Sneakers"))
            {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                            if (dShoesDataFromInternet != null) {
                                dShoesDataFromInternetsSneakers.add(dShoesDataFromInternet);
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
            }
            else if (ImageCategory.contentEquals("Smart"))
            {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                            if (dShoesDataFromInternet != null) {
                                dShoesDataFromInternetsSmart.add(dShoesDataFromInternet);
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
            }
            else if (ImageCategory.contentEquals("Ethnic"))
            {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                            if (dShoesDataFromInternet != null) {
                                dShoesDataFromInternetsEthnic.add(dShoesDataFromInternet);
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
            }
            return null;
        }
    }


}
