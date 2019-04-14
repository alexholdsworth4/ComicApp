package com.example.androidfirebasecomicreader.Adapter;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MySlideAdapter extends SliderAdapter {
    private List<String> imageList;

    public MySlideAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
            imageSlideViewHolder.bindImageSlide(imageList.get(position));

    }
}
