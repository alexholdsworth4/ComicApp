package com.example.androidfirebasecomicreader;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfirebasecomicreader.Adapter.MyComicAdaptor;
import com.example.androidfirebasecomicreader.Adapter.MySlideAdapter;
import com.example.androidfirebasecomicreader.Common.Common;
import com.example.androidfirebasecomicreader.Interface.IBannerLoadDone;
import com.example.androidfirebasecomicreader.Interface.IComicLoadDone;
import com.example.androidfirebasecomicreader.Model.Comic;
import com.example.androidfirebasecomicreader.Service.PicassoLoadingService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity implements IBannerLoadDone, IComicLoadDone {

    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_comic;
    TextView txt_comic;
    ImageView btn_filter_search;

    //Database
    DatabaseReference banners,comics;

    //Listener
    IBannerLoadDone bannerListener;
    IComicLoadDone comicListener;

    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Database
        banners = FirebaseDatabase.getInstance().getReference("Banners");
        comics = FirebaseDatabase.getInstance().getReference("Comic");

        //Init Listener
        bannerListener = this;
        comicListener = this;

        btn_filter_search = (ImageView)findViewById(R.id.btn_show_filter_search);
        btn_filter_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FilterSearchActivity.class));
            }
        });

        slider = (Slider)findViewById(R.id.slider);
        Slider.init(new PicassoLoadingService());

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
            R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
         @Override
           public void onRefresh() {
                loadBanner();
                loadComic();
              }
            });

        swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                 loadBanner();
                 loadComic();

                    }
                });

                recycler_comic = (RecyclerView)findViewById(R.id.recycler_comic);
                recycler_comic.setHasFixedSize(true);
                recycler_comic.setLayoutManager(new GridLayoutManager(this,2));

                txt_comic = (TextView)findViewById(R.id.txt_comic);
    }

    private void loadComic() {
        //Show Dialog
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .setMessage("Please wait...")
                .build();

        if(!swipeRefreshLayout.isRefreshing())
        alertDialog.show();

        comics.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Comic> comic_load = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot comicSnapShot:dataSnapshot.getChildren())
                {
                    Comic comic = comicSnapShot.getValue(Comic.class);
                    comic_load.add(comic);
                }

                comicListener.onComicLoadDoneListener(comic_load);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadBanner() {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String>bannerList = new ArrayList<>();
                for (DataSnapshot bannerSnapShot:dataSnapshot.getChildren())
                {
                    String image = bannerSnapShot.getValue(String.class);
                    bannerList.add(image);
                }

                //Call listener
                bannerListener.onBannerLoadDoneListener(bannerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
       slider.setAdapter(new MySlideAdapter(banners));
    }

    @Override
    public void onComicLoadDoneListener(List<Comic> comicList) {
        Common.comicList = comicList;

        recycler_comic.setAdapter(new MyComicAdaptor(getBaseContext(),comicList));

        txt_comic.setText(new StringBuilder("NEW COMIC (")
        .append(comicList.size())
        .append(")"));

        if(!swipeRefreshLayout.isRefreshing())
            alertDialog.dismiss();


    }
}
