/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.oz.game.tictactoe.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.ObjectifyService;

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
    static {
        ObjectifyService.register(PersistEntry.class);
    }

    @ApiMethod(name = "save")
    public void save(final PersistBean persistBean) throws PersistException {
        ofy().save().entities(persistBean.getEntries()).now();
    }

    @ApiMethod(name = "find")
    public PersistEntry find(final PersistEntry entry) throws PersistException {
        //TODO
        throw new UnsupportedOperationException();
    }

    @ApiMethod(name = "findBest")
    public PersistEntry findBest(final PersistEntry partialEntry) throws PersistException {
        //TODO
        throw new UnsupportedOperationException();
    }

    @ApiMethod(name = "delete")
    public void delete(final PersistEntry entry) throws PersistException {
        //TODO
        throw new UnsupportedOperationException();
    }
}
