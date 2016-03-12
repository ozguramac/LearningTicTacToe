/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.oz.game.tictactoe.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import java.io.Closeable;
import java.util.Map;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "learningTicTacToeApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.tictactoe.game.oz.com",
                ownerName = "backend.tictactoe.game.oz.com",
                packagePath = ""
        )
)
public class BackEndpoint {
    private static final Logger log = Logger.getLogger(BackEndpoint.class.getName());

    static {
        ObjectifyService.register(PersistBeanEntry.class);
    }

    @ApiMethod(name = "save")
    public PersistBean save(final PersistBean persistBean) throws BackendException {
        try {
            final Closeable c = ObjectifyService.begin();
            try {
                final PersistBeanEntry[] entries = persistBean.getEntries();
                for (PersistBeanEntry entry : entries) {
                    //Pull from db
                    final PersistBeanEntry fullEntry;
                    if ((fullEntry = load(entry)) != null) {
                        entry.populate(fullEntry);
                    }

                    //Update weight
                    final int winner = persistBean.getWinner();
                    if (Character.isSpaceChar(winner)) {
                        entry.tie();
                    }
                    else if (winner == entry.getT()) {
                        entry.winner();
                    }
                    else {
                        entry.loser();
                    }

                    //Increment num of times used as move
                    entry.incrementUsage();
                }


                final Map<Key<PersistBeanEntry>, PersistBeanEntry> map = ofy().save()
                        .entities(persistBean.getEntries())
                        .now();

                for (Map.Entry<Key<PersistBeanEntry>, PersistBeanEntry> e : map.entrySet()) {
                    for (PersistBeanEntry pe : persistBean.getEntries()) {
                        if (e.getValue() == pe) {
                            pe.populate(e.getKey());
                        }
                    }
                }

                return persistBean;
            }
            finally {
                c.close();
            }
        }
        catch (Throwable t) {
            log.throwing(getClass().getName(), "save", t);
            throw new BackendException("Could not persist "+persistBean);
        }
    }

    @ApiMethod(name = "find")
    public PersistBeanEntry load(final PersistBeanEntry entry) throws BackendException {
        try {
            final Closeable c = ObjectifyService.begin();
            try {
                return ofy().load().type(PersistBeanEntry.class)
                        .filter("x", entry.getX())
                        .filter("o", entry.getO())
                        .filter("t", entry.getT())
                        .filter("l", entry.getL())
                        .first()
                        .now();
            }
            finally {
                c.close();
            }
        }
        catch (Throwable t) {
            log.throwing(getClass().getName(), "load", t);
            throw new BackendException("Could not find "+entry);
        }
    }

    @ApiMethod(name = "findBest")
    public PersistBeanEntry findMax(final PersistBeanEntry entry) throws BackendException {
        try {
            final Closeable c = ObjectifyService.begin();
            try {
                //find both symmetric results and return better
                final PersistBeanEntry
                         pbe1 = findMax(entry.getX(), entry.getO())
                        ,pbe2 = findMax(entry.getO(), entry.getX());

                if (null == pbe1 && pbe2 != null) {
                    return pbe2;
                }

                if (pbe1 != null && null == pbe2) {
                    return pbe1;
                }

                if (pbe1.getW() > pbe2.getW()) {
                    return pbe2;
                }

                return pbe1;
            }
            finally {
                c.close();
            }
        }
        catch (Throwable t) {
            log.throwing(getClass().getName(), "findMax", t);
            throw new BackendException("Could not find best like "+entry);
        }
    }

    private PersistBeanEntry findMax(final int state1, final int state2) {
        return ofy().load().type(PersistBeanEntry.class)
                .filter("x", state1)
                .filter("o", state2)
                .filter("w >", 0.5) //Filter for better than 50-50 prob!
                .order("-w")
                .first()
                .now();
    }

    @ApiMethod(name = "delete")
    public void remove(final PersistBeanEntry entry) throws BackendException {
        try {
            final Closeable c = ObjectifyService.begin();
            try {
                ofy().delete()
                        .entity(entry)
                        .now();
            }
            finally {
                c.close();
            }
        }
        catch (Throwable t) {
            log.throwing(getClass().getName(), "remove", t);
            throw new BackendException("Could not delete "+entry);
        }
    }

    @ApiMethod(name = "stats")
    public StatsBean getStats() throws BackendException {
        try {
            final Closeable c = ObjectifyService.begin();
            try {
                final int cnt = ofy().load()
                        .type(PersistBeanEntry.class)
                        .count();

                final StatsBean statsBean = new StatsBean();
                statsBean.setLastCount(cnt);
                return statsBean;
            }
            finally {
                c.close();
            }
        }
        catch (Throwable t) {
            log.throwing(getClass().getName(), "stats", t);
            throw new BackendException("Could not get stats");
        }
    }
}
