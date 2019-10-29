package com.example.capstone0.BottomNavigationThings.MenShoes;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capstone0.R;
import com.google.android.material.tabs.TabLayout;

public class MenFragment extends Fragment {



    public static MenFragment newInstance() {
        return new MenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.men_fragment, container, false);
        ViewPager viewPager=view.findViewById(R.id.ViewPagerInMenFragment);
        viewPager.setAdapter(new MyAdapterForMenFragment(getFragmentManager()));
        TabLayout tabLayout=view.findViewById(R.id.TabLayoutInMenFragment);
        tabLayout.setupWithViewPager(viewPager);
     return view;
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
            Fragment fragment=null;
            switch (position)
            {
                case 0:
                    fragment=ToDisplay.getInstance(1);
                    break;
                case 1:
                    fragment=ToDisplay.getInstance(2);
                    break;
                case 2:
                    fragment=ToDisplay.getInstance(3);
                    break;
                case 3:
                    fragment=ToDisplay.getInstance(4);
                    break;
                case 4:
                    fragment=ToDisplay.getInstance(5);
                    break;
                case 5:
                    fragment=ToDisplay.getInstance(6);
                    break;

            }
            return fragment;
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
