package com.oz.game.tictactoe.core;

import java.util.logging.Logger;

/**
 * Created by developer on 7/13/15.
 */
class PersistController {
    private static final Logger log = Logger.getLogger(PersistController.class.getName());

    private final PersistFacade facade;

    PersistController(final PersistFacade facade) {
        this.facade = facade;
    }

    void save(final GameHistory history)  throws PersistenceException {
        facade.persist(history);
    }

    GameHistory.Entry find(final GameHistory.Entry entry) throws PersistenceException {
        final PersistEntry fullEntry = facade.match(entry);
        return new GameHistory.Entry(entry, entry.getMoveLocNum(), fullEntry.getWeight());
    }

    GameHistory.Entry findBest(final GameHistory.Key key) throws PersistenceException {
        final long start = System.currentTimeMillis();
        final PersistEntry found = facade.matchBest(key);
        final long span = System.currentTimeMillis() - start;
        if (span > 1000) {
            log.info("PersistController find best query took "+(span/1000d)+" seconds!");
        }
        if (null == found) {
            return null;
        }
        return new GameHistory.Entry(key, found.getMoveLocNum(), found.getWeight());
    }

    void delete(final GameHistory.Entry entry) throws PersistenceException {
        facade.delete(entry);
    }
}

