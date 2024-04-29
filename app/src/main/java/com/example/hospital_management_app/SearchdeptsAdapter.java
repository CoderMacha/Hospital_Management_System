package com.example.hospital_management_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchdeptsAdapter extends RecyclerView.Adapter<SearchdeptsAdapter.MyViewHolder>
{
    int[] dept_imgs;
    String[] dept_names;

    public SearchdeptsAdapter(int[] dept_imgs, String[] dept_names)
    {
        this.dept_imgs=dept_imgs;
        this.dept_names=dept_names;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
       View view= inflater.inflate(R.layout.dept_single_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.img.setImageResource(dept_imgs[position]);
        holder.names.setText(dept_names[position]);

    }

    @Override
    public int getItemCount() {
        return dept_imgs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView img;
        private TextView names;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img=(ImageView) itemView.findViewById(R.id.dept_image_View_single_item);
            names=(TextView) itemView.findViewById(R.id.dept_name_single_item);
        }
    }
}
