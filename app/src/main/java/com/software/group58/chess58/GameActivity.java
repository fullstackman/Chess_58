package com.software.group58.chess58;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    public static int turn = 1;
    public static boolean gameOver=false;
    public static boolean drawAvailable = false;
    public static String player = "";
    public static boolean[] blackCastle = new boolean[]{true,true};
    public static boolean[] whiteCastle = new boolean[]{true,true};
    public static String blackKing[] = new String[]{"e8","safe"};
    public static String whiteKing[] = new String[]{"e1","safe"};
    public static boolean stalemate = false;
    public static String passant = "";
    String currentLabel;
    ArrayList<String> validMoves = new ArrayList<String>();

    boolean firstSelection = true;
    TileView source = null;
    TileView target = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide();
        setContentView(R.layout.game_activity);

        Board.initializeBoard();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void handleInput(View touchedTile){
        if(gameOver)
            return;
        if(turn % 2 == 0)
            player = "Black";
        else
            player = "White";

        if(firstSelection){
            source = (TileView) touchedTile;
            currentLabel = getResources().getResourceName(source.getId());
            if(source.getDrawable() == null)
                return;

            String originPoints =  Mapping.convert(currentLabel.substring(currentLabel.length()-2));

            int originFile = Character.getNumericValue(originPoints.charAt(0));
            int originRank = Character.getNumericValue(originPoints.charAt(1));

            if(Board.tiles[originFile][originRank].currentPiece.charAt(0) != Character.toLowerCase(player.charAt(0))) {
                Toast.makeText(GameActivity.this, "That is not your piece!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            validMoves = Rules.findPossibleMoves( currentLabel.substring(currentLabel.length()-2) );
            if(validMoves.isEmpty()) {
                Toast.makeText(GameActivity.this, "Nothing is possible with that piece\nTry again...",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            //at this point it is safe to act on this piece
            Toast.makeText(GameActivity.this, "First input is: "+currentLabel.substring(currentLabel.length()-2),
                    Toast.LENGTH_SHORT).show();
            firstSelection = false;
        }
        else{
            target = (TileView) touchedTile;
            String startingLabel = currentLabel.substring(currentLabel.length()-2);
            currentLabel = getResources().getResourceName(target.getId());

            if(target != source){
                Toast.makeText(GameActivity.this, "Second input is: "+currentLabel.substring(currentLabel.length()-2),
                        Toast.LENGTH_SHORT).show();
                boolean foundMatch = false;
                for(String z: validMoves){
                    if (z.equals( currentLabel.substring(currentLabel.length()-2) ) ){
                        boolean leftKingInCheck;
                        leftKingInCheck = !executeMove(startingLabel, currentLabel.substring(currentLabel.length()-2), "", true,0);
                        if(leftKingInCheck){
                            Toast.makeText(GameActivity.this, "Don't leave your king in check!\nTry again...",
                                    Toast.LENGTH_SHORT).show();
                            firstSelection = true;
                            return;
                        }
                        foundMatch = true;
                        break;
                    }
                }
                if(!foundMatch){
                    Toast.makeText(GameActivity.this, "Illegal move.\nTry again...",
                            Toast.LENGTH_SHORT).show();
                    firstSelection = true;
                    return;
                }

                target.setImageDrawable(source.getDrawable());
                target.currentPiece = source.currentPiece;
                source.setImageDrawable(null);
                source.currentPiece = "empty";

                /*TODO: Need to incorporate some UI elements that indicate check and checkmate!*/
                boolean putEnemyInCheck;
                if(player.equals("White")){
                    putEnemyInCheck = Rules.leavesKingInCheck(blackKing[0], "b");
                    if(putEnemyInCheck){
                        blackKing[1] = "check";
                        gameOver = Rules.determineCheckmate(turn+1);
                        if(gameOver){
                            Toast.makeText(GameActivity.this, "Checkmate! White wins!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(GameActivity.this, "Check!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        blackKing[1] = "safe";
                }
                else{
                    putEnemyInCheck = Rules.leavesKingInCheck(whiteKing[0], "w");
                    if(putEnemyInCheck){
                        whiteKing[1] = "check";
                        gameOver = Rules.determineCheckmate(turn+1);
                        if(gameOver){
                            Toast.makeText(GameActivity.this, "Checkmate! Black wins!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(GameActivity.this, "Check!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        whiteKing[1] = "safe";
                }

                //support for our castling implementation?

                /*TODO: Might be possible to implement undo button here*/
                ++turn;
                firstSelection = true;

                if(gameOver){
                    /*TODO: Show button the lets user save game. Give it an onclicklistener and have
                    that function start a dialog that asks for a name. Then do serialization and write
                    the file to system memory
                    My tip is to just implement the serialization first and then add buttons and
                    dialogs later*/
                }
                return;
            }
            // Here the player must have touched the starting piece again
            Toast.makeText(GameActivity.this, "Move canceled...",
                    Toast.LENGTH_SHORT).show();
            firstSelection = true;
        }
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
