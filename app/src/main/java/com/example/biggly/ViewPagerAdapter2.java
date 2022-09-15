package com.example.biggly;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


public class ViewPagerAdapter2 extends RecyclerView.Adapter<ViewPagerAdapter2.ViewHolder> {

    // Array of images
    // Adding images from drawable folder
    private int[] images = {R.drawable.tour1_new, R.drawable.tour2_new, R.drawable.tour3_new,R.drawable.tour4_new, R.drawable.tour5_new, R.drawable.tour6_new};
    private Context ctx;

    // Constructor of our ViewPager2Adapter class
    public ViewPagerAdapter2(Context ctx) {
        this.ctx = ctx;
    }

    // This method returns our layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.welcome_item_vieew, parent, false);
        return new ViewHolder(view);
    }

    // This method binds the screen with the view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        // This will set the images in imageview
     //   holder.images.setBackgroundResource(images[position]);

       new Handler().post(new Runnable() {
           @Override
           public void run() {

               Glide.with(ctx).load(images[holder.getAdapterPosition()]).into(holder.images);

              // holder.images.setBackgroundResource(images[holder.getAdapterPosition()]);

           }
       });

//        holder.images.setBackgroundResource(images[position]);
    }

    // This Method returns the size of the Array
    @Override
    public int getItemCount() {
        return images.length;
    }

    // The ViewHolder class holds the view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.images);
        }
    }
}