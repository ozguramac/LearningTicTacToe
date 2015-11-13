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
public class PersistEndpoint {
    private static final Logger log = Logger.getLogger(PersistEndpoint.class.getName());

    static {
        ObjectifyService.register(PersistBeanEntry.class);
    }

    @ApiMethod(name = "save")
    public PersistBean save(final PersistBean persistBean) throws PersistException {
        try {
            final Closeable c = ObjectifyService.begin();
            try {
                final PersistBeanEntry[] entries = persistBean.getEntries();
                for (PersistBeanEntry entry : entries) {
                    //Pull from db
                    final PersistBeanEntry fullEntry;
                    if ((fullEntry = load(entry)) != null) {
                        entry.setW(fullEntry.getW());
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
                }


                final Map<Key<PersistBeanEntry>, PersistBeanEntry> map = ofy().save()
                        .entities(persistBean.getEntries())
                        .now();

                for (Map.Entry<Key<PersistBeanEntry>, PersistBeanEntry> e : map.entrySet()) {
                    for (PersistBeanEntry pe : persistBean.getEntries()) {
                        if (e.getValue() == pe) {
                            pe.setDbId(e.getKey().getId());
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
            throw new PersistException("Could not persist "+persistBean);
        }
    }

    @ApiMethod(name = "find")
    public PersistBeanEntry load(final PersistBeanEntry entry) throws PersistException {
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
            throw new PersistException("Could not find "+entry);
        }
    }

    @ApiMethod(name = "findBest")
    public PersistBeanEntry findMax(final PersistBeanEntry entry) throws PersistException {
        try {
            final Closeable c = ObjectifyService.begin();
            try {
                return ofy().load().type(PersistBeanEntry.class)
                        .filter("x", entry.getX())
                        .filter("o", entry.getO())
                        .filter("t", entry.getT())
                        .filter("w >", 0.5) //Filter for better than 50-50 prob!
                        .order("-w")
                        .first()
                        .now();
            }
            finally {
                c.close();
            }
        }
        catch (Throwable t) {
            log.throwing(getClass().getName(), "findMax", t);
            throw new PersistException("Could not find best like "+entry);
        }
    }

    @ApiMethod(name = "delete")
    public void remove(final PersistBeanEntry entry) throws PersistException {
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
            throw new PersistException("Could not delete "+entry);
        }
    }
}
