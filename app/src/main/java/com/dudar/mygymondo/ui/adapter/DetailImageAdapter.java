package com.dudar.mygymondo.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dudar.mygymondo.R;
import com.dudar.mygymondo.model.ExerciseImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailImageAdapter extends RecyclerView.Adapter<DetailImageAdapter.ImageViewHolder> {

    private List<ExerciseImage> imageList;

    public DetailImageAdapter(List<ExerciseImage> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_image_item,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int position) {
        ImageView image = imageViewHolder.imageView;
        Picasso.get().load(imageList.get(position).getImage()).fit().into(image);
    }

    @Override
    public int getItemCount() {
        return imageList.isEmpty() ? 0 : imageList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgRecyclerItem);
        }


    }
}
