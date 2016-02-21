package com.oz.game.tictactoe.core.persist;

/**
 * Created by developer on 10/22/15.
 */
public interface PersistContainer {
    <E extends PersistEntry> Iterable<E> getEntries();
    char getWinner();
}
