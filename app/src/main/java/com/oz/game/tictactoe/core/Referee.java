package com.oz.game.tictactoe.core;

/**
 * Created by developer on 3/17/15.
 */
class Referee {

    //Determine by looking for same piece in a row, column or diagonal
    private static final int[] WIN_STATES = {
            //HORIZONTAL
             0b000000111
            ,0b000111000
            ,0b111000000
            //VERTICAL
            ,0b001001001
            ,0b010010010
            ,0b100100100
            //DIAGONAL
            ,0b100010001
            ,0b001010100
    };

    private static final GameState.GamePiece[] GAME_PIECES = {
             GameState.GamePiece.O
            ,GameState.GamePiece.X
    };

    boolean isGameOver(final GameState state) {
        return getWinner(state) != GameState.GamePiece.NONE || state.getNumOfEmptySpots() == 0;
    }

    GameState.GamePiece getWinner(final GameState state) {
        for (int winState : WIN_STATES) {
            for (GameState.GamePiece gp : GAME_PIECES) {
                if ((state.of(gp) & winState) == winState) {
                    return gp;
                }
            }
        }
        return GameState.GamePiece.NONE;
    }

    GameState.GamePiece whoseTurn(final GameState state) {
        if ((state.of(GameState.GamePiece.O) & state.of(GameState.GamePiece.X)) != 0) {
            throw new IllegalStateException("Game state is illegal: Overlapping game pieces!!");
        }

        final int cntO = state.count(GameState.GamePiece.O);
        final int cntX = state.count(GameState.GamePiece.X);
        if (Math.abs(cntO - cntX) > 1) {
            throw new IllegalStateException("Game state is illegal: #X="+cntX+", #0="+cntO+"!!");
        }

        if (cntO < cntX) {
            return GameState.GamePiece.O;
        }
        return GameState.GamePiece.X; //X is default
    }
}
