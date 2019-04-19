/*
Class: ChapterActivity
Author: B5017070
Purpose: Controls functionality for the chapters page.
The user will be able to select a chapter in the comic selected.
And the comic reader will be opened at start of this chapter
*/

package com.example.androidfirebasecomicreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.androidfirebasecomicreader.Adapter.ChapterAdapter;
import com.example.androidfirebasecomicreader.Common.Common;
import com.example.androidfirebasecomicreader.Model.Comic;

public class ChapterActivity extends AppCompatActivity {

    //Chapter Activity Fields
    RecyclerView recycler_chapter;
    TextView txt_chapter_name;
    LinearLayoutManager layoutManager;

    @Override
    //Used to retrieve and save the current state of chapters page
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        //Text and RecyclerView for the chapters list in activity_chapters.xml
        txt_chapter_name = findViewById(R.id.txt_chapter_name);
        recycler_chapter = findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        //The toolbar will have a back arrow and comic book name
        Toolbar toolbar = findViewById(R.id.toolbar);
        //The title of the page will be the name of the comic book selected
        toolbar.setTitle(Common.comicSelected.Name);
        //Back arrow which will take user back to the MainActivity home page
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Fetch all the chapters in the comic book that has been selected
        fetchChapter(Common.comicSelected);

    }

    //The list of chapters will be a RecyclerView, using the chapters specified in Firebase
    private void fetchChapter(Comic comicSelected) {
        Common.chapterList = comicSelected.Chapters;
        recycler_chapter.setAdapter(new ChapterAdapter(this, comicSelected.Chapters));
        //Indicate to the user how many chapters are in the comic book that has been selected
        txt_chapter_name.setText(new StringBuilder("CHAPTERS (").append(comicSelected.Chapters.size()).append(")"));
    }
}
