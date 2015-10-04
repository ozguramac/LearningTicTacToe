package com.oz.game.tictactoe.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by developer on 8/15/15.
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestComputerPlayer {
    @Mock private GameHistory mockHistory;
    @Mock private GameState mockState;
    private ComputerPlayer player;
    private GameState.GamePiece gamePiece;

    @Before
    public void beforeTest() {
        gamePiece = GameState.GamePiece.O;
        player = new ComputerPlayer(gamePiece, null);

        when(mockState.getHistory()).thenReturn(mockHistory);
    }

    @Test
    public void testPlay() {
        final GameState.Spot bestSpot = new GameState.Spot(666);
        when(mockHistory.getBestMove(any(GameState.class), any(GameState.GamePiece.class)))
                .thenReturn(bestSpot);

        player.play(mockState);

        verify(mockHistory).getBestMove(eq(mockState),eq(gamePiece));
        verify(mockState).set(eq(gamePiece), eq(bestSpot));
    }
}
