//Class: SplashScreen
// Author: B5017070
// Purpose: Controls functionality for the splash screen

package com.example.androidfirebasecomicreader;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Splash page should be present for 3 seconds when application is started
        //After this time has occurred, the user should be taken to Main Activity (Home page)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
            }
        },3000);
    }
}
