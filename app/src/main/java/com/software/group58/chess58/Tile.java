package com.software.group58.chess58;

/**
 * What does this do?
 */

public class Tile {
    public String currentPiece;
    public String defaultColor;
    /*The label will describe the location of this tile. For example: a6 or f2.*/
    public String label;

    public Tile(){
        this.currentPiece="empty";
        this.defaultColor="## ";
        this.label="nowhere";
    }
}
