package com.oz.game.tictactoe.core.io;

import android.util.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by developer on 9/20/15.
 */
public class GameMove {
    private final char piece;
    private final Integer[] coordinates;
    public GameMove(final char piece, final Integer... coordinates) {
        this.piece = piece;
        this.coordinates = coordinates;
    }

    public GameMove(final char piece, List<Integer> coordinates) {
        this(piece, coordinates.toArray(new Integer[0]));
    }

    public char getPiece() {
        return piece;
    }

    public Integer get(final int index) {
        if (index < 0 || index >= coordinates.length) {
            return null;
        }
        return coordinates[index];
    }

    public boolean is(final Integer... coordinates) {
        return Arrays.equals(coordinates, this.coordinates);
    }
}
