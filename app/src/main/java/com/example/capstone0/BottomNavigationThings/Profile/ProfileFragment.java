package com.example.capstone0.BottomNavigationThings.Profile;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstone0.Login.A_SignIn;
import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.profile_fragment, container, false);
       ImageView signout=view.findViewById(R.id.Profile_Fragment_ImgView_Logout_Icon);
        TextView signout2=view.findViewById(R.id.Profile_Fragment_TxtView_Logout_Icon);
        TextView viewalladdress=view.findViewById(R.id.Profile_Fragment_TxtView_Profile_MyAddresses);
        viewalladdress.setOnClickListener(this);
        signout2.setOnClickListener(this);
       signout.setOnClickListener(this);

       return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.Profile_Fragment_ImgView_Logout_Icon || v.getId()==R.id.Profile_Fragment_TxtView_Logout_Icon)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), A_SignIn.class));
        }
        else if (v.getId()==R.id.Profile_Fragment_TxtView_Profile_MyAddresses)
        {
          startActivity(new Intent(getContext(),MyAddresses.class));
        }
    }
}
