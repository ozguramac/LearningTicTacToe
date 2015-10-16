package com.oz.game.tictactoe.backend;

/**
 * Created by developer on 10/15/15.
 */
public class PersistException extends Exception {
    PersistException(final String msg) {
            super(msg);
        }

    PersistException(final String msg, final Throwable t) {
            super(msg, t);
        }
}
