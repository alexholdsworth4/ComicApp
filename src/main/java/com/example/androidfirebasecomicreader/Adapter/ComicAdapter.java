/*
Class: ComicAdapter
Author: B5017070
Purpose: Provide default implementation of all methods
in comic event listener interface.
*/

package com.example.androidfirebasecomicreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidfirebasecomicreader.ChapterActivity;
import com.example.androidfirebasecomicreader.Common.Common;
import com.example.androidfirebasecomicreader.Interface.IRecyclerItemClickListener;
import com.example.androidfirebasecomicreader.Model.Comic;
import com.example.androidfirebasecomicreader.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.MyViewHolder> {

    //ComicAdapter fields
    Context context;
    List<Comic> comicList;
    LayoutInflater inflater;

    public ComicAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    //Use layout specified in comic_item.xml
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int v) {
        View itemView = inflater.inflate(R.layout.comic_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    //For scrolling grid list so view is reused by binding new comics to list
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int v) {
        Picasso.get().load(comicList.get(v).Image).into(myViewHolder.comic_image);
        myViewHolder.comic_name.setText(comicList.get(v).Name);

        //Comic becomes 'selected' when pressed and loads Chapter page
        myViewHolder.setRecyclerItemClickListener(new IRecyclerItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                //Save the comic that has been selected
                Common.comicSelected = comicList.get(position);
                context.startActivity(new Intent(context, ChapterActivity.class));
            }

            @Override
            public void onClick(View view, int adapterPosition) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ViewHolder fields
        TextView comic_name;
        ImageView comic_image;

        IRecyclerItemClickListener recyclerItemClickListener;


        public void setRecyclerItemClickListener(IRecyclerItemClickListener recyclerItemClickListener) {
            this.recyclerItemClickListener = recyclerItemClickListener;
        }

        //Each comic with have an image and name (comic book title)
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //The layouts of each comic book in RecyclerView are specified in
            comic_image = itemView.findViewById(R.id.comic_image);
            comic_name = itemView.findViewById(R.id.comic_name);

            itemView.setOnClickListener(this);
        }

        @Override
        //When clicked, retrieve the current position of the comic adapter
        public void onClick(View v) {
            recyclerItemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
