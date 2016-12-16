package com.software.group58.chess58;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {
    public static int turn = 0;
    public static boolean gameOver=false;
    public static boolean drawAvailable = false;
    public static String player = "";
    public static boolean[] blackCastle = new boolean[]{true,true};
    public static boolean[] whiteCastle = new boolean[]{true,true};
    public static String blackKing[] = new String[]{"e8","safe"};
    public static String whiteKing[] = new String[]{"e1","safe"};
    public static boolean stalemate = false;
    public static String passant = "";


    boolean firstSelection = true;
    TileView source = null;
    TileView target = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide();
        setContentView(R.layout.game_activity);

        Board.initializeBoard();

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
        String currentLabel;
        if(firstSelection){
            source = (TileView) touchedTile;
            currentLabel = getResources().getResourceName(source.getId());
            Toast.makeText(GameActivity.this, "You touched "+currentLabel.substring(currentLabel.length()-2),
                    Toast.LENGTH_SHORT).show();
            if(source.getDrawable() == null)
                return;
            else
                firstSelection = false;
        }
        else{
            target = (TileView) touchedTile;
            if(target != source){
                target.setImageDrawable(source.getDrawable());
                target.currentPiece = source.currentPiece;
                source.setImageDrawable(null);
                source.currentPiece = "empty";
            }
            firstSelection = true;
        }
        return;
    }

    /**
     * This method executes the move.
     * @param origin, the original location of the piece.
     * @param destination, where the piece wants to move.
     * @param promo, if pawn promotion is an option, then the type of piece
     * @param keepIt, x
     * that the player wishes in exchange for a pawn
     */
    public static boolean executeMove(String origin, String destination, String promo, boolean keepIt, int castling){
        origin = Mapping.convert(origin);
        int originFile = Character.getNumericValue(origin.charAt(0));
        int originRank = Character.getNumericValue(origin.charAt(1));
        String originPiece = Board.tiles[originFile][originRank].currentPiece;

        destination = Mapping.convert(destination);
        int destinationFile = Character.getNumericValue(destination.charAt(0));
        int destinationRank = Character.getNumericValue(destination.charAt(1));
        String destinationPiece = Board.tiles[destinationFile][destinationRank].currentPiece;

        Board.tiles[destinationFile][destinationRank].currentPiece = originPiece;
        Board.tiles[originFile][originRank].currentPiece = "empty";

        String previousWhiteKing = whiteKing[0];
        String previousBlackKing = blackKing[0];

        if(originPiece.equals("wK"))
            whiteKing[0] = Board.tiles[destinationFile][destinationRank].label;
        if(originPiece.equals("bK"))
            blackKing[0] = Board.tiles[destinationFile][destinationRank].label;

        int rookOriginFile, rookOriginRank, rookDestFile, rookDestRank;
        switch(castling){
            case 4:
                rookOriginFile = 1;
                rookOriginRank = 1;
                rookDestFile = 1;
                rookDestRank = 3;
                break;
            case 3:
                rookOriginFile = 1;
                rookOriginRank = 8;
                rookDestFile = 1;
                rookDestRank = 5;
                break;
            case 2:
                rookOriginFile = 8;
                rookOriginRank = 1;
                rookDestFile = 8;
                rookDestRank = 3;
                break;
            case 1:
                rookOriginFile = 8;
                rookOriginRank = 8;
                rookDestFile = 8;
                rookDestRank = 5;
                break;
            default:
                rookOriginFile = 0;
                rookOriginRank = 0;
                rookDestFile = 0;
                rookDestRank = 0;
        }

        if(rookOriginFile+rookOriginRank+rookDestFile+rookDestRank > 0){
            Board.tiles[rookDestFile][rookDestRank].currentPiece = Board.tiles[rookOriginFile][rookOriginRank].currentPiece;
            Board.tiles[rookOriginFile][rookOriginRank].currentPiece = "empty";
        }

        String thisKing = "";
        if(originPiece.substring(0,1).equalsIgnoreCase("b"))
            thisKing = blackKing[0];
        else
            thisKing = whiteKing[0];

        if(Rules.leavesKingInCheck(thisKing, originPiece.substring(0,1) ) ){
            Board.tiles[destinationFile][destinationRank].currentPiece = destinationPiece;
            Board.tiles[originFile][originRank].currentPiece = originPiece;
            if(rookOriginFile+rookOriginRank+rookDestFile+rookDestRank > 0){
                Board.tiles[rookOriginFile][rookOriginRank].currentPiece = Board.tiles[rookDestFile][rookDestRank].currentPiece;
                Board.tiles[rookDestFile][rookDestRank].currentPiece = "empty";
            }
            whiteKing[0] = previousWhiteKing;
            blackKing[0] = previousBlackKing;
            return false;
        }
        if(!keepIt){
            Board.tiles[destinationFile][destinationRank].currentPiece = destinationPiece;
            Board.tiles[originFile][originRank].currentPiece = originPiece;
            if(rookOriginFile+rookOriginRank+rookDestFile+rookDestRank > 0){
                Board.tiles[rookOriginFile][rookOriginRank].currentPiece = Board.tiles[rookDestFile][rookDestRank].currentPiece;
                Board.tiles[rookDestFile][rookDestRank].currentPiece = "empty";
            }
            whiteKing[0] = previousWhiteKing;
            blackKing[0] = previousBlackKing;
            return true;
        }

        switch(castling){
            case 4:
                whiteCastle[0] = false;
                whiteCastle[1] = false;
                break;
            case 3:
                whiteCastle[1] = false;
                whiteCastle[0] = false;
                break;
            case 2:
                blackCastle[1] = false;
                blackCastle[0] = false;
                break;
            case 1:
                blackCastle[1] = false;
                blackCastle[0] = false;
                break;
        }

        if(originPiece.equals("bp") && (destinationFile == 1 || destinationFile == '1')){
            //pawn promotion
            if(promo != ""){
                Board.tiles[destinationFile][destinationRank].currentPiece = "b" + promo;
            }
            else
                Board.tiles[destinationFile][destinationRank].currentPiece = "bQ";
        }
        if(originPiece.equals("wp") && (destinationFile == 8 || destinationFile == '8')){
            //pawn promotion
            if(promo != ""){
                Board.tiles[destinationFile][destinationRank].currentPiece = "w" + promo;
            }
            else
                Board.tiles[destinationFile][destinationRank].currentPiece = "wQ";
        }

        if(originPiece.charAt(1) == 'p' && Math.abs(destinationFile - originFile) == 2)
            passant = destination;
        else
            passant = "";

        return true;
    }
}
