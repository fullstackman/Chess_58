package com.software.group58.chess58;

import com.software.group58.chess58.GameActivity;

import java.util.ArrayList;

/**
 * Created by b-rad on 12/15/2016.
 */

public class Rules {
    /**
     * This method checks to see if checkmate was just achieved
     * @return a boolean indicating whether the game is over or not
     */
    public static boolean determineCheckmate(int nextTurn){
		/*Define the enemy player
		 * Loop through the board to find their pieces
		 * call findPossibleMoves() on each one
		 * call executeMove(false) on each one found
		 * return false on the first executable move found
		 * else return true*/
        String enemyColor = "";
        int start=0, end=0, iterator=0;

        switch(nextTurn % 2){
            case 0:
                enemyColor = "b";
                start = 8;
                end = 0;
                iterator = -1;
                break;
            case 1:
                enemyColor = "w";
                start = 1;
                end = 9;
                iterator = 1;
                break;
            default:
                System.out.println("You are not counting turns correctly!");
        }

        for(int i = start; i!=end; i+=iterator){
            for(int j = start; j!=end; j+=iterator){
                if(Board.tiles[i][j].currentPiece.substring(0,1).equalsIgnoreCase(enemyColor) ){
                    String origin = Board.tiles[i][j].label;
                    ArrayList<String> possibleMoves = findPossibleMoves(origin);
                    for(String move : possibleMoves){
                        if(GameActivity.executeMove(origin,move,"",false,0))
                            return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * This method finds the valid moves for the piece at the current Tile.
     * @param currentTile
     * @return an ArrayList containing all possible locations it can move to
     */

    public static ArrayList<String> findPossibleMoves (String currentTile){
        String location = Mapping.convert(currentTile);
        int rank = Character.getNumericValue(location.charAt(0));
        int file = Character.getNumericValue(location.charAt(1));
        ArrayList<String> moves = new ArrayList <String>();
        String piece = Board.tiles[rank][file].currentPiece;

        if(piece.equals("empty"))
            return moves;
        char currentColor = piece.charAt(0);

        switch (piece.charAt(1)){
			/*The procedure for each piece will be as follows:
			 * for(every direction the piece can move in)
			 * 	if this square is empty,
			 * 		add it to the validMoves list
			 * 		check the next square
			 * 	else (this square is occupied)
			 * 		if the occupying piece is of the enemy color
			 * 			add this square to the validMoves list
			 * 			check the next square
			 * 		stop*/

            case 'p':
                //it can move two spaces forward if in the first row of movement
                //one space forward else
                //diagonal by one space if it is taking another piece
                //cannot move backwards
                //en passant
                //can capture diagonally if there is a pawn in front
                //diagonal right or diagonal left

                //int flags to modify the movement code for its appropriate color
                int startingRow = 0;
                int sideOfBoard = 0;

                if(currentColor == 'b'){
                    startingRow = 7;
                    sideOfBoard = -1;
                }
                else{
                    startingRow = 2;
                    sideOfBoard = 1;
                }

                Tile possible = Board.tiles[rank+sideOfBoard][file];
                if(possible.currentPiece.equals("empty"))
                    moves.add(possible.label);

                if(rank == startingRow ){
                    possible = Board.tiles[rank+(2*sideOfBoard)][file];
                    //if the pawn is still in it's color's starting row, it can moves two spaces if that tile isn't occupied
                    if(possible.currentPiece.equals("empty"))
                        moves.add(possible.label);
                }

                for(int x=-1;  x<2; x+=2){
                    int row = rank+sideOfBoard;
                    int column = file+x;
                    if(row > 8 || row < 0 || column > 8 || column < 0)
                        continue;
                    possible = Board.tiles[row][column];
                    if(possible.currentPiece.equals("empty")){
                        //en passant only ever happens when a pawn is 3 spaces past its home row
                        if(rank == (startingRow+(3*sideOfBoard) ) && Board.tiles[rank][column].label.equals(GameActivity.passant)){
                            //en passant implementation
                            moves.add(possible.label);
                        }
                        else
                            continue;
                    }
                    else{
                        if(possible.currentPiece.charAt(0) != currentColor)
                            moves.add(possible.label);
                        continue;
                    }
                }
                break;

            case 'N':
                //has 8 possible jumps it can make
                //combinations of two vertical, one horizontal
                //or two horizontal, one vertical

                for(int i=-2; i<3; ++i){
                    for(int j=-2; j<3; ++j){
                        //filters out any tiles that don't match the knight's pattern of movement
                        if(Math.abs(i) == Math.abs(j) || j==0 || i==0)
                            continue;
                        //make sure we stay within the boundaries of the board!
                        int x = rank+i;
                        int y = file+j;
                        Tile target = new Tile();
                        if(x < 9 && x > 0 && y < 9 && y > 0)
                            target = Board.tiles[x][y];
                        else
                            continue;
                        if(target.currentPiece.equals("empty")){
                            moves.add(target.label);
                            continue;
                        }
                        else{
                            if(target.currentPiece.charAt(0) != currentColor)
                                moves.add(target.label);
                            continue;
                        }
                    }
                }
                break;

            case 'B':
                //diagonal in four directions, but
                //if a piece blocking it, cannot move

                for(int i=-1; i<2; i+=2){
                    for(int j=-1; j<2; j+=2){
                        // i and j will never be zero
                        //this means we won't loop through the non-diagonal movements nor the current tile

                        int x = rank+i;
                        int y = file+j;
                        Tile target = new Tile();

                        if(x < 9 && x > 0 && y < 9 && y > 0)
                            target = Board.tiles[x][y];
                        else
                            continue;

                        if(target.currentPiece.equals("empty")){
                            moves.add(target.label);
                            x+=i;
                            y+=j;
                            while(x < 9 && x > 0 && y < 9 && y > 0){
                                target = Board.tiles[x][y];
                                if(target.currentPiece.equals("empty")){
                                    moves.add(target.label);
                                    x = x+i;
                                    y = y+j;
                                    continue;
                                }
                                else{
                                    if(target.currentPiece.charAt(0) != currentColor)
                                        moves.add(target.label);
                                    break;
                                }
                            }
                        }
                        else{
                            if(target.currentPiece.charAt(0) != currentColor)
                                moves.add(target.label);
                            continue;
                        }
                    }
                }
                break;

            case 'Q':
                //any direction but if piece blocking it
                //cannot move

                for(int i=-1; i<2; ++i){
                    for(int j=-1; j<2; ++j){
                        //exclude the current tile
                        if(j==0 && i==0)
                            continue;

                        int x = rank+i;
                        int y = file+j;
                        Tile target = new Tile();

                        if(x < 9 && x > 0 && y < 9 && y > 0)
                            target = Board.tiles[x][y];
                        else
                            continue;
                        if(target.currentPiece.equals("empty")){
                            moves.add(target.label);
                            x+=i;
                            y+=j;
                            while(x < 9 && x > 0 && y < 9 && y > 0){
                                target = Board.tiles[x][y];
                                if(target.currentPiece.equals("empty")){
                                    moves.add(target.label);
                                    x = x+i;
                                    y = y+j;
                                    continue;
                                }
                                else{
                                    if(target.currentPiece.charAt(0) != currentColor)
                                        moves.add(target.label);
                                    break;
                                }
                            }
                        }//end if
                        else{
                            if(target.currentPiece.charAt(0) != currentColor)
                                moves.add(target.label);
                            continue;
                        }
                    } //end loop of j
                } //end loop of i
                break;

            case 'R':
                //can move up/down/left/right
                //if a piece is blocking its path, invalid move

                for(int i=-1; i<2; ++i){
                    for(int j=-1; j<2; ++j){
                        //exclude the diagonals and the current tile
                        // semantically equivalent to ( (j==0 && i==0) || (j!=0 && i!=0) )
                        if( (j ^ i) == 0)
                            continue;
                        int x = rank+i;
                        int y = file+j;
                        Tile target = new Tile();

                        if(x < 9 && x > 0 && y < 9 && y > 0)
                            target = Board.tiles[x][y];
                        else
                            continue;

                        if(target.currentPiece.equals("empty")){
                            moves.add(target.label);
                            x+=i;
                            y+=j;
                            while(x < 9 && x > 0 && y < 9 && y > 0){
                                target = Board.tiles[x][y];
                                if(target.currentPiece.equals("empty")){
                                    moves.add(target.label);
                                    x = x+i;
                                    y = y+j;
                                    continue;
                                }
                                else{
                                    if(target.currentPiece.charAt(0) != currentColor)
                                        moves.add(target.label);
                                    break;
                                }
                            }
                        }
                        else{
                            if(target.currentPiece.charAt(0) != currentColor)
                                moves.add(target.label);
                            continue;
                        }
                    }
                }
                break;

            case 'K':
                //one move in any direction
                //castle under certain conditions
                for(int i=-1; i<2; ++i){
                    for(int j=-1; j<2; ++j){
                        //exclude the current tile
                        if(j==0 && i==0)
                            continue;
                        //make sure we stay within the boundaries of the board!
                        int x = rank+i;
                        int y = file+j;
                        Tile target = new Tile();
                        if(x < 9 && x > 0 && y < 9 && y > 0)
                            target = Board.tiles[x][y];
                        else
                            continue;
                        if(target.currentPiece.equals("empty")){
                            moves.add(target.label);
                            continue;
                        }
                        else{
                            if(target.currentPiece.charAt(0) != currentColor)
                                moves.add(target.label);
                            continue;
                        }
                    }
                }

				/*Castling implementation.
				 * Requires the executeMove method to keep track of when the Kings and Rooks have moved
				 * in order to update the values of the respective castling booleans*/
                boolean queenSideCastle = false;
                boolean kingSideCastle = false;

                if (currentColor == 'b'){
                    kingSideCastle = GameActivity.blackCastle[1];
                    queenSideCastle = GameActivity.blackCastle[0];
                }
                else{
                    kingSideCastle = GameActivity.whiteCastle[1];
                    queenSideCastle = GameActivity.whiteCastle[0];
                }
                if(kingSideCastle){
                    for(int x=1; x<3; ++x){
                        if(file-x < 0 || file-x > 8)
                            continue;
                        Tile target = Board.tiles[rank][file-x];
                        if(!target.currentPiece.equals("empty")){
                            kingSideCastle = false;
                            break;
                        }
                    }
                    if(kingSideCastle){
                        moves.add(Board.tiles[rank][file-2].label);
                    }
                }
                if(queenSideCastle){
                    for(int x=1; x<4; ++x){
                        if(file+x < 0 || file+x > 8)
                            continue;
                        Tile target = Board.tiles[rank][file+x];
                        if(!target.currentPiece.equals("empty")){
                            queenSideCastle = false;
                            break;
                        }
                    }
                    if(queenSideCastle){
                        moves.add(Board.tiles[rank][file+2].label);
                    }
                }
                break;

            default:
                System.out.println("You gave is \'findPossibleMoves\' an invalid piece!");
                return moves;
        }

        return moves;
    }

    /**
     * This method tries to see if a King piece is in check
     * @return a boolean specifying whether the move is valid (i.e. does it
     * put the own player's king in check)
     */
    public static boolean leavesKingInCheck (String king, String kingColor){
        String kingLocation = Mapping.convert(king);
        int file = Character.getNumericValue(kingLocation.charAt(0));
        int rank = Character.getNumericValue(kingLocation.charAt(1));

        ArrayList<String> possibleAttacks = new ArrayList<String>();
        String[] attackers = new String[]{"N","B","Q","R","K"};
        for(String sampleAttacker : attackers){
            Board.tiles[file][rank].currentPiece = kingColor+sampleAttacker;
            possibleAttacks = findPossibleMoves(king);
            for(String move : possibleAttacks){
                String attackerLocation = Mapping.convert(move);
                int attackerFile = Character.getNumericValue(attackerLocation.charAt(0));
                int attackerRank = Character.getNumericValue(attackerLocation.charAt(1));
                if(Board.tiles[attackerFile][attackerRank].currentPiece.substring(1,2).equalsIgnoreCase(sampleAttacker)){
                    Board.tiles[file][rank].currentPiece = kingColor+"K";
                    return true;
                }
            }
            //at this point, we know the King is not in check by this type of piece
        }
        Board.tiles[file][rank].currentPiece = kingColor+"K";
        //Now we check if the pawn is putting the King in check
        //first find attacker pawn's color
        String attackerColor = "";
        if (kingColor.equals("b")) {
            attackerColor = "w";
        } else {
            attackerColor = "b";
        }

        //Now we check if the pawn is putting the King in check
        //king is at top left

        if (file == 8 && rank == 8 && file < 9 && rank > 0) {
            if (Board.tiles[file-1][rank-1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file-1][rank-1].label).contains(Board.tiles[file][rank].label) == true)) {
                return true;
            }
            //king is at top right
        } else if (file == 8 && rank == 1 && file < 9 && rank > 0) {
            if (Board.tiles[file-1][rank+1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file-1][rank+1].label).contains(Board.tiles[file][rank].label) == true)) {
                return true;
            }
            //king is at bottom left
        } else if (file == 1 && rank == 8 && file < 9 && rank > 0) {
            if (Board.tiles[file+1][rank-1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file+1][rank-1].label).contains(Board.tiles[file][rank].label) == true)) {
                return true;
            }
            //king is at bottom right
        } else if (file == 1 && rank == 1 && file < 9 && rank > 0) {
            if (Board.tiles[file+1][rank+1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file+1][rank+1].label).contains(Board.tiles[file][rank].label) == true)) {
                return true;
            }
            //king is at the top
        } else if (file == 8 && rank <= 7 && rank >= 2 && file < 9 && rank > 0) {
            if ((Board.tiles[file-1][rank+1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file-1][rank+1].label).contains(Board.tiles[file][rank].label) == true)) || (Board.tiles[file-1][rank-1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file-1][rank-1].label).contains(Board.tiles[file][rank].label) == true))) {
                System.out.println(file + " " + rank);
                return true;
            }
            //king is at the bottom
        } else if (file == 1 && rank <= 7 && rank >= 2 && file < 9 && rank > 0) {
            if ((Board.tiles[file+1][rank+1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file+1][rank+1].label).contains(Board.tiles[file][rank].label) == true)) || (Board.tiles[file+1][rank-1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file+1][rank-1].label).contains(Board.tiles[file][rank].label) == true))) {
                return true;
            }
            //king is on the left side
        } else if (rank == 8 && file <= 7 && file >= 2 && file < 9 && rank > 0) {
            if ((Board.tiles[file+1][rank-1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file+1][rank-1].label).contains(Board.tiles[file][rank].label) == true)) || (Board.tiles[file-1][rank-1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file-1][rank-1].label).contains(Board.tiles[file][rank].label) == true))) {
                return true;
            }
            //king is on the right side
        } else if (rank == 1 && file <= 7 && file >= 2 && file < 9 && rank > 0) {
            if ((Board.tiles[file+1][rank+1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file+1][rank+1].label).contains(Board.tiles[file][rank].label) == true)) || (Board.tiles[file-1][rank+1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file-1][rank+1].label).contains(Board.tiles[file][rank].label) == true))) {
                return true;
            }
            //king is anywhere else
        } else if ((Board.tiles[file+1][rank+1].currentPiece.equals(attackerColor+"p") && (findPossibleMoves(Board.tiles[file+1][rank+1].label).contains(Board.tiles[file][rank].label) == true)) || (Board.tiles[file+1][rank-1].currentPiece.equals(attackerColor+"p")  && (findPossibleMoves(Board.tiles[file+1][rank-1].label).contains(Board.tiles[file][rank].label) == true)) || (Board.tiles[file-1][rank+1].currentPiece.equals(attackerColor+"p")  && (findPossibleMoves(Board.tiles[file-1][rank+1].label).contains(Board.tiles[file][rank].label) == true)) || (Board.tiles[file-1][rank-1].currentPiece.equals(attackerColor+"p")  && (findPossibleMoves(Board.tiles[file-1][rank-1].label).contains(Board.tiles[file][rank].label) == true))) {
            return true;
        }

        //at this point, the King must be completely safe!
        return false;
    }

    /**
     * This method determines whether a stalemate has been reached for the next
     * player to play.
     * @return a boolean that tells whether a stalemate has been reached
     * or not.
     */

    public static boolean isStalemate(){
        //complete this method
        return false;
    }
}
