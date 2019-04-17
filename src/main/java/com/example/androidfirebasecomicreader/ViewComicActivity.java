//Class: ViewComicActivity
// Author: B5017070
// Purpose: Controls functionality for the comic viewing experience

package com.example.androidfirebasecomicreader;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfirebasecomicreader.Adapter.MyViewPagerAdapter;
import com.example.androidfirebasecomicreader.Common.Common;
import com.example.androidfirebasecomicreader.Model.Chapter;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

public class ViewComicActivity extends AppCompatActivity {

    //Comic viewing fields
    TextView txt_chapter_name;
    View back, next;
    ViewPager viewPager;

    //Used to retrieve and save the current state of the comic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The toolbar at top of comic viewer controlled by this code
        setContentView(R.layout.activity_view_comic);

        viewPager = findViewById(R.id.view_pager);
        txt_chapter_name = findViewById(R.id.txt_chapter_name);

        //The next and back buttons in the header toolbar
        next = findViewById(R.id.chapter_next);
        back = findViewById(R.id.chapter_back);

        //The next button can be pressed to take user to the next chapter in the comic
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //User will be given this message when they have reached final chapter and press button
                //No functionality other than popup warning
                if (Common.chapterIndex == Common.chapterList.size() - 1)
                    Toast.makeText(ViewComicActivity.this, "This is the last chapter!", Toast.LENGTH_SHORT).show();
                else {
                    //If there is another chapter available, take the user to first page in next chapter
                    Common.chapterIndex++;
                    fetchLinks(Common.chapterList.get(Common.chapterIndex));
                }
            }
        });
        //The back button can be pressed to take user back a chapter in the comic
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            //User will be given this message when they are on the first chapter and press button
            //No functionality other than popup warning
            public void onClick(View view) {
                if (Common.chapterIndex == 0)
                    Toast.makeText(ViewComicActivity.this, "This is the first chapter!", Toast.LENGTH_SHORT).show();
                    //If there is a previous chapter available, take the user to first page in previous chapter
                else {
                    Common.chapterIndex--;
                    fetchLinks(Common.chapterList.get(Common.chapterIndex));
                }
            }
        });
        fetchLinks(Common.chapterSelected);

    }
    //Changing pages between chapters and what functionality should occur
    private void fetchLinks(Chapter chapter) {
        if (chapter.Links != null) {
            if (chapter.Links.size() > 0) {
                MyViewPagerAdapter adapter = new MyViewPagerAdapter(getBaseContext(), chapter.Links);
                viewPager.setAdapter(adapter);

                txt_chapter_name.setText(Common.formatString(Common.chapterSelected.Name));

                //Animation for book turning effect
                // Owner of BookFlipPageTransformer: https://github.com/wajahatkarim3/EasyFlipViewPager
                BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
                bookFlipPageTransformer.setScaleAmountPercent(10f);
                viewPager.setPageTransformer(true, bookFlipPageTransformer);
            } else {
                Toast.makeText(this, "No image available!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "This chapter is loading...", Toast.LENGTH_SHORT).show();
        }
    }
}