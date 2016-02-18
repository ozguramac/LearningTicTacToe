package com.oz.game.tictactoe.core;

import com.oz.game.tictactoe.core.persist.PersistContainer;
import com.oz.game.tictactoe.core.persist.PersistEntry;
import com.oz.game.tictactoe.core.persist.PersistFacade;
import com.oz.game.tictactoe.core.persist.PersistenceException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by developer on 7/15/15.
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestPersistController {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Mock private GameHistory mockHistory;
    @Mock private PersistFacade mockPersistFacade;
    private PersistController persistController;

    @Before
    public void beforeTest() {
        persistController = new PersistController(mockPersistFacade);
    }

    @Test
    public void testFindMax() throws Exception {
        final GameHistory.Key key = new GameHistory.Key(666, 999, 'Z');

        final GameHistory.Entry entry1 = new GameHistory.Entry(key, 222, 0);
        final GameHistory.Entry entry2 = new GameHistory.Entry(key, 333, 0);
        final GameHistory.Entry entry3 = new GameHistory.Entry(key, 111, 0);

        when(mockHistory.getEntries()).thenReturn(Arrays.asList(entry1, entry2, entry3));

        persistController.save(mockHistory);

        verify(mockPersistFacade).persist(any(PersistContainer.class));

        when(mockPersistFacade.matchBest(any(PersistEntry.class))).thenReturn(entry2);

        final GameHistory.Entry foundEntry = persistController.findBest(key);
        Assert.assertEquals("Move location", entry2.getMoveLocNum(), foundEntry.getMoveLocNum());

        verifyFindDelete(entry1);
        verifyFindDelete(entry2);
        verifyFindDelete(entry3);
    }

    private void verifyFindDelete(GameHistory.Entry entry) throws PersistenceException {
        when(mockPersistFacade.match(any(PersistEntry.class))).thenReturn(entry);

        final GameHistory.Entry found = persistController.find(entry);
        verify(mockPersistFacade).match(eq(entry));

        persistController.delete(found);
        verify(mockPersistFacade).delete(eq(found));
    }

    @Test
    public void testNoFind() throws Exception {
        final GameHistory.Key key = new GameHistory.Key(111, 222, 'S');
        Assert.assertNull("Not found", persistController.findBest(key));
        verify(mockPersistFacade).matchBest(key);

        final GameHistory.Entry entry = new GameHistory.Entry(key, 333, 0);
        Assert.assertNull("Not found", persistController.find(entry));
        verify(mockPersistFacade).match(entry);
    }

    @Test
    public void testWriteError() throws Exception {
        final GameHistory.Key key = new GameHistory.Key(666, 999, 'Z');
        final GameHistory.Entry e = new GameHistory.Entry(key, 222, 0);

        when(mockPersistFacade.match(eq(e))).thenThrow(PersistenceException.class);

        thrown.expect(PersistenceException.class);
        persistController.find(e);
    }
}
