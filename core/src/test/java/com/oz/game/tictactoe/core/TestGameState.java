package com.oz.game.tictactoe.core;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by developer on 3/23/15.
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestGameState {
    @Mock(name = "history") private GameHistory mockHistory;
    private GameState gameState;

    @Before
    public void beforeTest() throws Exception {
        gameState = new GameState(mockHistory);

        Assert.assertEquals("Mock History", mockHistory, gameState.getHistory());
    }

    @Test
    public void emptySpotsTest() throws Exception {
        final List<GameState.Spot> emptySpots = gameState.getEmptySpots();
        Assert.assertNotNull(emptySpots);
        Assert.assertEquals("# of empty spots", 9, emptySpots.size());

        for (GameState.Spot emptySpot : emptySpots) {
            Assert.assertEquals("Empty spot", GameState.GamePiece.NONE, gameState.get(emptySpot));
        }
    }

    @Test
    public void getSpotTest() throws Exception {
        final GameState.Spot spot = new GameState.Spot(0,0);
        final GameState.GamePiece gp = gameState.get(spot);
        Assert.assertNotNull(gp);
        Assert.assertEquals("Spot@0,0", GameState.GamePiece.NONE, gp);
    }

    @Test
    public void setSpotTest() throws Exception {
        final GameState.Spot spot = new GameState.Spot(1,1);
        final GameState.GamePiece gp = GameState.GamePiece.O;
        gameState.set(gp, spot);
        verify(mockHistory).addMove(eq(gameState), eq(spot), eq(gp));
        Assert.assertEquals("Spot@1,1", gp, gameState.get(spot));
    }

    @Test
    public void useCaseSanity() throws Exception {
        final GameState.Spot spot = new GameState.Spot(1,1);
        final GameState.GamePiece gp = GameState.GamePiece.X;
        gameState.set(gp, spot);

        Assert.assertEquals("Spot@1,1", gp, gameState.get(spot));

        Assert.assertEquals("# of empty spots", 8, gameState.getNumOfEmptySpots());

        for (GameState.Spot emptySpot : gameState.getEmptySpots()) {
            Assert.assertNotSame("Empty spot", spot, emptySpot);
        }

        Assert.assertEquals("Num of Xs", 1, gameState.count(gp));
    }

    @Test
    public void stateOfTest() throws Exception {
        final GameState.GamePiece gp = GameState.GamePiece.O;

        gameState.set(gp, new GameState.Spot(0,0));
        gameState.set(gp, new GameState.Spot(1,1));
        gameState.set(gp, new GameState.Spot(2, 2));
        gameState.set(gp, new GameState.Spot(0, 1)); //extra spot

        final int stateOfO = gameState.of(gp);
        Assert.assertEquals("Game State of "+gp, 0b0100010011, stateOfO);
    }

    @Test
    public void printTest() throws Exception {
        gameState.set(GameState.GamePiece.X, new GameState.Spot(1,1));
        gameState.print(System.out);
    }
}
