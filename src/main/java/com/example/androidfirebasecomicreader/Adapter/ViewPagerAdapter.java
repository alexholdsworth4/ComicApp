/*
Class: ViewPagerAdapter
Author: B5017070
Purpose: Provide default implementation of all methods
in pager viewer event listener interface.
*/

package com.example.androidfirebasecomicreader.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidfirebasecomicreader.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    //ViewPagerAdapter fields
    Context context;
    List<String> imageLinks;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context, List<String> imageLinks) {
        this.context = context;
        this.imageLinks = imageLinks;
        inflater = LayoutInflater.from(context);
    }

    @Override
    //Returns the number of images (pages) that the comic contains
    public int getCount() {
        return imageLinks.size();
    }

    @Override
    //Implemented to return the view always an object
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    //A matching implementation removes the view from parent ViewGroup
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    //Calls are made to load images in the page adapter
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //Uses layout specified in activity_view_comic.xml
        View image_layout = inflater.inflate(R.layout.view_pager_item, container, false);

        PhotoView pager_image = image_layout.findViewById(R.id.pager_image);
        Picasso.get().load(imageLinks.get(position)).into(pager_image);

        container.addView(image_layout);
        return image_layout;
    }
}
