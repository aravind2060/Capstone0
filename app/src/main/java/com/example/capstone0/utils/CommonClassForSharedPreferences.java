package com.example.capstone0.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.capstone0.D_CurrentUser;

public class CommonClassForSharedPreferences {

    public static void setDataIntoSharedPreference(Context context)
    { SharedPreferences sharedPreferences=context.getSharedPreferences("CurrentLoggedInUserDetails",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Name", D_CurrentUser.getName());
        editor.putString("Email",D_CurrentUser.getEmail());
        editor.putString("Gender",D_CurrentUser.getGender());
        editor.putString("Phone",D_CurrentUser.getPhone());
        editor.apply();
    }

    public static void getDataFromSharedPreference(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("CurrentLoggedInUserDetails",0);
        String EmailData=sharedPreferences.getString("Email","");
        if (!TextUtils.isEmpty(EmailData))
        {
            D_CurrentUser.setEmail(EmailData);
            D_CurrentUser.setGender(sharedPreferences.getString("Gender","Male"));
            D_CurrentUser.setName(sharedPreferences.getString("Name",""));
            D_CurrentUser.setPhone(sharedPreferences.getString("Phone",""));
        }
    }
}
