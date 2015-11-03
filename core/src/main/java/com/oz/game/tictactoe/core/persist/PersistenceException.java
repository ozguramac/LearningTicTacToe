package com.oz.game.tictactoe.core.persist;

/**
 * Created by developer on 10/15/15.
 */
public class PersistenceException extends Exception {
    public PersistenceException(final String msg) {
            super(msg);
        }

    public PersistenceException(final String msg, final Throwable t) {
            super(msg, t);
        }
}
