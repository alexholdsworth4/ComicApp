//Class: MainActivity
// Author: B5017070
// Purpose: Controls functionality for the home page opened after splash screen.
//          The user can swipe through banner images, select a comic to read or
//          press search icon to filter/search for a specific comic.

package com.example.androidfirebasecomicreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

    //Home page fields
    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_comic;
    TextView txt_comic;
    ImageView btn_filter_search;

    //The images used in the banner and comics are referenced from Firebase database.
    DatabaseReference banners, comics;

    //Declaring Listener fields
    IBannerLoadDone bannerListener;
    IComicLoadDone comicListener;

    //Declaring alert dialogs for Main Activity
    android.app.AlertDialog alertDialog;

    //Used to retrieve and save the current state of comics and banners loaded on this page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialising Database- will look for references of 'Comic' and 'Banners' in Firebase.
        //These will be displayed on home page in appropriate sections.
        comics = FirebaseDatabase.getInstance().getReference("Comic");
        banners = FirebaseDatabase.getInstance().getReference("Banners");


        //Initialising event listeners
        bannerListener = this;
        comicListener = this;

        //In the top right of page will be a search icon
        btn_filter_search = (ImageView) findViewById(R.id.btn_show_filter_search);
        btn_filter_search.setOnClickListener(new View.OnClickListener() {
            //When the icon is pressed takes user to filter/search page.
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FilterSearchActivity.class));
            }
        });

        slider = findViewById(R.id.slider);
        Slider.init(new PicassoLoadingService());

        //The user can refresh contents of page with vertical swipe gesture
        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //When the swipe gesture is done- the comics and banners should be reloaded
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
        //The comics should take the layout specified in activity_main.xml
        recycler_comic = findViewById(R.id.recycler_comic);
        recycler_comic.setHasFixedSize(true);
        //The comics take a grid format, and should have 2 comics to fill width of screen
        recycler_comic.setLayoutManager(new GridLayoutManager(this, 2));
        //At the bottom of each comic image, will be a text box with comic title in
        txt_comic = findViewById(R.id.txt_comic);
    }

    //Method which loads the banner which appears in top half of screen
    private void loadBanner() {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //The banner will be an array of images. These images will be referenced in Firebase.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bannerList = new ArrayList<>();
                //The DataSnapshot instance contains data from my Firebase database
                for (DataSnapshot bannerSnapShot : dataSnapshot.getChildren()) {
                    String image = bannerSnapShot.getValue(String.class);
                    bannerList.add(image);
                }

                //Call the banner listener
                bannerListener.onBannerLoadDoneListener(bannerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method which loads the list of comics which appears in bottom half of screen
    private void loadComic() {
        //The list of comics may be big and take a while to load.
        //Have included SpotsDialog to indicate to user the application hasn't crashed.
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .setMessage("Loading...")
                .build();

        if (!swipeRefreshLayout.isRefreshing())
            alertDialog.show();

        //The comic ArrayList
        comics.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Comic> comic_load = new ArrayList<>();

            @Override
            //The comics will be an array of images. These images will be referenced in Firebase.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot comicSnapShot : dataSnapshot.getChildren()) {
                    Comic comic = comicSnapShot.getValue(Comic.class);
                    comic_load.add(comic);
                }

                comicListener.onComicLoadDoneListener(comic_load);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //When the banner is done loading, set the MySlideAdapter
    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
        slider.setAdapter(new MySlideAdapter(banners));
    }

    //When the comics are done loading, list all the comics into the RecyclerView
    @Override
    public void onComicLoadDoneListener(List<Comic> comicList) {
        Common.comicList = comicList;

        recycler_comic.setAdapter(new MyComicAdaptor(getBaseContext(), comicList));

        //The banner and comics will be split by a sub-title
        //The sub-title will state how many comics are in the application
        txt_comic.setText(new StringBuilder("COMIC BOOK COLLECTION (")
                .append(comicList.size())
                .append(")"));

        if (!swipeRefreshLayout.isRefreshing())
            alertDialog.dismiss();
    }
}
