package com.example.capstone0.Login;

public class D_UserDataToStoreInFirebase {

    public String Name,Email,Phone,Gender,Password;
    public int noOfAddress,noOfItemsInCart,noPreviousOrders,noOfWishListedProducts;

    public D_UserDataToStoreInFirebase(String name, String email, String phone, String gender,String password) {
        Name = name;
        Email = email;
        Phone = phone;
        Gender = gender;
        noOfAddress=0;
        noOfItemsInCart=0;
        noPreviousOrders=0;
        noOfWishListedProducts=0;
        Password=password;
    }

    public D_UserDataToStoreInFirebase() {
    }
}
