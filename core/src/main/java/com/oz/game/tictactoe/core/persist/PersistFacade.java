package com.oz.game.tictactoe.core.persist;

/**
 * Created by developer on 10/23/15.
 */
public interface PersistFacade
{
    public void persist(PersistContainer persistContainer) throws PersistenceException;

    public PersistEntry match(PersistEntry entry) throws PersistenceException;

    public PersistEntry matchBest(PersistEntry entry) throws PersistenceException;

    public void delete(PersistEntry entry) throws PersistenceException;

    StatsContainer getStats() throws PersistenceException;
}
