package com.example.capstone0.BottomNavigationThings.MenShoes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.capstone0.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DisplayAProduct extends Fragment {

    ArrayList<D_ShoesDataFromInternet> arrayList;
    Context context;
    String ImageCategorye;
     DisplayAProduct(Context context1,ArrayList<D_ShoesDataFromInternet> arrayList1,String imageCategorye)
     {
        arrayList=arrayList1;
        context=context1;
        ImageCategorye=imageCategorye;
     }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         View view=LayoutInflater.from(context).inflate(R.layout.fragment_to_display,container,false);
         RecyclerView recyclerView=view.findViewById(R.id.RecyclerViewToDisplay);

         recyclerView.setAdapter(new MyAdapter(arrayList,ImageCategorye,context));

        return view;
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolderClass>
    {
        ArrayList<D_ShoesDataFromInternet> arrayList;
        Context context;
        String ImageCategory;
        private MyAdapter(ArrayList<D_ShoesDataFromInternet> arrayList,String imageCategory,Context context1) {
            this.ImageCategory=imageCategory;
            this.arrayList = arrayList;
            this.context=context1;
        }
        @NonNull
        @Override
        public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolderClass(LayoutInflater.from(context).inflate(R.layout.single_view_for_label_of_shoe,parent,true));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
            holder.title.setText(arrayList.get(position).ProductTitle);
            holder.price.setText(arrayList.get(position).Price);
            holder.productDescription.setText(arrayList.get(position).ProductDescription);
            Glide.with(context).load(holder.storageReference.child(ImageCategory+(position+1))).into(holder.image);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        private class ViewHolderClass extends RecyclerView.ViewHolder {

            ImageButton image;
            TextView price,title,productDescription;
            StorageReference storageReference;
            public ViewHolderClass(@NonNull View itemView)
            {
                super(itemView);
                image=itemView.findViewById(R.id.SingleViewForLabelOfShoe_ImageView);
                price=itemView.findViewById(R.id.SingleViewForLabelOfShoe_Price);
                title=itemView.findViewById(R.id.SingleViewForLabelOfShoe_Name);
                productDescription=itemView.findViewById(R.id.ProductDescription);
                storageReference= FirebaseStorage.getInstance().getReference("MenFootWear").child(ImageCategory);
            }
        }
    }


}
