/*
Class: Common
Author: B5017070
Purpose: Common java class containing fields used throughout the application
*/

package com.example.androidfirebasecomicreader.Common;

import com.example.androidfirebasecomicreader.Model.Chapter;
import com.example.androidfirebasecomicreader.Model.Comic;
import com.example.androidfirebasecomicreader.Model.User;

import java.util.ArrayList;
import java.util.List;

public class Common {

    //Fields used in comic book and chapter selection
    public static Comic comicSelected;
    public static List<Comic> comicList = new ArrayList<>();
    public static Chapter chapterSelected;
    public static List<Chapter> chapterList;
    //ChapterIndex -1 to prevent off-by-one-errors
    public static int chapterIndex = -1;

    //Formatting String for comic book and category names
    public static String formatString(String name) {
        StringBuilder finalResult = new StringBuilder(name.length() > 15 ? name.substring(0, 15) + "..." : name);
        return finalResult.toString();
    }
    //List of comic book genre categories that will be compared to categories the comics have in Firebase
    public static String[] filter_categories = {
            "Action",
            "Adventure",
            "Comedy",
            "DC",
            "Drama",
            "Fantasy",
            "Historical",
            "Horror",
            "Marvel",
            "Material arts",
            "Mystery",
            "Psychological",
            "Romance",
            "Sci fi",
            "Sports",
            "Superhero",
            "Supernatural",
            "Tragedy",
    };
    public static User currentUser;
}
