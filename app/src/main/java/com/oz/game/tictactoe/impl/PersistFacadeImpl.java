package com.oz.game.tictactoe.impl;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.LearningTicTacToeApi;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.model.PersistBean;
import com.oz.game.tictactoe.core.PersistContainer;
import com.oz.game.tictactoe.core.PersistEntry;
import com.oz.game.tictactoe.core.PersistException;
import com.oz.game.tictactoe.core.PersistFacade;

import java.io.IOException;

/**
 * Created by developer on 10/23/15.
 */
public class PersistFacadeImpl implements PersistFacade {
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
    public <C extends PersistContainer> C save(C pc) throws PersistException {
        //TODO
        return null;
    }

    @Override
    public <E extends PersistEntry> E find(E entry) throws PersistException {
        //TODO
        return null;
    }

    @Override
    public <E extends PersistEntry> E findMax(E entry) throws PersistException {
        //TODO
        return null;
    }

    @Override
    public <E extends PersistEntry> void delete(E entry) throws PersistException {
        //TODO
    }
}
