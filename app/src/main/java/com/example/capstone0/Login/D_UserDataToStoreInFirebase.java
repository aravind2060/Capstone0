package com.example.capstone0.Login;

public class D_UserDataToStoreInFirebase {

    public String Name,Email,Phone,Gender;
    public int noOfAddress;

    public D_UserDataToStoreInFirebase(String name, String email, String phone, String gender) {
        Name = name;
        Email = email;
        Phone = phone;
        Gender = gender;
        noOfAddress=0;
    }

    public D_UserDataToStoreInFirebase() {
    }
}
