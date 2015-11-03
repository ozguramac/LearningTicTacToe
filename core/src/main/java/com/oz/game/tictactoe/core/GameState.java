package com.oz.game.tictactoe.core;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by developer on 3/17/15.
 */
class GameState {
    private static final int DIM = 3; //TODO: Make dimensions configurable
    private final byte[][] stateMatrix = new byte[DIM][DIM];
    private final GameHistory history;

    static enum GamePiece {
        NONE(0, ' '),
        O(1, 'O'),
        X(2, 'X');

        private final byte id;
        private final char c;

        private GamePiece(final int id, final char c) {
            this.id = (byte) id;
            this.c = c;
        }

        static GamePiece valueOf(final byte id) {
            for (GamePiece gp : GamePiece.values()) {
                if (gp.id == id) {
                    return gp;
                }
            }
            return NONE;
        }

        char toChar() {
            return c;
        }
    }

    static class Spot {
        private final int i, j;

        Spot(final int i, final int j) {
            this.i = i;
            this.j = j;
        }

        Spot(final int locationNum) {
            this(locationNum/DIM, locationNum%DIM);
        }

        int toNum() {
            return (i*DIM + j);
        }

        List<Integer> toCoordinates() {
            return Collections.unmodifiableList(Arrays.asList(i,j));
        }

        @Override
        public boolean equals(Object o) {
            if (super.equals(o)) {
                return true;
            }
            if (null == o || false == o instanceof Spot) {
                return false;
            }
            final Spot other = (Spot)o;
            return other.toNum() == this.toNum();
        }
    }

    GameState(final GameHistory history) {
        this.history = history;

        for (byte[] row : stateMatrix) {
            Arrays.fill(row, GamePiece.NONE.id); //init
        }
    }

    GamePiece get(final Spot spot) {
        return GamePiece.valueOf(stateMatrix[spot.i][spot.j]);
    }

    void set(final GamePiece gp, final Spot spot) {
        history.addMove(this, spot, gp); //add move pre-transition of state
        stateMatrix[spot.i][spot.j] = gp.id;
    }

    int count(final GamePiece gp) {
        int num = 0;
        for (int i=0; i < DIM; i++) {
            for (int j=0; j < DIM; j++ ) {
                if (gp.id == stateMatrix[i][j]) {
                    num++;
                }
            }
        }
        return num;
    }

    int getNumOfEmptySpots() {
        return count(GamePiece.NONE);
    }

    List<Spot> getEmptySpots() {
        final List<Spot> emptySpots = new ArrayList<>();
        for (int i=0; i < DIM; i++) {
            for (int j=0; j < DIM; j++ ) {
                if (GamePiece.NONE.id == stateMatrix[i][j]) {
                    emptySpots.add(new Spot(i, j));
                }
            }
        }
        return Collections.unmodifiableList(emptySpots);
    }

    int of(final GamePiece gp) {
        int state = 0b000000000;
        for (int i=0; i < DIM; i++) {
            for (int j=0; j < DIM; j++ ) {
                if (stateMatrix[i][j] == gp.id) {
                    state |= 1 << new Spot(i,j).toNum();
                }
            }
        }
        return state;
    }

    GameHistory getHistory() {
        return history;
    }

    void print(PrintStream out) {
        out.println();
        for (int i=0; i < DIM; i++) {
            for (int j=0; j < DIM; j++ ) {
                out.print(' ');
                out.print(GamePiece.valueOf(stateMatrix[i][j]).toChar());
                out.print(' ');
                if (j < (DIM - 1)) {
                    out.print('|');
                }
            }
            out.println();
            if (i < (DIM - 1)) {
                out.println("---+---+---");
            }
        }
        out.println();
    }
}


