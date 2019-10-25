package com.example.capstone0.Login;

public class D_UserDataToStoreInFirebase {

    public String Name,Email,Phone,Gender,Password;
    public int noOfAddress;

    public D_UserDataToStoreInFirebase(String name, String email, String phone, String gender,String password) {
        Name = name;
        Email = email;
        Phone = phone;
        Gender = gender;
        noOfAddress=0;
        Password=password;
    }

    public D_UserDataToStoreInFirebase() {
    }
}
