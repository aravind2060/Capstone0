package com.example.capstone0.BottomNavigationThings.MenShoes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DisplayAProduct extends Fragment{

    ArrayList<D_ShoesDataFromInternet> arrayListFormal;
    Context context;
    String ImageCategorye;
    MyAdapter myAdapter;
     DisplayAProduct(Context context1,ArrayList<D_ShoesDataFromInternet> arrayList1,String imageCategorye)
     {
        arrayListFormal=arrayList1;
        context=context1;
        ImageCategorye=imageCategorye;
     }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         View view=LayoutInflater.from(context).inflate(R.layout.fragment_to_display,container,false);
        setListView(view);
        return view;
    }

    private void setListView(View view) {
        ListView listView=view.findViewById(R.id.ListViewToDisplayProducts);
        myAdapter=new MyAdapter(context,R.layout.single_view_for_label_of_shoe,arrayListFormal,ImageCategorye);
        listView.setAdapter(myAdapter);
    }
   public void setDataModifiedChangeListener()
   {
       myAdapter.notifyDataSetChanged();
   }



    class MyAdapter extends ArrayAdapter<D_ShoesDataFromInternet>
   {
       ArrayList<D_ShoesDataFromInternet> d_shoesDataFromInternets;
       Context context1;
       String ImageCategory;
       StorageReference storageReference;
       Uri downloadableuri=null;
       public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<D_ShoesDataFromInternet> objects,String ImageCategory) {
           super(context, resource, objects);
           d_shoesDataFromInternets=objects;
           context1=context;
           this.ImageCategory=ImageCategory;
           storageReference=FirebaseStorage.getInstance().getReference("MenFootWear").child(ImageCategory);
       }
        private class ViewHolder
        {
            ImageView imageView;
            TextView ProductTitle,PriceOfProduct;
        }
       @NonNull
       @Override
       public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
           if (convertView==null)
           {
               viewHolder=new ViewHolder();
               convertView=LayoutInflater.from(context1).inflate(R.layout.single_view_for_label_of_shoe,parent,false);
               viewHolder.imageView=convertView.findViewById(R.id.SingleViewForLabelOfShoe_ImageView);
               viewHolder.PriceOfProduct=convertView.findViewById(R.id.SingleViewForLabelOfShoe_Price);
               viewHolder.ProductTitle=convertView.findViewById(R.id.SingleViewForLabelOfShoe_Name);
           }else
           {
               viewHolder= (ViewHolder) convertView.getTag();
           }

           if(position<d_shoesDataFromInternets.size()) {
               ImageView imageButton = convertView.findViewById(R.id.SingleViewForLabelOfShoe_ImageView);
               TextView ProductTitle = convertView.findViewById(R.id.SingleViewForLabelOfShoe_Name);
               TextView PriceOfProduct = convertView.findViewById(R.id.SingleViewForLabelOfShoe_Price);
               storageReference.child(ImageCategory+(position+1)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       downloadableuri=uri;
                       Log.e("DisplayAProduct","DownloadProduct: "+downloadableuri);
                   }
               });
               Picasso.get().load(downloadableuri).into(imageButton);
               ProductTitle.setText(d_shoesDataFromInternets.get(position).ProductTitleOfShoe);
               PriceOfProduct.setText(d_shoesDataFromInternets.get(position).ProductPriceOfShoe);
           }
           return convertView;
       }

       @Override
       public int getCount() {
           Log.e("Display","Size: "+d_shoesDataFromInternets.size());
           return d_shoesDataFromInternets.size();
       }

   }




}
