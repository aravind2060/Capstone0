package com.example.capstone0.BottomNavigationThings.Profile;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

interface OnTaskCompleted {
    void onTaskCompletedListener(D_Address result);
}
public class ASyncTaskToFetchAddressUsingGps extends AsyncTask<Location,Void,D_Address>
{

   private Context context;
   private OnTaskCompleted taskCompleted;
    private static final String LOG_TAG="FetchAddressFrom";
     public ASyncTaskToFetchAddressUsingGps(Context context, OnTaskCompleted taskCompleted)
     {
         this.context=context;
         this.taskCompleted=taskCompleted;
     }


    @Override
    protected void onPostExecute(D_Address d_addresses) {
         taskCompleted.onTaskCompletedListener(d_addresses);
        super.onPostExecute(d_addresses);
    }
    @Override
    protected D_Address doInBackground(Location... locations)
    {
        Geocoder geocoder=new Geocoder(context, Locale.getDefault());
        Location location=locations[0];
        D_Address d_address=new D_Address(null);
        List<Address> addresses=null;
        try {
            addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            Log.d(LOG_TAG,"Service Not available");
            d_address.error="Service Not Available";
        }catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values
            Log.e(LOG_TAG, "Invalid coordinates");
            d_address.error="Invalid Coordinates";
        }

        if (addresses == null || addresses.size() == 0) {
            if (d_address.error.isEmpty()) {
                d_address.error = "No Address Found";
            }
        }
        else {
            // If an address is found, read it into D_Address
            Address address = addresses.get(0);
            // join them, and send them to the thread
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                d_address.PinCode=address.getPostalCode();
                d_address.HouseNo=address.getFeatureName();
                d_address.Road_Area_Colony=address.getLocality()+","+address.getSubLocality();
                d_address.City=address.getSubAdminArea();
                d_address.State=address.getAdminArea();
            }
        }
        return d_address;
    }
}