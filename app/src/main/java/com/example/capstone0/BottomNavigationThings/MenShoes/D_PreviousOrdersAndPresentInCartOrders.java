package com.example.capstone0.BottomNavigationThings.MenShoes;


/*
  This class holds PreviousOrders
  And Items in Cart Data
 */
public class D_PreviousOrdersAndPresentInCartOrders {

   private String ProductCategoryByGender,ProductCategory,ProductImage,ProductTitle,ProductDescription,PriceDetails,Size,Quantity;

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
    public String getProductCategoryByGender() {
        return ProductCategoryByGender;
    }

    public void setProductCategoryByGender(String productCategoryByGender) {
        ProductCategoryByGender = productCategoryByGender;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductTitle() {
        return ProductTitle;
    }

    public void setProductTitle(String productTitle) {
        ProductTitle = productTitle;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

//    public String getNoOfOrdersPurchased() {
//        return NoOfOrdersPurchased;
//    }
//
//    public void setNoOfOrdersPurchased(String noOfOrdersPurchased) {
//        NoOfOrdersPurchased = noOfOrdersPurchased;
//    }

    public String getPriceDetails() {
        return PriceDetails;
    }

    public void setPriceDetails(String priceDetails) {
        PriceDetails = priceDetails;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
