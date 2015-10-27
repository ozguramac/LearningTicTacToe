package com.oz.game.tictactoe.impl;

import android.support.annotation.NonNull;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.LearningTicTacToeApi;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.model.PersistBean;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.model.PersistBeanEntry;
import com.oz.game.tictactoe.core.PersistContainer;
import com.oz.game.tictactoe.core.PersistEntry;
import com.oz.game.tictactoe.core.PersistenceException;
import com.oz.game.tictactoe.core.PersistFacade;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
    public void persist(final PersistContainer persistContainer) throws PersistenceException {
        final PersistBean content = new PersistBean();
        final List<PersistBeanEntry> entries = new LinkedList<>();
        for (PersistEntry pe: persistContainer.getEntries()) {
            entries.add(convert(pe));
        }
        content.setEntries(entries);
        try {
            api.save(content).execute();
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to persist entry", e);
        }
    }

    @Override
    public PersistEntry match(final PersistEntry entry) throws PersistenceException {
        try {
            final PersistBeanEntry found = api.find(convert(entry)).execute();
            return new PersistEntryImpl(found);
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to match entry", e);
        }
    }

    @Override
    public PersistEntry matchBest(final PersistEntry entry) throws PersistenceException {
        try {
            final PersistBeanEntry found = api.findBest(convert(entry)).execute();
            return new PersistEntryImpl(found);
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to find the best matching entry", e);
        }
    }

    @Override
    public void delete(final PersistEntry entry) throws PersistenceException {
        try {
            api.delete(convert(entry)).execute();
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to delete entry", e);
        }
    }

    @NonNull
    private static PersistBeanEntry convert(final PersistEntry pe) {
        final PersistBeanEntry entry = new PersistBeanEntry();
        entry.setX(pe.getStateOfX());
        entry.setO(pe.getStateOfO());
        entry.setT((int) pe.getWhoseTurn());
        entry.setL(pe.getMoveLocNum());
        entry.setW(pe.getWeight());
        return entry;
    }

    private static class PersistEntryImpl implements PersistEntry {
        private final PersistBeanEntry pbe;
        PersistEntryImpl(final PersistBeanEntry pbe) {
            this.pbe = pbe;
        }

        @Override
        public int getStateOfX() {
            return pbe.getX();
        }

        @Override
        public int getStateOfO() {
            return pbe.getO();
        }

        @Override
        public char getWhoseTurn() {
            return (char) pbe.getT().intValue();
        }

        @Override
        public int getMoveLocNum() {
            return pbe.getL();
        }

        @Override
        public double getWeight() {
            return pbe.getW();
        }
    }
}
