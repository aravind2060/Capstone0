package com.example.capstone0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.capstone0.BottomNavigationThings.MenShoes.MenFragment;
import com.example.capstone0.BottomNavigationThings.Profile.ProfileFragment;
import com.example.capstone0.BottomNavigationThings.WomenShoes.WomenFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    public static final String FIRST_FRAGMENT_MEN="FIRST_FRAGMENT_MEN";
    public static final String SECOND_FRAGMENT_WOMEN="SECOND_FRAGMENT_WOMEN";
    public static final String FOURTH_FRAGMENT_PROFILE="FOURTH_FRAGMENT_PROFILE";
    MenFragment MenFragmentInstance=new MenFragment();
    WomenFragment WomenFragmentInstance=new WomenFragment();
    ProfileFragment ProfileFragmentInstance=ProfileFragment.newInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(itemSelectedListener);
        replaceFragment(MenFragmentInstance,FIRST_FRAGMENT_MEN);
        bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu_items);
}

    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_men:
                    replaceFragment(MenFragmentInstance,FIRST_FRAGMENT_MEN);
                    return true;
                case R.id.navigation_women:
                    replaceFragment(WomenFragmentInstance,SECOND_FRAGMENT_WOMEN);
                    return true;
                case R.id.navigation_profile:
                    replaceFragment(ProfileFragmentInstance,FOURTH_FRAGMENT_PROFILE);
                    return true;
            }
            return false;
        }

    };


    public void replaceFragment(Fragment fragment, String tag)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment,tag).commit();
    }


}
