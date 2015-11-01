package com.oz.game.tictactoe.core;

import com.oz.game.tictactoe.core.io.GameMove;
import com.oz.game.tictactoe.core.io.GameInput;

import java.util.List;

/**
 * Created by developer on 7/11/15.
 */
class HumanPlayer extends Player {
    private final GameInput io;

    HumanPlayer(final GameState.GamePiece gp, final GameInput io) {
        super(gp);
        this.io = io;
    }

    void play(final GameState state) {
        final GameMove gameMove = io.get();
        if (gameMove != null) {
            final GameState.Spot spot = new GameState.Spot(gameMove.get(0), gameMove.get(1));
            state.set(getGamePiece(), spot);
        }
    }
}
