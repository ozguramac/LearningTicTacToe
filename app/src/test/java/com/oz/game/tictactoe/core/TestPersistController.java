package com.oz.game.tictactoe.core;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by developer on 7/15/15.
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestPersistController {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Mock private GameHistory mockHistory;
    private PersistController persistController;

    @Before
    public void beforeTest() {
        persistController = new PersistController(null, "test");
    }

    @After
    public void afterTest() {
        if (persistController != null) {
            persistController.cleanUp();
        }
    }

    @Test
    public void testFindMax() throws Exception {
        final GameHistory.Key key = new GameHistory.Key(666, 999, 'Z');

        final GameHistory.Entry entry1 = new GameHistory.Entry(key, 222, 0.664);
        final GameHistory.Entry entry2 = new GameHistory.Entry(key, 333, 0.666);
        final GameHistory.Entry entry3 = new GameHistory.Entry(key, 111, 0.564);

        when(mockHistory.getEntries()).thenReturn(Arrays.asList(entry1, entry2, entry3));

        persistController.save(mockHistory);

        final GameHistory.Entry foundEntry = persistController.findMax(key);
        Assert.assertEquals("Weight", entry2.getWeight(), foundEntry.getWeight());

        persistController.delete(persistController.find(entry1));
        persistController.delete(persistController.find(entry2));
        persistController.delete(persistController.find(entry3));
    }

    @Test
    public void testNoFind() throws Exception {
        final GameHistory.Key key = new GameHistory.Key(111, 222, 'S');
        Assert.assertNull("Not found", persistController.findMax(key));
        final GameHistory.Entry entry = new GameHistory.Entry(key, 333, 0.000);
        Assert.assertNull("Not found", persistController.find(entry));
    }

    @Test
    public void testBadHost() throws Exception {
        thrown.expect(PersistController.PersistenceException.class);
        new PersistController("badHost", "test").save(mockHistory);
    }

    @Ignore @Test
    public void testWriteError() throws Exception {
        final GameHistory.Key key = new GameHistory.Key(666, 999, 'Z');
        final GameHistory.Entry e = new GameHistory.Entry(key, 222, 0.664);

        //TODO: Negative test of write error some how??
        thrown.expect(PersistController.PersistenceException.class);
        persistController.delete(e);
    }
}
