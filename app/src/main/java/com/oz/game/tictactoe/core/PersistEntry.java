package com.oz.game.tictactoe.core;

/**
 * Created by developer on 10/22/15.
 */
public interface PersistEntry {
    int getStateOfX();

    int getStateOfO();

    int getWhoseTurn();

    int getMoveLocNum();

    double getWeight();
}
