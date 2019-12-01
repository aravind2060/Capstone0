package com.example.capstone0.BottomNavigationThings.Profile;

import androidx.annotation.NonNull;

public class D_Address {

    public String PinCode;
    public String HouseNo;
    public String Road_Area_Colony;
    public String City;
    public String State;
    public String Name;
    public String Phone;
    public String AddressType;
    public String error=null;

    public D_Address()
    {

    }
    public D_Address(String error) {
        PinCode=null;
        HouseNo=null;
        Road_Area_Colony=null;
        City=null;
        State=null;
        Name=null;
        Phone=null;
        AddressType=null;
        this.error=error;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("Name:"+Name+"\n");
        stringBuilder.append("Phone:"+Phone+"\n");
        stringBuilder.append("HouseNo:"+HouseNo+"\n");
        stringBuilder.append("Road:"+Road_Area_Colony+"\n");
        stringBuilder.append("City:"+City+"\n");
        stringBuilder.append("State:"+State+"-"+PinCode);
        return stringBuilder.toString();
    }
}
