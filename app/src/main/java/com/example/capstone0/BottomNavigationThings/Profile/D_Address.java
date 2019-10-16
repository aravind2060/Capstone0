package com.example.capstone0.BottomNavigationThings.Profile;

public class D_Address {

    public String PinCode;
    public String HouseNo;
    public String Road_Area_Colony;
    public String City;
    public String State;
    public String Name;
    public String Phone;
    public int AddressType;
    public String error=null;
    /*
      @AddressType --> 0 is home Address
                        1 is Office Address
     */

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
        AddressType=0;
        this.error=error;
    }
}
