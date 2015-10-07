/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.oz.game.tictactoe.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

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

    /**
     * Save entries
     */
    @ApiMethod(name = "save")
    public void save(@Named("entries") final EntryBean[] entries) {

    }

    //TODO: Model after persist controller's interaction with mongo db

}
