package com.oz.game.tictactoe.impl;

import android.support.annotation.NonNull;

import com.oz.game.tictactoe.backend.learningTicTacToeApi.LearningTicTacToeApi;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.model.PersistBean;
import com.oz.game.tictactoe.backend.learningTicTacToeApi.model.PersistBeanEntry;
import com.oz.game.tictactoe.core.persist.PersistContainer;
import com.oz.game.tictactoe.core.persist.PersistEntry;
import com.oz.game.tictactoe.core.persist.PersistFacade;
import com.oz.game.tictactoe.core.persist.PersistenceException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by developer on 11/2/15.
 */
public abstract class PersistFacadeBase implements PersistFacade {

    protected abstract LearningTicTacToeApi getApi();

    @Override
    public void persist(final PersistContainer persistContainer) throws PersistenceException {
        final PersistBean content = new PersistBean();
        content.setWinner((int)persistContainer.getWinner());
        final List<PersistBeanEntry> entries = new LinkedList<>();
        for (PersistEntry pe: persistContainer.getEntries()) {
            entries.add(convert(pe));
        }
        content.setEntries(entries);
        try {
            final PersistBean savedContent = getApi().save(content).execute();
            persistContainer.setTotalCount( savedContent.getLastCount() );
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to persist entry", e);
        }
    }

    @Override
    public PersistEntry match(final PersistEntry entry) throws PersistenceException {
        try {
            final PersistBeanEntry found = getApi().find(convert(entry)).execute();
            if (found != null) {
                return new PersistEntryImpl(found);
            }
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to match entry", e);
        }
        return  null;
    }

    @Override
    public PersistEntry matchBest(final PersistEntry entry) throws PersistenceException {
        try {
            final PersistBeanEntry found = getApi().findBest(convert(entry)).execute();
            if (found != null) {
                return new PersistEntryImpl(found);
            }
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to find the best matching entry", e);
        }
        return null;
    }

    @Override
    public void delete(final PersistEntry entry) throws PersistenceException {
        try {
            getApi().delete(convert(entry)).execute();
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
        public Integer getMoveLocNum() {
            return pbe.getL();
        }

        @Override
        public int getNumOfPlays() {
            return pbe.getN();
        }
    }
}
