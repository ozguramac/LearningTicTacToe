package com.oz.game.tictactoe.core;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by developer on 7/11/15.
 */
class GameHistory {
    private static final Logger log = Logger.getLogger(GameHistory.class.getName());

    private final PersistController persistController;
    private final Collection<Entry> entries = new LinkedList<>();

    private int numBestMoveFinds = 0;

    static class Key {
        private final int x; //state of X
        private final int o; //state of O
        private final char t; //game piece with the turn

        Key(final int x, final int o, final char t) {
            this.o = o;
            this.t = t;
            this.x = x;
        }

        private Key(final GameState state, final GameState.GamePiece gp) {
            this(state.of(GameState.GamePiece.X), state.of(GameState.GamePiece.O), gp.toChar());
        }

        int getStateOfX() {
            return x;
        }

        int getStateOfO() {
            return o;
        }

        char getWhoseTurn() {
            return t;
        }
    }

    static class Entry extends Key {
        private final int l; //location num of the move
        private double w; //probability weight of move winning [0,1]
        private Object dbId = null; //database id

        private Entry(final int x, final int o, final char t, final int l) {
            super(x, o, t);
            this.l = l;
            this.w = 0.5; //Either way probable by default
        }

        private Entry(final GameState state, final GameState.Spot spot, final GameState.GamePiece gp) {
            this(state.of(GameState.GamePiece.X), state.of(GameState.GamePiece.O)
                    , gp.toChar(), spot.toNum());
        }

        Entry (final Key key, final int l, final double w, final Object dbId) {
            this(key.getStateOfX(), key.getStateOfO(), key.getWhoseTurn(), l);
            this.w = w;
            this.dbId = dbId;
        }

        Entry (final Key key, final int l, final double w) {
            this(key, l ,w, null);
        }

        private void merge(final Entry e) {
            this.w = e.w;
            this.dbId = e.dbId;
        }

        private boolean matches(final GameState.GamePiece gp) {
            return gp.toChar() == getWhoseTurn();
        }

        private void winner() {
            w = (w + 1.0) / 2.0;
        }

        private void loser() { w /= 2.0; }

        private void tie() { w = (w + 0.5) / 2.0; }

        int getMoveLocLum() {
            return l;
        }

        double getWeight() {
            return w;
        }

        Object getDbId() {
            return dbId;
        }
    }

    GameHistory(final PersistController persistController) {
        this.persistController = persistController;
    }

    void addMove(final GameState state, final GameState.Spot spot, final GameState.GamePiece gp) {
        entries.add(new Entry(state, spot, gp));
    }

    Iterable<Entry> getEntries() {
        return Collections.unmodifiableCollection(entries);
    }

    void persist(final GameState.GamePiece winner) {
        try {
            for (Entry entry : entries) {
                //Pull from db
                final Entry fullEntry;
                if ((fullEntry = persistController.find(entry)) != null) {
                    entry.merge(fullEntry); //update weight
                }

                //Update weight
                if (winner == GameState.GamePiece.NONE) {
                    entry.tie();
                }
                else if (entry.matches(winner)) {
                    entry.winner();
                }
                else {
                    entry.loser();
                }
            }

            //Persist to db
            persistController.save(this);
        }
        catch (PersistController.PersistenceException e) {
            log.throwing("Game History", "persist", e);
        }
    }

    GameState.Spot getBestMove(final GameState state, final GameState.GamePiece gp) {
        //Find best move from history
        final Key key = new Key(state, gp);
        try {
            final Entry found = persistController.findBest(key);
            if (found != null && found.getWeight() > 0.5d) { //Found and better than 50-50 prob!
                numBestMoveFinds++;
                return new GameState.Spot(found.getMoveLocLum());
            }
        }
        catch (PersistController.PersistenceException e) {
            log.throwing("Game History", "get best move", e);
        }

        //Else randomly choose from empty spots
        final List<GameState.Spot> empties = state.getEmptySpots();
        final Random random = new Random(System.currentTimeMillis());
        final int randSpotIdx = random.nextInt(empties.size());
        final GameState.Spot spot = empties.get(randSpotIdx);
        return spot;
    }

    double getPercOfBestMoveFinds() {
        return numBestMoveFinds / ((double) entries.size());
    }
}
