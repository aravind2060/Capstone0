package com.example.capstone0.BottomNavigationThings.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone0.D_CurrentUser;
import com.example.capstone0.Login.A_SignIn;
import com.example.capstone0.R;
import com.example.capstone0.utils.CommonClassForSharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment implements View.OnClickListener {


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    ImageView profileImage,EditProfile;
    ImageView signout;
    TextView signout2;
    TextView viewalladdress;
    TextView profileName;
    TextView profilePhoneNumber;
    TextView AccountSettings;
    TextView Myorders;
    TextView WishList;
    TextView MyCart;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent intent=new Intent(getContext(),A_SignIn.class);
            intent.putExtra("Number",2);
            startActivity(intent);
        }
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
        Myorders=view.findViewById(R.id.Profile_Fragment_TxtView_Profile_MyOrders);
        WishList=view.findViewById(R.id.Profile_Fragment_TxtView_Profile_MyWishList);
        MyCart=view.findViewById(R.id.Profile_Fragment_TxtView_Profile_MyCart);
        EditProfile=view.findViewById(R.id.Profile_Fragment_ImgBtn_Profile_Edit_Btn);

        EditProfile.setOnClickListener(this);
        AccountSettings.setOnClickListener(this);
        viewalladdress.setOnClickListener(this);
        signout2.setOnClickListener(this);
        signout.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        Myorders.setOnClickListener(this);
        WishList.setOnClickListener(this);
        MyCart.setOnClickListener(this);
    }
     private void setProfileImage()
     {
         if (D_CurrentUser.Gender==null)
             profileImage.setImageResource(R.drawable.men_icon);
         else if (D_CurrentUser.Gender.contentEquals("Male"))
             profileImage.setImageResource(R.drawable.men_icon);
         else
             profileImage.setImageResource(R.drawable.women_icon);
     }
     private void setProfileName()
     {
         profileName.setText(D_CurrentUser.getName());
     }
     private void setProfilePhoneNumber(){
        profilePhoneNumber.setText(D_CurrentUser.getPhone());}

     @Override
    public void onClick(View v) {

        if (v.getId()==R.id.Profile_Fragment_ImgBtn_Profile_Edit_Btn)
        {
          changeProfile();
        }
        else if (v.getId()==R.id.Profile_Fragment_ImgView_Logout_Icon || v.getId()==R.id.Profile_Fragment_TxtView_Logout_Icon)
        {
            FirebaseAuth.getInstance().signOut();
            clearCurrentUserData();
            CommonClassForSharedPreferences.setDataIntoSharedPreference(getContext());
            Intent intent=new Intent(getContext(),A_SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if (v.getId()==R.id.Profile_Fragment_TxtView_Profile_MyAddresses)
        {
          startActivity(new Intent(getContext(), A_MyAddresses.class));
        }
        else if (v.getId()==R.id.Profile_Fragment_TxtView_Profile_MySettings)
        {
            startActivity(new Intent(getContext(),A_AccountSettings.class));
        }
        else if (v.getId()==R.id.Profile_Fragment_TxtView_Profile_MyOrders)
        {
            startActivity(new Intent(getContext(),A_MyOrders.class));
        }
        else if (v.getId()==R.id.Profile_Fragment_TxtView_Profile_MyWishList)
        {
            startActivity(new Intent(getContext(),A_WishList.class));
        }
        else if (v.getId()==R.id.Profile_Fragment_TxtView_Profile_MyCart)
        {
            startActivity(new Intent(getContext(),A_MyCart.class));
        }

    }

    private void changeProfile() {
        final TextInputLayout textInputLayoutForName=new TextInputLayout(getContext());
        textInputLayoutForName.setHint("Name");
        textInputLayoutForName.setErrorEnabled(true);
        final TextInputLayout textInputLayoutForPhone=new TextInputLayout(getContext());
        textInputLayoutForPhone.setErrorEnabled(true);
        textInputLayoutForPhone.setHint("Phone");

        final TextInputEditText textInputEditTextForName=new TextInputEditText(getContext());
        final TextInputEditText textInputEditTextForPhone=new TextInputEditText(getContext());
        textInputLayoutForName.addView(textInputEditTextForName);
        textInputLayoutForPhone.addView(textInputEditTextForPhone);
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textInputLayoutForName);
        linearLayout.addView(textInputLayoutForPhone);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(linearLayout).setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            String name=textInputEditTextForName.getEditableText().toString();
            String phone=textInputEditTextForPhone.getEditableText().toString();
            if (TextUtils.isEmpty(name))
            {
                textInputLayoutForName.setError("Name Cannot be empty!");
            }
            else if (TextUtils.isEmpty(phone))
            {
                textInputLayoutForPhone.setError("Phone cannot be empty!");
            }
            else
            {
                if (name.contentEquals(D_CurrentUser.getName()) || phone.contentEquals(D_CurrentUser.getPhone()))
                {
                    if (name.contentEquals(D_CurrentUser.getName()))
                        textInputLayoutForName.setError("Update with new Name");
                    else if (phone.contentEquals(D_CurrentUser.getPhone()))
                        textInputLayoutForPhone.setError("Update with new Phone number");
                }
                else
                {
                    if (phone.length()!=10)
                         textInputLayoutForPhone.setError("Phone number not valid!");
                    else
                    {
                        textInputLayoutForName.setError(null);
                        textInputLayoutForPhone.setError(null);
                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        databaseReference.child("Name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(getContext(), "Name Updated SuccessFully", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getContext(), "Unable to update name", Toast.LENGTH_SHORT).show();
                            }
                        });
                        databaseReference.child("Phone").setValue(phone);
                        D_CurrentUser.setName(name);
                        D_CurrentUser.setPhone(phone);
                        setProfileName();
                        setProfilePhoneNumber();
                        CommonClassForSharedPreferences.setDataIntoSharedPreference(getContext());

                    }
                }
            }
            }
        }).setTitle("Update Your details").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void clearCurrentUserData()
    {
        D_CurrentUser.setGender(null);
        D_CurrentUser.setPhone(null);
        D_CurrentUser.setName(null);
        D_CurrentUser.setEmail(null);
    }
}
