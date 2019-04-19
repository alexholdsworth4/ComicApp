/*
Interface: IBanner
Author: B5017070
Purpose: Abstract class used to group methods with the banner loading
*/

package com.example.androidfirebasecomicreader.Interface;

import java.util.List;

public interface IBanner {
    void onBannerLoadDoneListener(List<String> banners);
}
