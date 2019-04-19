/*
Interface: IComicLoad
Author: B5017070
Purpose: Abstract class used to group methods with comic book loading
*/

package com.example.androidfirebasecomicreader.Interface;

import com.example.androidfirebasecomicreader.Model.Comic;

import java.util.List;

public interface IComicLoad {
    void onComicLoadDoneListener(List<Comic> comicList);
}
