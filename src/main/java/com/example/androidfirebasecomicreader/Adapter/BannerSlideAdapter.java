/*
Class: BannerSlideAdapter
Author: B5017070
Purpose: Provide default implementation of all methods
in banner slider event listener interface.
*/

package com.example.androidfirebasecomicreader.Adapter;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class BannerSlideAdapter extends SliderAdapter {
    private List<String> imageList;

    public BannerSlideAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    //Returns the number of images (pages) that the banner contains
    public int getItemCount() {
        return imageList.size();
    }

    @Override
    //Banner slide effect from:
    // https://github.com/saeedsh92/Banner-Slider/blob/master/bannerslider/src/main/java/ss/com/bannerslider/adapters/SliderRecyclerViewAdapter.java
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(imageList.get(position));

    }
}
