package com.oz.game.tictactoe.backend;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Closeable;
import java.util.logging.Logger;

/**
 * Created by developer on 10/6/15.
 */
public class TestPersistEndpoint {
    private static final Logger log = Logger.getLogger(TestPersistEndpoint.class.getName());

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
             new LocalDatastoreServiceTestConfig()
    );

    private PersistEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        endpoint = new PersistEndpoint();
        helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
        endpoint = null;
    }

    @Test
    public void testSave() throws Exception {
        final PersistBean savedPb = save();
        for (PersistBeanEntry savedPe : savedPb.getEntries()) {
            Assert.assertNotNull("No db id set", savedPe.getDbId());
        }
        log.info("Saved " + savedPb);
    }

    @Test
    public void testFind() throws Exception {
        final PersistBean savedPb = save();
        final PersistBeanEntry pe = savedPb.getEntries()[0];

        log.info("Searching for "+pe);
        final PersistBeanEntry foundPe = endpoint.load(pe);
        Assert.assertNotNull(foundPe);
        log.info("Found "+foundPe);
    }

    @Test
    public void testFindMax() throws Exception {
        final PersistBean savedPb = save();
        final PersistBeanEntry pe = savedPb.getEntries()[0];

        log.info("Searching max like " + pe);
        final PersistBeanEntry bestPe = endpoint.findMax(pe);
        Assert.assertNotNull(bestPe);
        Assert.assertTrue(bestPe.getW() > pe.getW());
        log.info("Found max "+bestPe);
    }

    @Test
    public void testDelete() throws Exception {
        final PersistBean savedPb = save();
        final PersistBeanEntry pe = savedPb.getEntries()[0];

        log.info("Deleting " + pe);
        endpoint.remove(pe);

        log.info("Searching for " + pe);
        Assert.assertNull(endpoint.load(pe));
    }

    private PersistBean save() throws PersistException {
        final PersistBeanEntry pe0 = new PersistBeanEntry();
        pe0.setO(666);
        pe0.setX(666);
        pe0.setT('X');
        pe0.setL(666);
        pe0.setW(0.666);

        final PersistBeanEntry pe1 = new PersistBeanEntry();
        pe1.setO(666);
        pe1.setX(666);
        pe1.setT('X');
        pe1.setL(888);
        pe1.setW(0.999);

        final PersistBean pb = new PersistBean();
        pb.setEntries(new PersistBeanEntry[]{
                pe0
                , pe1
        });

        log.info("Saving " + pb);
        return endpoint.save(pb);
    }
}
