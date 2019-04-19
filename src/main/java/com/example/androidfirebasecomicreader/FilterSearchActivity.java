/*
Class: FilterSearchActivity
Author: B5017070
Purpose: Controls functionality for the filter and search page.
The user can press either icon to find a specific comic.
Search will match the users query with comic titles.
Filter will match the users query with comic genres.
*/

package com.example.androidfirebasecomicreader;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfirebasecomicreader.Adapter.ComicAdapter;
import com.example.androidfirebasecomicreader.Common.Common;
import com.example.androidfirebasecomicreader.Model.Comic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {

    //Filter and Search Activity Fields
    BottomNavigationView bottomNavigationView;
    RecyclerView recycler_filter_search;

    //Used to retrieve and save the current state of filter/search page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        //All filter and search results should be formatted in a grid
        // RecyclerView layout specified in activity_filter_search
        recycler_filter_search = findViewById(R.id.recycler_filter_search);
        //The grid should be two comics wide- filling the full width of the screen
        recycler_filter_search.setHasFixedSize(true);
        recycler_filter_search.setLayoutManager(new GridLayoutManager(this, 2));

        //Functionality for the navigation bar at the bottom of the screen
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            //When an icon is pressed, the specified action needs to take place
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //When filter icon pressed- open filter dialog
                    case R.id.action_filter:
                        showFilterDialog();
                        break;
                    //When search icon pressed- open search dialog
                    case R.id.action_search:
                        showSearchDialog();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    //This method controls how the search dialog popup behaves
    private void showSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterSearchActivity.this);
        //Search title at top of dialog to remind user what function has been pressed
        alertDialog.setTitle("Search");

        final LayoutInflater inflater = this.getLayoutInflater();
        View search_layout = inflater.inflate(R.layout.dialog_search, null);
        //Use layout specified in dialog_search.xml
        //This is the text box the user will type their search query
        final EditText txt_search = search_layout.findViewById(R.id.txt_search);

        //The cancel button will drop the dialog and no further action will be taken
        alertDialog.setView(search_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

            }
        });
        //The search button will look through Firebase at all comics
        alertDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            //All comics with a characters in title that match the search query will be shown
            public void onClick(DialogInterface dialog, int d) {
                fetchSearchComic(txt_search.getText().toString());
            }
        });
        //Show dialog message
        alertDialog.show();
    }

    //This method controls how the filter dialog popup behaves
    private void showFilterDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(FilterSearchActivity.this);
        //Select Category title to remind user they need to select category to filter
        alert.setTitle("Filter");

        final LayoutInflater inflater = this.getLayoutInflater();
        //Use layout specified in dialog_filter.xml
        View filter_layout = inflater.inflate(R.layout.dialog_filter, null);
        //This is the text box that will automatically complete as category names are typed
        final AutoCompleteTextView txt_filter = filter_layout.findViewById(R.id.txt_filter);
        final ChipGroup chipGroup = filter_layout.findViewById(R.id.chipGroup);

        //Creates the autocomplete functionality using Category names listed in Common class
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, Common.filter_categories);
        txt_filter.setAdapter(adapter);

        txt_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txt_filter.setText("");

                //Creates the chip tags found at bottom of dialog
                //These chips will be categories the user has filtered
                //Layout specified in chip_item.xml
                Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                chip.setText(((TextView) view).getText());
                //The 'X' button can be pressed to remove a chip filter
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chipGroup.removeView(view);

                    }
                });
                chipGroup.addView(chip);
            }
        });
        alert.setView(filter_layout);
        //The cancel button will drop the dialog and no further action will be taken
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int d) {
                //Drop the dialog
                dialog.dismiss();

            }
        });
        //The filter button will look through Firebase at all comics
        alert.setPositiveButton("FILTER",new DialogInterface.OnClickListener() {
            @Override
            //In Firebase, each comic will have its genre categories listed with it
            public void onClick(DialogInterface dialog, int d) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query = new StringBuilder("");
                for (int c = 0; c < chipGroup.getChildCount(); c++) {
                    Chip chip = (Chip) chipGroup.getChildAt(c);
                    filter_key.add(chip.getText().toString());
                }
                //In Firebase, Category will be sorted from A to Z
                //And categories will be split by a comma
                //Filter_key needs to sort be sorted
                Collections.sort(filter_key);
                //Covert list of categories from Firebase to string
                for (String key : filter_key) {
                    filter_query.append(key).append(",");
                }
                //Remove the last comma from the string
                filter_query.setLength(filter_query.length() - 1);

                //Filter the query against the string produced
                fetchFilterCategory(filter_query.toString());
            }
        });
        alert.show();

    }

    //Once Search query has been completed retrieve list of comics that match query
    private void fetchSearchComic(String query) {
        List<Comic> comic_search = new ArrayList<>();
        for (Comic comic : Common.comicList) {
            //If any of the comic's name characters appear in the query
            if (comic.Name.contains(query))
                //Fetch this comic and add it to the list of comics shown
                comic_search.add(comic);
        }
        // If any comics are found in search, display them using the RecyclerView
        if (comic_search.size() > 0)
            recycler_filter_search.setAdapter(new ComicAdapter(getBaseContext(), comic_search));
        else
            //If no comics are found, generate popup message alerting user of this
            Toast.makeText(this, "No results found!", Toast.LENGTH_SHORT).show();
    }

    //Once Filter query has been completed retrieve list of comics that match query
    private void fetchFilterCategory(String query) {
        List<Comic> comic_filtered = new ArrayList<>();
        //When the comic category matches the query category- add comic to fetched list
        for (Comic comic : Common.comicList) {
            if (comic.Category != null) {
                if (comic.Category.contains(query))
                    comic_filtered.add(comic);
            }
        }
        // If any comics are found in filter, display them using the RecyclerView
        if (comic_filtered.size() > 0)
            recycler_filter_search.setAdapter(new ComicAdapter(getBaseContext(), comic_filtered));
        else
            //If no comics are found, generate popup message alerting user of this
            Toast.makeText(this, "No results found!", Toast.LENGTH_SHORT).show();

    }
}
