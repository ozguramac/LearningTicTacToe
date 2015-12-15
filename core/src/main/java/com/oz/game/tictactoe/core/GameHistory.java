package com.oz.game.tictactoe.core;

import com.oz.game.tictactoe.core.persist.PersistContainer;
import com.oz.game.tictactoe.core.persist.PersistEntry;
import com.oz.game.tictactoe.core.persist.PersistenceException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by developer on 7/11/15.
 */
class GameHistory implements PersistContainer {
    private static final Logger log = Logger.getLogger(GameHistory.class.getName());

    private final PersistController persistController;
    private final Collection<Entry> entries = new LinkedList<>();

    private GameState.GamePiece winner = GameState.GamePiece.NONE;
    private int numBestMoveFinds = 0;
    private int numOfExplorations = 0;

    static class Key implements PersistEntry {
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

        public int getStateOfX() {
            return x;
        }

        public int getStateOfO() {
            return o;
        }

        public char getWhoseTurn() {
            return t;
        }

        public Integer getMoveLocNum() {
            return null;
        }
    }

    static class Entry extends Key {
        private final int l; //location num of the move

        private Entry(final int x, final int o, final char t, final int l) {
            super(x, o, t);
            this.l = l;
        }

        private Entry(final GameState state, final GameState.Spot spot, final GameState.GamePiece gp) {
            this(state.of(GameState.GamePiece.X), state.of(GameState.GamePiece.O)
                    , gp.toChar(), spot.toNum());
        }

        Entry (final Key k, int l) {
            this(k.getStateOfX(), k.getStateOfO(), k.getWhoseTurn(), l);
        }

        @Override
        public Integer getMoveLocNum() {
            return l;
        }
    }

    GameHistory(final PersistController persistController) {
        this.persistController = persistController;
    }

    void addMove(final GameState state, final GameState.Spot spot, final GameState.GamePiece gp) {
        entries.add(new Entry(state, spot, gp));
    }

    public Iterable<Entry> getEntries() {
        return Collections.unmodifiableCollection(entries);
    }

    void persist(final GameState.GamePiece winner) {
        try {
            //Persist to db
            this.winner = winner;
            persistController.save(this);
        }
        catch (PersistenceException e) {
            log.throwing("Game History", "persist", e);
        }
    }

    @Override
    public char getWinner() {
        return winner.toChar();
    }

    GameState.Spot getBestMove(final GameState state, final GameState.GamePiece gp) {
        final Random random = new Random(System.currentTimeMillis());

        //Exploratory possibility of reinforcement learning:
        //Only do greedy lookup half of the time to explore new possibilities
        if (random.nextBoolean()) {
            //Find best move from history
            final Key key = new Key(state, gp);
            try {
                final Entry found = persistController.findBest(key);
                if (found != null) {
                    numBestMoveFinds++;
                    return new GameState.Spot(found.getMoveLocNum());
                }
            } catch (PersistenceException e) {
                log.throwing("Game History", "get best move", e);
            }
        }
        else {
            numOfExplorations++;
        }

        //Else randomly choose from empty spots
        final List<GameState.Spot> empties = state.getEmptySpots();
        final int randSpotIdx = random.nextInt(empties.size());
        final GameState.Spot spot = empties.get(randSpotIdx);
        return spot;
    }

    double getPercOfBestMoveFinds() {
        return numBestMoveFinds / ((double) entries.size());
    }

    double getPercOfExploratoryMoves() { return numOfExplorations / ((double) entries.size()); }
}
