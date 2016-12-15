package com.software.group58.chess58;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide();
        setContentView(R.layout.game_activity);
        ImageView knight = (ImageView) findViewById(R.id.a1);
        knight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView source = (ImageView) v;
                source.setImageResource(R.drawable.ic_menu_share);
                ImageView target = (ImageView) findViewById(R.id.b2);
                target.setImageResource(R.drawable.white_knight);
                target.setBackgroundColor(0xFF0000);  //should be red!
            }
        });
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
