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

    void save(final GameHistory history)  throws PersistException {
        facade.save(history);
    }

    GameHistory.Entry find(final GameHistory.Entry entry) throws PersistException {
        return facade.find(entry);
    }

    GameHistory.Entry findBest(final GameHistory.Key key) throws PersistException {
        final long start = System.currentTimeMillis();
        final PersistEntry found = facade.find(key);
        final long span = System.currentTimeMillis() - start;
        if (span > 1000) {
            log.info("PersistController find best query took "+(span/1000d)+" seconds!");
        }
        if (null == found) {
            return null;
        }
        return new GameHistory.Entry(found);
    }

    void delete(final GameHistory.Entry entry) throws PersistException {
        facade.delete(entry);
    }
}

