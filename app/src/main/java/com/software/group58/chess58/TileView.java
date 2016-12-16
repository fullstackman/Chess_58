package com.software.group58.chess58;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * We are too lazy to delete this useless class and refactor
 */

public class TileView extends ImageView {
    public String currentPiece;
    /*The label will describe the location of this tile. For example: a6 or f2.*/
    public String label;

    public TileView(Context givenContext) {
        super(givenContext);
        this.currentPiece = "empty";
        this.label = "nowhere";
    }

    public TileView(Context givenContext, AttributeSet givenAttrs) {
        super(givenContext, givenAttrs);
        this.currentPiece = "empty";
        this.label = "nowhere";
    }

    public TileView(Context givenContext, AttributeSet givenAttrs, int givenStyleAttr) {
        super(givenContext, givenAttrs, givenStyleAttr);
        this.currentPiece = "empty";
        this.label = "nowhere";
    }

    public TileView(Context givenContext, AttributeSet givenAttrs, int givenStyleAttr, int givenStyleRes) {
        super(givenContext, givenAttrs, givenStyleAttr, givenStyleRes);
        this.currentPiece = "empty";
        this.label = "nowhere";
    }
}