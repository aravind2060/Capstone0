package com.example.capstone0.BottomNavigationThings.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstone0.D_CurrentUser;
import com.example.capstone0.Login.A_SignIn;
import com.example.capstone0.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment implements View.OnClickListener {


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    ImageView profileImage;
    ImageView signout;
    TextView signout2;
    TextView viewalladdress;
    TextView profileName;
    TextView profilePhoneNumber;
    TextView AccountSettings;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.profile_fragment, container, false);
       findViewByIds(view);
       setProfileImage();
       setProfileName();
       setProfilePhoneNumber();
       return view;
    }
     private void findViewByIds(View view)
     {
         profileImage =view.findViewById(R.id.Profile_Fragment_ImgBtn_Profile);
        signout=view.findViewById(R.id.Profile_Fragment_ImgView_Logout_Icon);
         signout2=view.findViewById(R.id.Profile_Fragment_TxtView_Logout_Icon);
        viewalladdress=view.findViewById(R.id.Profile_Fragment_TxtView_Profile_MyAddresses);
        profileName=view.findViewById(R.id.Profile_Fragment_TxtView_Profile_Name);
        profilePhoneNumber=view.findViewById(R.id.Profile_Fragment_TxtView_Profile_Phone);
        AccountSettings=view.findViewById(R.id.Profile_Fragment_TxtView_Profile_MySettings);

        AccountSettings.setOnClickListener(this);
        viewalladdress.setOnClickListener(this);
        signout2.setOnClickListener(this);
        signout.setOnClickListener(this);
        profileImage.setOnClickListener(this);

    }
     private void setProfileImage()
     {
          if (D_CurrentUser.Gender.contentEquals("Male"))
             profileImage.setImageResource(R.drawable.men_icon);
         else
             profileImage.setImageResource(R.drawable.women_icon);
     }
     private void setProfileName()
     {
         profileName.setText(D_CurrentUser.getName());
     }
     private void setProfilePhoneNumber(){profilePhoneNumber.setText(D_CurrentUser.getPhone());}

     @Override
    public void onClick(View v) {
        if (v.getId()==R.id.Profile_Fragment_ImgView_Logout_Icon || v.getId()==R.id.Profile_Fragment_TxtView_Logout_Icon)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), A_SignIn.class));
        }
        else if (v.getId()==R.id.Profile_Fragment_TxtView_Profile_MyAddresses)
        {
          startActivity(new Intent(getContext(), A_MyAddresses.class));
        }
        else if (v.getId()==R.id.Profile_Fragment_TxtView_Profile_MySettings)
        {
            startActivity(new Intent(getContext(),A_AccountSettings.class));
        }

    }


}
