package com.egreen2.egeen2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
    [파일명] A07_ApplyGSub.java
    [설명] 수강신청 가이드
    [작성자] 장희원
    [작성일시] 2021.01.15
 */

class A07_ApplyGSub extends RecyclerView.Adapter<A07_ApplyGSub.MyViewHolder> {
    private final Context context;
    private final String[] sliderImage;


    public A07_ApplyGSub(Context context, String[] sliderImage) {
        this.context = context;
        this.sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public A07_ApplyGSub.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.a07_apply_g_sub, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull A07_ApplyGSub.MyViewHolder holder, int position) {
        holder.bindSliderImage(sliderImage[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImage.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageSlider);
        }

        public void bindSliderImage(String imageURL) {
            Glide.with(context)
                    .load(imageURL)
                    .into(mImageView);
        }
    }
}