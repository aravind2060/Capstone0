package com.example.capstone0.BottomNavigationThings.CustomShoes;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.capstone0.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDesignShoe extends Fragment {


    public CustomDesignShoe() {
        // Required empty public constructor
    }

    WebView webView;
    Button buynow;
    Bitmap bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_custom_design_shoe, container, false);
        setWebView(view);
        setBuynow(view);
      return view;
     }

     private void setWebView(View view)
     {
         webView=view.findViewById(R.id.WebView_custom);
         webView.loadUrl("https://design.milople.com/capstone/store/product/1/11");
         WebSettings webSettings=webView.getSettings();
         webSettings.setJavaScriptEnabled(true);
         webView.setWebViewClient(new WebViewClient(){
             @Override
             public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                 String url=request.getUrl().toString();
                 webView.loadUrl(url);
                 return true;
             }
             @Override
             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                 super.onPageStarted(view, url, favicon);
             }

             @Override
             public void onPageFinished(WebView view, String url) {
                 super.onPageFinished(view, url);
             }

         });
         webView.setWebChromeClient(new WebChromeClient()
         {
             @Override
             public void onProgressChanged(WebView view, int newProgress) {
                 super.onProgressChanged(view, newProgress);
                 //progressBar.setProgress(newProgress);
             }

             @Override
             public void onReceivedTitle(WebView view, String title) {
                 super.onReceivedTitle(view, title);
                 //toolbar.setTitle(title);
             }

             @Override
             public void onReceivedIcon(WebView view, Bitmap icon) {
                 super.onReceivedIcon(view, icon);
             }
         });
     }

     private void setBuynow(View view)
     {
         buynow=view.findViewById(R.id.BuyNow_custom);
         final RelativeLayout relativeLayout=view.findViewById(R.id.mainlayout);
         buynow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 relativeLayout.setDrawingCacheEnabled(true);
                 relativeLayout.buildDrawingCache(true);
                 bitmap= Bitmap.createBitmap(relativeLayout.getDrawingCache());
                 relativeLayout.setDrawingCacheEnabled(false);
                 //uploadImageToFireBase(bitmap);
                 navigateToBuyNowPage(bitmap);
                 Log.e("CustomShoe","Inside onclick Of CustomShoe");
             }
         });

     }

     private void uploadImageToFireBase(Bitmap bitmap)
     {
         if (bitmap!=null)
         {
             ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
             bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
             byte[] data=byteArrayOutputStream.toByteArray();
//             UploadTask uploadTask=storageReference.putBytes(data);
//             uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                 @Override
//                 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                     if (task.isSuccessful()) {
//                         Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
//                         Log.e("CustomShoe", "Image Uploaded");
//                     } else
//                     {
//                         Toast.makeText(getContext(), "Unable to upload image", Toast.LENGTH_SHORT).show();
//                          Log.e("CustomShoe", "Image not Uploaded");
//                     }
//                 }
//             }).addOnFailureListener(new OnFailureListener() {
//                 @Override
//                 public void onFailure(@NonNull Exception e) {
//                     Log.e("CustomShoe",e.getMessage());
//                 }
//             });

         }
         else
             Log.e("CustomShoe","bitmap is null");
     }

     private void navigateToBuyNowPage(Bitmap bitmap1)
     {
         Intent intent=new Intent(getContext(),CustomDesignBuyNowPage.class);
         //intent.putExtra("BitmapImage",bitmap1);
         intent.putExtra("CustomProductTitle","FirstStep Shoe");
         intent.putExtra("CustomDescription","FirstStep Provides best shoe");
         intent.putExtra("CustomPrice","1200");
         startActivity(intent);
     }
}
