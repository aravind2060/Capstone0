package com.example.capstone0.BottomNavigationThings.MenShoes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone0.D_CurrentUser;
import com.example.capstone0.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CompleteViewOfProduct extends AppCompatActivity implements View.OnClickListener {
// todo use imageswitcher
    ImageView ProductImage,Bookmark;
    TextView Price,ProductTitle,ProductDescription,QuantityCounter;
    Button SizeOfProduct_7,SizeOfProduct_8,SizeOfProduct_9,SizeOfProduct_10,AddToCart,BuyNow;
    ImageButton Quantity_Increment,Quantity_Decrement;

   String D_ProductTitle,D_Price,D_ProductDescription,D_ProductGender,D_ImageCategory,D_Image;
   int D_Quantity,D_Size,ActualPrice;
   Toolbar toolbar;
   boolean isBookMarked=true;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_view_of_product);
        findViewByIds();
        showImage();
    }
    private void findViewByIds()
    {
        ProductImage=findViewById(R.id.ImageOfProduct_Custom);
        Price=findViewById(R.id.Price);
        ProductTitle=findViewById(R.id.NameOfProduct);
        ProductDescription=findViewById(R.id.ProductDescription);
        QuantityCounter=findViewById(R.id.Quantity_Counter);
        Quantity_Increment=findViewById(R.id.Quantity_increment);
        Quantity_Decrement=findViewById(R.id.Quantity_decrement);
        SizeOfProduct_7=findViewById(R.id.Size_7);
        SizeOfProduct_8=findViewById(R.id.Size_8);
        SizeOfProduct_9=findViewById(R.id.Size_9);
        SizeOfProduct_10=findViewById(R.id.Size_10);
        Bookmark=findViewById(R.id.Bookmark_Image);
        AddToCart=findViewById(R.id.AddToCart);
        BuyNow=findViewById(R.id.BuyNow);
        toolbar=findViewById(R.id.Toolbar_complete_view_Of_Product);
        AddToCart.setOnClickListener(this);
        Bookmark.setOnClickListener(this);
        BuyNow.setOnClickListener(this);
        Quantity_Increment.setOnClickListener(this);
        Quantity_Decrement.setOnClickListener(this);
        QuantityCounter.setText("1");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showImage() {
        String ImageName=getIntent().getStringExtra("Image");
        String ImageCategory=getIntent().getStringExtra("ImageCategory");
        String GenderCategory=getIntent().getStringExtra("GenderCategory");
        StorageReference storageReference= FirebaseStorage.getInstance().getReference(GenderCategory).child(ImageCategory).child(ImageName);
        //Glide.with(getApplicationContext()).load(storageReference).into(ProductImage);
        D_Image= ImageName;
        D_ImageCategory=ImageCategory;
        D_ProductGender=GenderCategory;
        showPrice();
    }
    private void showPrice() {
       String price=getIntent().getStringExtra("Price");
       Price.setText(price);
       D_Price=price;
        showTitle();
    }
    private void showTitle()
    {
      String title=getIntent().getStringExtra("Title");
      ProductTitle.setText(title);
      D_ProductTitle=title;
     ShowProductDescription();
    }
    private void ShowProductDescription() {
     String description=getIntent().getStringExtra("ProductDescription");
     ProductDescription.setText(description);
     D_ProductDescription=description;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.BuyNow)
        {
           PaymentViaPaytm();
        }
        else if (v.getId()==R.id.AddToCart)
        {
         setAddToCart();
        }
        else if (v.getId()==R.id.Quantity_increment)
        {
           setQuantity_Increment();
        }
        else if (v.getId()==R.id.Quantity_decrement)
        {
            setQuantity_Decrement();
        }
        else if (v.getId()==R.id.Size_7)
        {
            //todo Change color of button
            D_Size=7;
        }
        else if (v.getId()==R.id.Size_8)
        {
            D_Size=8;
        }
        else if (v.getId()==R.id.Size_9)
        {
            D_Size=9;
        }
        else if (v.getId()==R.id.Size_10)
        {
            D_Size=10;
        }
        else if (v.getId()==R.id.Bookmark_Image)
        {
            if (isBookMarked)
            {
                Bookmark.setImageResource(R.drawable.bookmark_filled_blue);
                isBookMarked=false;
            }
            else
            {
                Bookmark.setImageResource(R.drawable.bookmark_black);
                isBookMarked=true;
            }
            setBookmarkStatus(isBookMarked);
        }
    }

    private void setQuantity_Increment() {
        int n1 = Integer.parseInt(QuantityCounter.getText().toString());
        if (n1 >= 3) {
            QuantityCounter.setText("3");
            D_Quantity =3;
            Quantity_Increment.setEnabled(false);
        } else {
            QuantityCounter.setText("" + n1);
            D_Quantity=n1;
        }
    }
    private void setQuantity_Decrement() {
        int n2 = Integer.parseInt(QuantityCounter.getText().toString());
        if (n2 <= 0) {
            QuantityCounter.setText("1");
            QuantityCounter.setEnabled(false);
            D_Quantity=1;
        } else {
            QuantityCounter.setText("" + n2);
          D_Quantity=n2;
        }
    }

    private void PaymentViaPaytm()
    {
        //todo generate random numbers
        ActualPrice=D_Quantity*Integer.parseInt(D_Price);
        Intent paytmIntent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putDouble("nativeSdkForMerchantAmount", ActualPrice);
        bundle.putString("orderid", "1111");
        bundle.putString("txnToken", "123");
        bundle.putString("mid", "");

        paytmIntent.setComponent(new ComponentName("net.one97.paytm", "net.one97.paytm.AJRJarvisSplash"));
        paytmIntent.putExtra("paymentmode", 2); // You must have to pass hard coded 2 here, Else your transaction would not proceed.
        paytmIntent.putExtra("bill", bundle);
        startActivityForResult(paytmIntent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            if (resultCode==0)
            {
                Toast.makeText(getApplicationContext(),"Payment Failed",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            else if (resultCode==1)
            {
                Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show();
                storePurchaseDetailsInFireBase();
            }
        }
    }

    private void storePurchaseDetailsInFireBase()
    {
        D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders=new D_PreviousOrdersAndPresentInCartOrders(D_ProductGender,D_ImageCategory,D_Image,D_ProductTitle,D_ProductDescription,D_Price,""+D_Size,""+D_Quantity);
        new StorePurchaseDetailsInFireBase().execute(d_previousOrdersAndPresentInCartOrders);
    }
    private void setBookmarkStatus(boolean isBookMark) {
        if (isBookMark==false)
        {
            D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders=new D_PreviousOrdersAndPresentInCartOrders(D_ProductGender,D_ImageCategory,D_Image,D_ProductTitle,D_ProductDescription,D_Price,""+ D_Size);
            new StoreBookmarkDetailsInFireBase().execute(d_previousOrdersAndPresentInCartOrders);
        }
    }
    private void setAddToCart()
    {
        D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders=new D_PreviousOrdersAndPresentInCartOrders(D_ProductGender,D_ImageCategory,D_Image,D_ProductTitle,D_ProductDescription,D_Price,""+ D_Size);
        new StoreAddToCartDetailsInFireBase().execute(d_previousOrdersAndPresentInCartOrders);
    }

   class StorePurchaseDetailsInFireBase extends AsyncTask<D_PreviousOrdersAndPresentInCartOrders,Void,Boolean> {

        Boolean isSuccess=false;
       @Override
       protected Boolean doInBackground(D_PreviousOrdersAndPresentInCartOrders... d_previousOrdersAndPresentInCartOrders) {

           D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders1=d_previousOrdersAndPresentInCartOrders[0];

           DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyOrders").push();
                   databaseReference.setValue(d_previousOrdersAndPresentInCartOrders1).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                       isSuccess=true;
                   else
                       isSuccess=false;
               }
           });

           return isSuccess;
       }

       @Override
       protected void onPostExecute(Boolean aBoolean) {
           super.onPostExecute(aBoolean);
           if (aBoolean)
               Toast.makeText(CompleteViewOfProduct.this, "Updated Your Purchase details Successfully", Toast.LENGTH_SHORT).show();
           else
               Toast.makeText(CompleteViewOfProduct.this, "Unable to Update Your Purchase details", Toast.LENGTH_SHORT).show();
       }
   }
   
   class StoreAddToCartDetailsInFireBase extends AsyncTask<D_PreviousOrdersAndPresentInCartOrders,Void,Boolean>
   {
      boolean isSuccess=false;
       @Override
       protected Boolean doInBackground(D_PreviousOrdersAndPresentInCartOrders... d_previousOrdersAndPresentInCartOrders) {
           
           D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders1=d_previousOrdersAndPresentInCartOrders[0];
           DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyCart").push();
           
           databaseReference.setValue(d_previousOrdersAndPresentInCartOrders1).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                       isSuccess=true;
                   else
                       isSuccess=false;
               }
           });
           return isSuccess;
       }

       @Override
       protected void onPostExecute(Boolean aBoolean) {
           super.onPostExecute(aBoolean);
           if (aBoolean)
               Toast.makeText(CompleteViewOfProduct.this, "Item Added to Cart successfully", Toast.LENGTH_SHORT).show();
           else
               Toast.makeText(CompleteViewOfProduct.this, "Unable to add item to cart", Toast.LENGTH_SHORT).show();
       }
   }

   class StoreBookmarkDetailsInFireBase extends AsyncTask<D_PreviousOrdersAndPresentInCartOrders,Void,Boolean>
   {
      boolean isSuccess=false;
       @Override
       protected Boolean doInBackground(D_PreviousOrdersAndPresentInCartOrders... d_previousOrdersAndPresentInCartOrders) {
           D_PreviousOrdersAndPresentInCartOrders d_previousOrdersAndPresentInCartOrders1=d_previousOrdersAndPresentInCartOrders[0];

           DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MyWishList").push();
                  databaseReference.setValue(d_previousOrdersAndPresentInCartOrders1).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful())
                          {
                              isSuccess=true;
                          }
                          else
                              isSuccess=false;
                      }
                  });


           return isSuccess;
       }

       @Override
       protected void onPostExecute(Boolean aBoolean) {
           super.onPostExecute(aBoolean);
           if (aBoolean)
           {
               Toast.makeText(CompleteViewOfProduct.this, "Added to wishList SuccessFully", Toast.LENGTH_SHORT).show();
              onBackPressed();
           }
           else
           {
               Toast.makeText(CompleteViewOfProduct.this, "Unable to BookMark sorry", Toast.LENGTH_SHORT).show();
           }
       }
   }
}
