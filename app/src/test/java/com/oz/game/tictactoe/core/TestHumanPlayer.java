package com.oz.game.tictactoe.core;

import com.oz.game.tictactoe.core.io.GameInput;
import com.oz.game.tictactoe.core.io.GameMove;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by developer on 8/15/15.
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestHumanPlayer {
    @Mock private GameState mockState;
    private HumanPlayer player;
    private GameState.GamePiece gamePiece;
    private GameState.Spot someSpot;

    @Before
    public void beforeTest() {
        final int i=1, j=1;
        someSpot = new GameState.Spot(i, j);
        gamePiece = GameState.GamePiece.X;
        player = new HumanPlayer(gamePiece, new GameInput() {
            @Override
            public GameMove get() {
                return new GameMove(gamePiece.toChar(), i, j);
            }
        });
    }

    @Test
    public void testPlay() {//TODO: Failing with null pointer (game move pair is not initialized?!)
        player.play(mockState);

        verify(mockState).set(eq(gamePiece), eq(someSpot));
    }
}
