package com.example.capstone0.BottomNavigationThings.CommonUtils;

public class D_PastOrders {

    public String CustomerName,CustomerMobileNumber,CustomerEmail,CustomerAddress,ProductName,ProductDescription,ProductImage,NoOfOrdersPurchased,ProductSize,ProductCategoryByGender,ProductCategoryByMaterial,AmountPaid,OrderId,DateOfPurchase_DeliveryStatus;

    public D_PastOrders(String customerName, String customerMobileNumber, String customerEmail, String customerAddress, String productName, String productDescription, String productImage, String noOfOrdersPurchased, String productSize, String productCategoryByGender, String productCategoryByMaterial, String amountPaid, String orderId, String dateOfPurchase_DeliveryStatus) {
        CustomerName = customerName;
        CustomerMobileNumber = customerMobileNumber;
        CustomerEmail = customerEmail;
        CustomerAddress = customerAddress;
        ProductName = productName;
        ProductDescription = productDescription;
        ProductImage = productImage;
        NoOfOrdersPurchased = noOfOrdersPurchased;
        ProductSize = productSize;
        ProductCategoryByGender = productCategoryByGender;
        ProductCategoryByMaterial = productCategoryByMaterial;
        AmountPaid = amountPaid;
        OrderId = orderId;
        DateOfPurchase_DeliveryStatus = dateOfPurchase_DeliveryStatus;
    }

}
