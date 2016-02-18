package com.oz.game.tictactoe.core.persist;

/**
 * Created by developer on 10/22/15.
 */
public interface PersistEntry {
    int getStateOfX();

    int getStateOfO();

    char getWhoseTurn();

    Integer getMoveLocNum();

    int getNumOfPlays();
}
