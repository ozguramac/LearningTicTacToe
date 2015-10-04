package com.oz.game.tictactoe.core;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by developer on 8/15/15.
 */
public class TestPlayer {
    private Player player;
    private GameState.GamePiece gamePiece;

    @Before
    public void beforeTest() {
        gamePiece = GameState.GamePiece.X;
        player = new Player(gamePiece) {
            @Override void play(GameState state) {}
        };
    }

    @Test
    public void test() {
        Assert.assertEquals("Game piece" , gamePiece, player.getGamePiece());
    }
}
