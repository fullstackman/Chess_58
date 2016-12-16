package com.software.group58.chess58;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * What does this do?
 */

public class Tile extends ImageView {
    public String currentPiece;
    /*The label will describe the location of this tile. For example: a6 or f2.*/
    public String label;

    public Tile(Context givenContext){
        super(givenContext);
        this.currentPiece="empty";
        this.label="nowhere";
    }

    public Tile(Context givenContext, AttributeSet givenAttrs){
        super(givenContext, givenAttrs);
        this.currentPiece="empty";
        this.label="nowhere";
    }

    public Tile(Context givenContext, AttributeSet givenAttrs, int givenStyleAttr){
        super(givenContext, givenAttrs, givenStyleAttr);
        this.currentPiece="empty";
        this.label="nowhere";
    }

    public Tile(Context givenContext, AttributeSet givenAttrs, int givenStyleAttr, int givenStyleRes){
        super(givenContext, givenAttrs, givenStyleAttr, givenStyleRes);
        this.currentPiece="empty";
        this.label="nowhere";
    }

    public String getLabel(){
        return label;
    }

    public String getCurrentPiece(){
        return currentPiece;
    }
}
