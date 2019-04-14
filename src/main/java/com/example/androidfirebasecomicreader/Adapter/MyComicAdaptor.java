package com.example.androidfirebasecomicreader.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidfirebasecomicreader.Model.Comic;
import com.example.androidfirebasecomicreader.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyComicAdaptor extends RecyclerView.Adapter<MyComicAdaptor.MyViewHolder> {

    Context context;
    List<Comic> comicList;
    LayoutInflater inflater;

    public MyComicAdaptor(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.comic_item,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Picasso.get().load(comicList.get(i).Image).into(myViewHolder.comic_image);
        myViewHolder.comic_name.setText(comicList.get(i).Name);
    }

    @Override
    public int getItemCount() {
        return comicList.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView comic_name;
        ImageView comic_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            comic_image = (ImageView)itemView.findViewById(R.id.image_comic);
            comic_name = (TextView) itemView.findViewById(R.id.comic_name);
        }
    }
}
