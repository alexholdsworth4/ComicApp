/*
Interface: IRecyclerItemClickListener
Author: B5017070
Purpose: Abstract class used to group methods with RecyclerView click events
*/

package com.example.androidfirebasecomicreader.Interface;

import android.view.View;

public interface IRecyclerItemClickListener {
    void OnClick(View view, int position);

    void onClick(View view, int position);
}

