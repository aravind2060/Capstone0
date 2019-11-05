package com.example.capstone0.BottomNavigationThings.MenShoes;


/*
  This class holds PreviousOrders
  And Items in Cart Data
 */
public class D_PreviousOrdersAndPresentInCartOrders {

   public String ProductCategoryByGender,ProductCategory,ProductImage,ProductTitle,ProductDescription,PriceDetails,Size,Quantity;

    public D_PreviousOrdersAndPresentInCartOrders(String productCategoryByGender, String productCategory, String productImage, String productTitle, String productDescription, String priceDetails,String size) {
        ProductCategoryByGender = productCategoryByGender;
        ProductCategory = productCategory;
        ProductImage = productImage;
        ProductTitle = productTitle;
        ProductDescription = productDescription;
        PriceDetails = priceDetails;
        Size=size;
    }

    public D_PreviousOrdersAndPresentInCartOrders(String productCategoryByGender, String productCategory, String productImage, String productTitle, String productDescription, String priceDetails,String size,String quantity) {
        ProductCategoryByGender = productCategoryByGender;
        ProductCategory = productCategory;
        ProductImage = productImage;
        ProductTitle = productTitle;
        ProductDescription = productDescription;
        PriceDetails = priceDetails;
        Size=size;
        Quantity=quantity;
    }

    public D_PreviousOrdersAndPresentInCartOrders(){}

}
