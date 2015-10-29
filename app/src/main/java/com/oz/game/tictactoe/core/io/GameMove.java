package com.oz.game.tictactoe.core.io;

import android.util.Pair;

/**
 * Created by developer on 9/20/15.
 */
public class GameMove extends Pair<Integer, Integer> {
    private final char piece;
    public GameMove(final char piece, final int i, final int j) {
        super(Integer.valueOf(i), Integer.valueOf(j));
        this.piece = piece;
    }

    public GameMove(final char piece, final Pair<Integer,Integer> c) {
        this(piece, c.first, c.second);
    }

    public char getPiece() {
        return piece;
    }
}
