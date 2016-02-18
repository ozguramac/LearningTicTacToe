package com.oz.game.tictactoe.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by developer on 7/25/15.
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestGameHistory {
    @Mock private GameState mockState;
    @Mock private PersistController mockPersistController;
    private GameHistory history;

    @Before
    public void beforeTest() throws Exception {
        history = new GameHistory(mockPersistController);
        history.setGreedyMoveThreshold(1d); //ensure best move is used
    }

    @Test
    public void testEntry() throws Exception
    {
        final GameHistory.Key key = new GameHistory.Key(666, 999, 'O');

        final int l = 333;

        final GameHistory.Entry e = new GameHistory.Entry(key, l, 0);

        Assert.assertEquals(key.getStateOfX(), e.getStateOfX());
        Assert.assertEquals(key.getStateOfO(), e.getStateOfO());
        Assert.assertEquals(key.getWhoseTurn(), e.getWhoseTurn());
        Assert.assertEquals(l, e.getMoveLocNum().intValue());
    }

    @Test
    public void testAddMove() throws Exception {
        final GameState.GamePiece gp = GameState.GamePiece.X;
        final GameState.Spot spot = new GameState.Spot(0,0);
        history.addMove(mockState, spot, gp);

        verify(mockState).of(GameState.GamePiece.O);
        verify(mockState).of(GameState.GamePiece.X);

        for (GameHistory.Entry entry : history.getEntries()) {
            Assert.assertEquals("Game piece", gp.toChar(), entry.getWhoseTurn());
            Assert.assertEquals("Spot", spot.toNum(), entry.getMoveLocNum().intValue());
        }
    }

    @Test
    public void testGetRandomMove() throws Exception {
        final GameState.Spot spot = new GameState.Spot(1,1);
        when(mockState.getEmptySpots()).thenReturn(Arrays.asList(spot));

        final GameState.Spot randomSpot = history.getBestMove(mockState, GameState.GamePiece.O);

        verify(mockState).of(GameState.GamePiece.O);
        verify(mockState).of(GameState.GamePiece.X);
        verify(mockPersistController).findBest(any(GameHistory.Key.class));

        Assert.assertEquals("Spot loc num", spot.toNum(), randomSpot.toNum());
    }

    @Test
    public void testGetBestMove() throws Exception {
        final int locNum = 666;
        final GameHistory.Entry mockFoundEntry = mock(GameHistory.Entry.class);
        when(mockFoundEntry.getMoveLocNum()).thenReturn(locNum);
        when(mockPersistController.findBest(any(GameHistory.Key.class))).thenReturn(mockFoundEntry);

        final GameState.GamePiece gp = GameState.GamePiece.O;
        final GameState.Spot bestSpot = history.getBestMove(mockState, gp);

        verify(mockState).of(GameState.GamePiece.O);
        verify(mockState).of(GameState.GamePiece.X);
        verify(mockPersistController).findBest(any(GameHistory.Key.class));

        Assert.assertEquals("Best spot loc num", locNum, bestSpot.toNum());
    }

    @Test
    public void testPersistTie() throws Exception {
        history.addMove(mockState, new GameState.Spot(0, 0), GameState.GamePiece.X);
        history.addMove(mockState, new GameState.Spot(0, 1), GameState.GamePiece.X);
        history.addMove(mockState, new GameState.Spot(0, 2), GameState.GamePiece.X);
        history.addMove(mockState, new GameState.Spot(1, 0), GameState.GamePiece.O);
        history.addMove(mockState, new GameState.Spot(1, 1), GameState.GamePiece.O);
        history.addMove(mockState, new GameState.Spot(1, 2), GameState.GamePiece.O);

        //No winner -> Tie
        final GameState.GamePiece tie = GameState.GamePiece.NONE;
        history.persist(tie);

        verify(mockPersistController).save(eq(history));

        Assert.assertEquals("Winner", GameState.GamePiece.NONE.toChar(), history.getWinner());
    }

    @Test
    public void testPersistWinnerLoser() throws Exception {
        history.addMove(mockState, new GameState.Spot(0, 0), GameState.GamePiece.X);
        history.addMove(mockState, new GameState.Spot(0, 1), GameState.GamePiece.X);
        history.addMove(mockState, new GameState.Spot(0, 2), GameState.GamePiece.X);
        history.addMove(mockState, new GameState.Spot(1, 0), GameState.GamePiece.O);
        history.addMove(mockState, new GameState.Spot(1, 1), GameState.GamePiece.O);
        history.addMove(mockState, new GameState.Spot(1, 2), GameState.GamePiece.O);

        //A winner
        final GameState.GamePiece winner = GameState.GamePiece.X;
        history.persist(winner);

        verify(mockPersistController).save(eq(history));

        Assert.assertEquals("Winner", GameState.GamePiece.X.toChar(), history.getWinner());
    }

    //TODO: Negative testing...

}
