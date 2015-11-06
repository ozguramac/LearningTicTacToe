package com.oz.game.tictactoe.impl;

import android.support.annotation.NonNull;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.LearningTicTacToeApi;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.model.PersistBean;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.model.PersistBeanEntry;
import com.oz.game.tictactoe.core.persist.PersistContainer;
import com.oz.game.tictactoe.core.persist.PersistEntry;
import com.oz.game.tictactoe.core.persist.PersistenceException;
import com.oz.game.tictactoe.core.persist.PersistFacade;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by developer on 10/23/15.
 */
public final class PersistFacadeImpl extends PersistFacadeBase {
        private final LearningTicTacToeApi api = new LearningTicTacToeApi.Builder(
             AndroidHttp.newCompatibleTransport()
            ,new AndroidJsonFactory()
            ,null)
            .setRootUrl("http://10.0.2.2:8080/_ah/api")
            .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> gcr) throws IOException {
                    gcr.setDisableGZipContent(true);
                }
            })
            .build();

    @Override
    protected LearningTicTacToeApi getApi() {
        return api;
    }
}
