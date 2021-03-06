package com.example.androidfirebasecomicreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidfirebasecomicreader.ChaptersActivity;
import com.example.androidfirebasecomicreader.Common.Common;
import com.example.androidfirebasecomicreader.Interface.IRecyclerItemClickListener;
import com.example.androidfirebasecomicreader.Model.Chapter;
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

        //Event
        myViewHolder.setRecyclerItemClickListener(new IRecyclerItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                //Save comic selected
                Common.comicSelected = comicList.get(position);
                context.startActivity(new Intent(context, ChaptersActivity.class));
            }

            @Override
            public void onClick(View view, int adapterPosition) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return comicList.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView comic_name;
        ImageView comic_image;

        IRecyclerItemClickListener recyclerItemClickListener;

        public void setRecyclerItemClickListener(IRecyclerItemClickListener recyclerItemClickListener) {
            this.recyclerItemClickListener = recyclerItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            comic_image = (ImageView)itemView.findViewById(R.id.image_comic);
            comic_name = (TextView) itemView.findViewById(R.id.comic_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           recyclerItemClickListener.onClick(view,getAdapterPosition());
        }
    }
}
