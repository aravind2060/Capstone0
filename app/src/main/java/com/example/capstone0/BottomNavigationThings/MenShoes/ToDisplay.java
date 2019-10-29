package com.example.capstone0.BottomNavigationThings.MenShoes;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.capstone0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToDisplay extends Fragment {

    static int index;
    ArrayList<D_ShoesDataFromInternet> FormalShoesList;
    ArrayList<D_ShoesDataFromInternet> SneakersShoesList;
    ArrayList<D_ShoesDataFromInternet> SportsShoesList;
    ArrayList<D_ShoesDataFromInternet> SmartShoesList;
    ArrayList<D_ShoesDataFromInternet> EthnicShoesList;
    ArrayList<D_ShoesDataFromInternet> CasualShoesList;

    ArrayList<ProductDetails> productDetails =new ArrayList<>();
    ArrayList<File>Images=new ArrayList<>();
    RecyclerView recyclerView;
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("MenFootWear");
    StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("MenFootWear");

    public static ToDisplay getInstance(int indexx)
   {
       index=indexx;
       return new ToDisplay();
   }
    public ToDisplay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_to_display, container, false);
          recyclerView=view.findViewById(R.id.RecyclerViewToDisplay);
          switch (index)
          {
              case 1:
                getProductName();
                recyclerView.setAdapter(new MyAdapter(FormalShoesList));
                break;
              case 2:
                  getProductName();
                  recyclerView.setAdapter(new MyAdapter(SneakersShoesList));
                  break;
              case 3:
                  getProductName();
                  recyclerView.setAdapter(new MyAdapter(SportsShoesList));
                  break;
              case 4:
                  getProductName();
                  recyclerView.setAdapter(new MyAdapter(SmartShoesList));
                  break;
              case 5:
                  getProductName();
                  recyclerView.setAdapter(new MyAdapter(EthnicShoesList));
                  break;
              case 6:
                  getProductName();
                  recyclerView.setAdapter(new MyAdapter(CasualShoesList));
                  break;

          }
     return view;
   }


    private class MyAdapter extends RecyclerView.Adapter<ViewHolderClass> {


        ArrayList<D_ShoesDataFromInternet> arrayList;
        public MyAdapter(ArrayList<D_ShoesDataFromInternet> arrayList) {
            this.arrayList = arrayList;
        }
        @NonNull
        @Override
        public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolderClass(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_for_label_of_shoe,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {

          holder.title.setText(arrayList.get(position).ProductTitle);
          holder.price.setText(arrayList.get(position).Price);
          holder.productDescription.setText(arrayList.get(position).ProductDescription);
            Bitmap bitmap=BitmapFactory.decodeFile(""+arrayList.get(position).Image);
            holder.image.setImageBitmap(bitmap);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private class ViewHolderClass extends RecyclerView.ViewHolder {

        ImageButton image;
        TextView price,title,productDescription;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.SingleViewForLabelOfShoe_ImageView);
            price=itemView.findViewById(R.id.SingleViewForLabelOfShoe_Price);
            title=itemView.findViewById(R.id.SingleViewForLabelOfShoe_Name);
            productDescription=itemView.findViewById(R.id.ProductDescription);
        }
    }

    private void getProductName()
    {
        StringBuilder RootTag=new StringBuilder();
        switch (index)
        {
            case 1:
                RootTag.append("Formal");
                break;
            case 2:
                RootTag.append("Sneakers");
                break;
            case 3:
                RootTag.append("Sports");
                break;
            case 4:
                RootTag.append("Smart");
                break;
            case 5:
                RootTag.append("Ethnic");
                break;
            case 6:
                RootTag.append("Casual");
                break;
        }
        DatabaseReference parentOfSpecificChild=databaseReference.child(RootTag.toString());

        for (int i=1;i<=6;i++)
        {
            parentOfSpecificChild.child(""+i).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productDetails.add(dataSnapshot.getValue(ProductDetails.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
       getImages();
    }

    private void getImages()
    {
        StringBuilder ImageSpecificParent=new StringBuilder();
        switch(index)
        {
            case 1:
                ImageSpecificParent.append("Formal");
                break;
            case 2:
                ImageSpecificParent.append("Sneakers");
                break;
            case 3:
                ImageSpecificParent.append("Sports");
                break;
            case 4:
                ImageSpecificParent.append("Smart");
                break;
            case 5:
                ImageSpecificParent.append("Ethnic");
                break;
            case 6:
                ImageSpecificParent.append("Casual");
                break;
        }
       StorageReference storageReference1= storageReference.child(ImageSpecificParent.toString());
        for (int i=1;i<=6;i++)
        {

            try {
                 final File image=File.createTempFile(ImageSpecificParent.toString()+""+i,"jpg");
                storageReference1.getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Images.add(image);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        setDataIntoDataClass();
    }

    private void setDataIntoDataClass()
    {

        switch (index)
        {
            case 1:
              FormalShoesList=new ArrayList<>();
              break;
            case 2:
                SneakersShoesList=new ArrayList<>();
                break;
            case 3:
                SportsShoesList=new ArrayList<>();
                break;
            case 4:
                SmartShoesList=new ArrayList<>();
                break;
            case 5:
                EthnicShoesList=new ArrayList<>();
                break;
            case 6:
                CasualShoesList=new ArrayList<>();
                break;
        }
        setArrayListData();
    }
    private void setArrayListData()
    {
       switch (index)
       {
           case 1:
               for (int i=1;i<=6;i++)
               FormalShoesList.add(new D_ShoesDataFromInternet(Images.get(i),productDetails.get(i).ProductTitle,productDetails.get(i).Price,productDetails.get(i).ProductDescription));
               break;
           case 2:
               for (int i=1;i<=6;i++)
                   SneakersShoesList.add(new D_ShoesDataFromInternet(Images.get(i),productDetails.get(i).ProductTitle,productDetails.get(i).Price,productDetails.get(i).ProductDescription));
               break;
           case 3:
               for (int i=1;i<=6;i++)
                   SportsShoesList.add(new D_ShoesDataFromInternet(Images.get(i),productDetails.get(i).ProductTitle,productDetails.get(i).Price,productDetails.get(i).ProductDescription));
               break;
           case 4:
               for (int i=1;i<=6;i++)
                   SmartShoesList.add(new D_ShoesDataFromInternet(Images.get(i),productDetails.get(i).ProductTitle,productDetails.get(i).Price,productDetails.get(i).ProductDescription));
               break;
           case 5:
               for (int i=1;i<=6;i++)
                   EthnicShoesList.add(new D_ShoesDataFromInternet(Images.get(i),productDetails.get(i).ProductTitle,productDetails.get(i).Price,productDetails.get(i).ProductDescription));
               break;
           case 6:
               for (int i=1;i<=6;i++)
                   CasualShoesList.add(new D_ShoesDataFromInternet(Images.get(i),productDetails.get(i).ProductTitle,productDetails.get(i).Price,productDetails.get(i).ProductDescription));
               break;

       }
    }


  class ProductDetails
  {
      String Price;
      String ProductTitle;
      String ProductDescription;
      public ProductDetails(String price, String productTitle,String productDescription) {
          Price = price;
          ProductTitle = productTitle;
          ProductDescription=productDescription;
      }
  }
}
