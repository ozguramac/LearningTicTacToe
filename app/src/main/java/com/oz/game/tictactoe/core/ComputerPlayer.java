package com.oz.game.tictactoe.core;

import com.oz.game.tictactoe.core.io.GameMove;
import com.oz.game.tictactoe.core.io.GameOutput;

/**
 * Created by developer on 7/11/15.
 */
class ComputerPlayer extends Player {
    private final GameOutput io;

    ComputerPlayer(final GameState.GamePiece gp, final GameOutput io) {
        super(gp);
        this.io = io;
    }

    void play(final GameState state) {
        //Look up from history and choose best move
        final GameState.GamePiece gp = getGamePiece();
        final GameState.Spot spot = state.getHistory().getBestMove(state, gp);
        state.set(gp, spot);
        if (io != null) {
            io.onMove(new GameMove(getGamePiece().toChar(), spot.toPair()));
        }
    }
}
