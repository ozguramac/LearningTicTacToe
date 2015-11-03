package com.oz.game.tictactoe.core.io;

/**
 * Created by developer on 9/20/15.
 */
public interface GameOutput {
    void onMove(final GameMove gameMove);
    void onGameOver(final char winner);
}
