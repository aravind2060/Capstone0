package com.example.capstone0.BottomNavigationThings.MenShoes;


import com.example.capstone0.BottomNavigationThings.Profile.D_Address;

/*
  This class holds PreviousOrders
  And Items in Cart Data
 */
public class D_PreviousOrdersAndPresentInCartOrders {

   public String ProductCategoryByGender,ProductCategoryByMaterial,ProductImage,ProductPrice,OrderId,ProductTitle,ProductDescription,PriceDetails,Size,Quantity;
   public String DateOfPurchase_DeliveryStatus;
   public D_Address d_address;
   public D_PreviousOrdersAndPresentInCartOrders(String productCategoryByGender,String productCategoryByMaterial,String productImage,String productPrice,String orderId,String dateOfPurchase_DeliveryStatus,String productTitle,String productDescription,String size,String quantity,D_Address d_address)
   {
       this.ProductCategoryByGender=productCategoryByGender;
       this.ProductCategoryByMaterial=productCategoryByMaterial;
       this.ProductImage=productImage;
       this.ProductPrice=productPrice;
       this.DateOfPurchase_DeliveryStatus=dateOfPurchase_DeliveryStatus;
       this.OrderId=orderId;
       this.ProductTitle=productTitle;
       this.ProductDescription=productDescription;
       this.PriceDetails=productPrice;
       this.Size=size;
       this.Quantity=quantity;
       this.d_address=d_address;
   }
    public D_PreviousOrdersAndPresentInCartOrders(){}

    public D_PreviousOrdersAndPresentInCartOrders(String productCategoryByGender,String productCategoryByMaterial,String productTitle,String productPrice,String productImage,String productDescription,String quantity,String size)
    {
        this.ProductCategoryByGender=productCategoryByGender;
        this.ProductCategoryByMaterial=productCategoryByMaterial;
        this.ProductTitle=productTitle;
        this.ProductPrice=productPrice;
        this.ProductDescription=productDescription;
        this.ProductImage=productImage;
        this.Size=size;
        this.Quantity=quantity;
    }
}
