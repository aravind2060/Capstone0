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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.capstone0.D_CurrentUser;
import com.example.capstone0.R;
import com.example.capstone0.utils.Converter;
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

  int cart_count=0;
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
            switch (position)
            {
                case 0:
                    return new FormalShoe(getContext());
                case 1:
                    return new SneakersShoe(getContext());
                case 2:
                    return new SportsShoe(getContext());
                case 3:
                    return new SmartShoe(getContext());
                case 4:
                    return new EthnicShoe(getContext());
                case 5:
                    return new CasualShoe(getContext());

            }
            return null;
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

}
