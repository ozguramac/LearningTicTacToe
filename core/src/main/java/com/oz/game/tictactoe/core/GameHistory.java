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

    private static int numBestMoveFinds = 0;
    private static int numOfExplorations = 0;
    private static int numOfUnplayedFinds = 0;
    private static int numOfRandomMoves = 0;
    private static int numOfEntries = 0;
    private static int numOfTotalEntriesBegin = 0;
    private static int numOfTotalEntries = 0;

    private final PersistController persistController;
    private final Collection<Entry> entries = new LinkedList<>();

    private GameState.GamePiece winner = GameState.GamePiece.NONE;

    private double greedyMoveThreshold = 0.5d;

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

    @Override
    public Iterable<Entry> getEntries() {
        return Collections.unmodifiableCollection(entries);
    }

    void persist(final GameState.GamePiece winner) {
        try {
            //Persist to db
            this.winner = winner;
            persistController.save(this);
            numOfEntries += entries.size();
        }
        catch (PersistenceException e) {
            log.throwing("Game History", "persist", e);
        }
    }

    @Override
    public char getWinner() {
        return winner.toChar();
    }

    @Override
    public void setTotalCount(int totalCount) {
        numOfTotalEntries = totalCount;
        if (numOfTotalEntriesBegin == 0) {
            numOfTotalEntriesBegin = numOfTotalEntries; //once
        }
    }

    double getGreedyMoveThreshold() {
        return greedyMoveThreshold;
    }

    void setGreedyMoveThreshold(final double greedyMoveThreshold) {
        if (greedyMoveThreshold >= 0d && greedyMoveThreshold <= 1d) {
            this.greedyMoveThreshold = greedyMoveThreshold;
        }
    }

    GameState.Spot getBestMove(final GameState state, final GameState.GamePiece gp) {
        final Random random = new Random(System.currentTimeMillis());
        final Key key = new Key(state, gp);

        //Exploratory possibility of reinforcement learning:
        //Only do greedy lookup half of the time to explore new possibilities
        if (random.nextDouble() < greedyMoveThreshold) {
            //Find best move from history
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

        final List<GameState.Spot> empties = state.getEmptySpots();

        //Else try to choose a previously unplayed spot
        for (GameState.Spot empty : empties) {
            final Entry possible = new Entry(key, empty.toNum());
            try {
                if (null == persistController.find(possible)) {
                    numOfUnplayedFinds++;
                    return empty;
                }
            } catch (PersistenceException e) {
                log.throwing("Game History", "find possible spot", e);
            }
        }

        //Else randomly choose from empty spots
        final int randSpotIdx = random.nextInt(empties.size());
        final GameState.Spot spot = empties.get(randSpotIdx);
        numOfRandomMoves++;
        return spot;
    }

    static double getPercOfBestMoveFinds() {
        return numBestMoveFinds / ((double) numOfEntries);
    }

    static double getPercOfExploratoryMoves() { return numOfExplorations / ((double) numOfEntries); }

    static double getPercOfUnplayedFinds() { return numOfUnplayedFinds / ((double) numOfEntries); }

    static double getPercOfRandomMoves() { return numOfRandomMoves / ((double) numOfEntries); }

    static int getNumOfTotalEntries() { return numOfTotalEntries; }

    static int getNumOfNewlyAddedEntries() { return numOfTotalEntries - numOfTotalEntriesBegin; }

}
