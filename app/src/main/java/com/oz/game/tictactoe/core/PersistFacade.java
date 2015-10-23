package com.oz.game.tictactoe.core;

/**
 * Created by developer on 10/23/15.
 */
public interface PersistFacade
{
    public <C extends PersistContainer> C save(C persistContainer) throws PersistException;

    public <E extends PersistEntry> E find(E entry) throws PersistException;

    public <E extends PersistEntry> E findMax(E entry) throws PersistException;

    public <E extends PersistEntry> void delete(E entry) throws PersistException;
}
