package com.oz.game.tictactoe.core;

/**
 * Created by developer on 3/17/15.
 */
abstract class Player {
    private final GameState.GamePiece gamePiece;

    Player(final GameState.GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }

    protected GameState.GamePiece getGamePiece() {
        return gamePiece;
    }

    abstract void play(GameState state);
}
