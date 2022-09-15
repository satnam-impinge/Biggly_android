package com.example.biggly;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {
    Context context;
    ArrayList<FileModel> categoryList = new ArrayList<>();
    boolean isUploaded= false;

    public FilesAdapter(Context context, ArrayList<FileModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.category.setText(categoryList.get(position).file_name);
        holder.progressBar.setProgress(categoryList.get(position).progress);

        if(categoryList.get(position).progress>95){
            holder.tick.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        }else {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.tick.setVisibility(View.GONE);
        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isUploaded) {

                    categoryList.remove(position);

                    Fragment fragment = ((MainActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);

                    Fragment childFragment = fragment.getChildFragmentManager().getFragments().get(0);

                    if (childFragment instanceof SecondFragment) {
                        ((SecondFragment) childFragment).setFileList(categoryList);
                        ((SecondFragment) childFragment).removeFIle(position);
                    }

                    notifyDataSetChanged();

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        ImageView cancel,tick;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            category = itemView.findViewById(R.id.file_name);
            tick = itemView.findViewById(R.id.tick);
            progressBar = itemView.findViewById(R.id.progress_bar);
            cancel = itemView.findViewById(R.id.cross);

        }
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }
}

