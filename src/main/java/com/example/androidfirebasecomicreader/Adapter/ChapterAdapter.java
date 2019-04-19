/*
Class: ChapterAdapter
Author: B5017070
Purpose: Provide default implementation of all methods
in chapter event listener interface.
*/

package com.example.androidfirebasecomicreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidfirebasecomicreader.Common.Common;
import com.example.androidfirebasecomicreader.Interface.IRecyclerItemClickListener;
import com.example.androidfirebasecomicreader.Model.Chapter;
import com.example.androidfirebasecomicreader.R;
import com.example.androidfirebasecomicreader.ViewComicActivity;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {

    //ChapterAdapter fields
    Context context;
    List<Chapter> chapterList;
    LayoutInflater inflater;

    //Context variables used to give chapterList correct properties
    public ChapterAdapter(Context context, List<Chapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    //Called to initialize Chapter ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int v) {
        View itemView = inflater.inflate(R.layout.chapter_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    //Called for each ViewHolder to bind Chapters to the adapter
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int v) {
        myViewHolder.txt_chapter_number.setText(chapterList.get(v).Name);

        myViewHolder.setRecyclerItemClickListener(new IRecyclerItemClickListener() {
            @Override
            //When Chapter is selected, look up up the chapter position
            public void OnClick(View view, int chapterPosition) {
                Common.chapterSelected = chapterList.get(chapterPosition);
                Common.chapterIndex = chapterPosition;
                //Load the comic viewing experience
                context.startActivity(new Intent(context, ViewComicActivity.class));
            }

            @Override
            public void onClick(View view, int position) {

            }
        });
    }

    @Override
    //Returns the size of the chapter list collection that is displayed
    public int getItemCount() {
        return chapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_chapter_number;
        IRecyclerItemClickListener recyclerItemClickListener;

        public void setRecyclerItemClickListener(IRecyclerItemClickListener recyclerItemClickListener) {
            this.recyclerItemClickListener = recyclerItemClickListener;
        }

        //Each chapter with have a chapter number
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_chapter_number = (TextView) itemView.findViewById(R.id.txt_chapter_number);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
