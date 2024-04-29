
package com.example.hospital_management_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class searchdocsadapter extends RecyclerView.Adapter<searchdocsadapter.MyViewHolder>
{
    String[] arr_names;
    int[] arr_imgs;

    Context context;
        public searchdocsadapter(int[] arr_imgs, String[] arr_names){
            this.arr_imgs = arr_imgs;
            this.arr_names = arr_names;
        }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      context=parent.getContext();
      LayoutInflater inflater =  LayoutInflater.from(context);
     View view= inflater.inflate(R.layout.single_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // holder.img.setImageResource(Integer.parseInt(arr_imgs[position]));
       holder.img.setImageResource(arr_imgs[position]);

       // String imageUrl = arr_imgs[position];
      //  Picasso.get().load(imageUrl).into(holder.img);

        holder.name.setText(arr_names[position]);
        searchdocsanimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return arr_imgs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

         private ImageView img;
         private TextView name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img=(ImageView) itemView.findViewById(R.id.image_View_single_item);
            name=(TextView) itemView.findViewById(R.id.name_single_item);


        }
    }

    public void searchdocsanimation(View view)
    {
        Animation animation=AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
        view.startAnimation(animation);
    }

}



