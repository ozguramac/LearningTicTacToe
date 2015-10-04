package com.oz.game.tictactoe.core;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by developer on 7/25/15.
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestReferee {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock private GameState mockState;
    private Referee referee;

    @Before
    public void beforeTest() throws Exception {
        referee = new Referee();
    }

    @Test
    public void testWinner() throws Exception
    {
        final GameState.GamePiece expectedGp = GameState.GamePiece.X;
        when(mockState.of(eq(expectedGp))).thenReturn(0b111000000);

        final GameState.GamePiece gp = referee.getWinner(mockState);
        Assert.assertEquals("Game piece", expectedGp, gp);
    }

    @Test
    public void testNoWinner() throws Exception
    {
        when(mockState.of(any(GameState.GamePiece.class))).thenReturn(0b000000000);

        final GameState.GamePiece gp = referee.getWinner(mockState);
        Assert.assertEquals("Game piece", GameState.GamePiece.NONE, gp);
    }

    @Test
    public void testFuzzyTie() throws Exception
    {
        when(mockState.of(eq(GameState.GamePiece.O))).thenReturn(0b110100010);
        when(mockState.of(eq(GameState.GamePiece.X))).thenReturn(0b000011101);

        final GameState.GamePiece gp = referee.getWinner(mockState);
        Assert.assertEquals("Game piece", GameState.GamePiece.NONE, gp);
    }

    @Test
    public void testFuzzyWinner() throws Exception
    {
        final GameState.GamePiece expectedGp = GameState.GamePiece.X;
        when(mockState.of(eq(expectedGp))).thenReturn(0b111001001);

        final GameState.GamePiece gp = referee.getWinner(mockState);
        Assert.assertEquals("Game piece", expectedGp, gp);
    }

    @Test
    public void testGameOverTie() throws Exception
    {
        when(mockState.of(any(GameState.GamePiece.class))).thenReturn(0b000000000);
        when(mockState.getNumOfEmptySpots()).thenReturn(0);

        final boolean bGameOver = referee.isGameOver(mockState);
        Assert.assertTrue("Game over", bGameOver);
    }

    @Test
    public void testGameOverWinner() throws Exception
    {
        when(mockState.of(eq(GameState.GamePiece.O))).thenReturn(0b111000010);
        when(mockState.of(eq(GameState.GamePiece.X))).thenReturn(0b000011101);
        when(mockState.getNumOfEmptySpots()).thenReturn(1);

        final boolean bGameOver = referee.isGameOver(mockState);
        Assert.assertTrue("Game over", bGameOver);
    }

    @Test
    public void testWhoseTurn() throws Exception
    {
        when(mockState.count(eq(GameState.GamePiece.O))).thenReturn(0);
        when(mockState.count(eq(GameState.GamePiece.X))).thenReturn(0);

        Assert.assertEquals("Game piece", GameState.GamePiece.X, referee.whoseTurn(mockState));

        when(mockState.count(eq(GameState.GamePiece.X))).thenReturn(1);

        Assert.assertEquals("Game piece", GameState.GamePiece.O, referee.whoseTurn(mockState));
    }

    @Test
    public void testIllegalOverlappingState() throws Exception
    {
        when(mockState.of(eq(GameState.GamePiece.O))).thenReturn(0b111010110);
        when(mockState.of(eq(GameState.GamePiece.X))).thenReturn(0b001111101);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Game state is illegal: Overlapping game pieces!!");

        referee.whoseTurn(mockState);
    }

    @Test
    public void testIllegalCountState() throws Exception
    {
        when(mockState.of(eq(GameState.GamePiece.O))).thenReturn(0b110010010);
        when(mockState.of(eq(GameState.GamePiece.X))).thenReturn(0b000101101);

        when(mockState.count(eq(GameState.GamePiece.O))).thenReturn(3);
        when(mockState.count(eq(GameState.GamePiece.X))).thenReturn(1);

        thrown.expectMessage("Game state is illegal: #X=1, #0=3!!");

        referee.whoseTurn(mockState);
    }
}
