package com.software.group58.chess58;

import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    boolean firstSelection = true;
    Tile source = null;
    Tile target = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide();
        setContentView(R.layout.game_activity);

        Board.initializeBoard(this);

        /*GridView gridview = (GridView) findViewById(R.id.boardView);
        gridview.setAdapter(new TileAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(GameActivity.this, "You touched an item!",
                        Toast.LENGTH_SHORT).show();
            }
        });*/

        //Tile sampleTile = (Tile) findViewById(R.id.sampleTile);
        //sampleTile.setImageResource(Board.tiles[1][4]);

        /*Tile knight = (Tile) findViewById(R.id.leftTile);
        knight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tile source = (Tile) v;

            }
        });*/
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void handleInput(View touchedTile){
        if(firstSelection){
            source = (Tile) touchedTile;
            //Log.d("Developer Notes","OnPostCreate is happening!");
            if(source.getDrawable() == null)
                return;
            else
                firstSelection = false;
        }
        else{
            target = (Tile) touchedTile;
            if(target != source){
                target.setImageDrawable(source.getDrawable());
                target.label = source.label;
                source.setImageDrawable(null);
                source.label = "empty";
            }
            firstSelection = true;
        }
        return;
    }
}
