package com.oz.game.tictactoe.core;

import com.oz.game.tictactoe.core.io.GameInput;
import com.oz.game.tictactoe.core.io.GameOutput;
import com.oz.game.tictactoe.core.persist.PersistEntry;
import com.oz.game.tictactoe.core.persist.PersistFacade;
import com.oz.game.tictactoe.core.persist.PersistenceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by developer on 11/12/15.
 */

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestGameSession {
    @Mock private GameInput mockIn;
    @Mock private GameOutput mockOut;
    @Mock private PersistFacade mockFacade;
    private GameSession gameSession;

    @Before
    public void beforeTest() throws Exception {
        gameSession = new GameSession(
                new GameConfig()
                .difficuilty(GameConfig.Difficulty.GREEDY)
                .persistFacade(mockFacade)
                .input(mockIn)
                .output(mockOut)
                .printState(true)
        );
    }

    @Test
    public void testPlay() throws Exception {
        do {
            gameSession.play();
        } while (false == gameSession.isGameOver());

        gameSession.logStats();
    }

    @Test
    public void testError() throws Exception {
        final PersistenceException pe = new PersistenceException("Testing!!");
        when(mockFacade.matchBest(any(PersistEntry.class))).thenThrow(pe);

        gameSession.play();

        verify(mockFacade).matchBest(any(PersistEntry.class));
    }
}
