package com.software.group58.chess58;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("Developer Notes","OnCreate is happening!");
        setContentView(R.layout.activity_loading);
        hide();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //android.os.SystemClock.sleep(7000);
        //showHomeScreen();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* Execute some code after 4 seconds have passed
            ideally we should find a way to make this execute only the first time the app is opened.
        */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showHomeScreen();
            }
        }, 2700);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /** Opens the next screen*/
    public void showHomeScreen() {
        Intent goHome = new Intent(this, HomeActivity.class);
        startActivity(goHome);
    }
}
