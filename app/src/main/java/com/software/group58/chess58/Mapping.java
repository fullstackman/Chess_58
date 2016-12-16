package com.software.group58.chess58;

/**
 * Created by b-rad on 12/15/2016.
 */

public class Mapping {
    /**
     * This method converts a given move to an appropriate location in the board matrix
     * @param location, the current Tile
     * @return the string representation of that location
     */
    public static String convert(String location){
        char column = location.charAt(0);
        int b = column % (89 + (2 * (column - 97) ) );
        return location.substring(1) + Integer.toString(b);
    }
}
