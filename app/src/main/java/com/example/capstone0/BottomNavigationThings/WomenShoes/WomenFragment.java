package com.example.capstone0.BottomNavigationThings.WomenShoes;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capstone0.R;
import com.google.android.material.tabs.TabLayout;

public class WomenFragment extends Fragment {

    public static WomenFragment newInstance() {
        return new WomenFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.women_fragment, container, false);
        setupViewPagerFunction(view);
        return view;
    }
    private void setupViewPagerFunction(View view)
    {
        ViewPager viewPager=view.findViewById(R.id.ViewPagerInWomenFragment);
        viewPager.setAdapter(new MyAdapterForWomenShoe(getChildFragmentManager()));
        TabLayout tabLayout=view.findViewById(R.id.TabLayoutInWomenFragment);
        tabLayout.setupWithViewPager(viewPager);
    }
   class MyAdapterForWomenShoe extends FragmentPagerAdapter
   {

       public MyAdapterForWomenShoe(@NonNull FragmentManager fm) {
           super(fm);
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

       @NonNull
       @Override
       public Fragment getItem(int position) {
           DisplayAProductOfWomenShoe displayAProductOfWomenShoe=null;
           switch (position)
           {
               case 0:
//                   displayAProductOfWomenShoe=new DisplayAProductOfWomenShoe();
                   return new FormalWomenShoe(getContext());
//                   break;
               case 1:
//                   displayAProductOfWomenShoe=new DisplayAProductOfWomenShoe();
//                   break;
                   return new SneakersWomenShoe(getContext());
               case 2:
//                   displayAProductOfWomenShoe=new DisplayAProductOfWomenShoe();
//                   break;
                   return new SportsWomenShoe(getContext());
               case 3:
//                   displayAProductOfWomenShoe=new DisplayAProductOfWomenShoe();
//                   break;
                   return new SmartWomenShoe(getContext());
               case 4:
//                   displayAProductOfWomenShoe=new DisplayAProductOfWomenShoe();
//                   break;
                   return new EthnicWomenShoe(getContext());
               case 5:
//                   displayAProductOfWomenShoe=new DisplayAProductOfWomenShoe();
//                   break;
                   return new CasualWomenShoe(getContext());
           }
           return displayAProductOfWomenShoe;
       }

       @Override
       public int getCount() {
           return 6;
       }
   }
}
