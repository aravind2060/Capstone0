package com.example.capstone0;

public class D_CurrentUser {
    public static String Name="Male",Email,Gender,Phone;
    public static int noOfAddress;

    public D_CurrentUser() {
    }
    public D_CurrentUser(String name,String email,String gender,String phone,int noOfAddresss)
    {
        Name=name;
        Email=email;
        Gender=gender;
        Phone=phone;
        noOfAddress=noOfAddresss;
    }
    public static String getName() {
        return Name;
    }

    public static void setName(String name) {
        Name = name;
    }

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getGender() {
        return Gender;
    }

    public static void setGender(String gender) {
        Gender = gender;
    }

    public static String getPhone() {
        return Phone;
    }

    public static void setPhone(String phone) {
        Phone = phone;
    }

    public static int getNoOfAddress() {
        return noOfAddress;
    }

    public static void setNoOfAddress(int noOfAddress) {
        D_CurrentUser.noOfAddress = noOfAddress;
    }
}
