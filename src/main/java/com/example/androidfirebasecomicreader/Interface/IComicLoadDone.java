package com.example.androidfirebasecomicreader.Interface;

import com.example.androidfirebasecomicreader.Model.Comic;

import java.util.List;

public interface IComicLoadDone {
    void onComicLoadDoneListener(List<Comic> comicList);
}
