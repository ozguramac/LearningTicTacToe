package com.oz.game.tictactoe.core;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * Created by developer on 7/13/15.
 */
class PersistController {
    private static final Logger log = Logger.getLogger(PersistController.class.getName());

    private static final String COLL_NAME = "ticTacToeHist";

    private final String host;
    private final String dbName;

    private static MongoClient mongoClient = null;
    private static DB db = null;
    private static DBCollection dbColl = null;

    PersistController(final String host, final String dbName) {
        this.host = host;
        this.dbName = dbName;
    }

    void save(final GameHistory history)  throws PersistenceException {
        final DBCollection coll = getDbCollection();
        for (GameHistory.Entry entry : history.getEntries()) {
            final DBObject dbObj = getDbObj(entry);
            final WriteResult wr = coll.save(dbObj);
            final String err = wr.getError();
            if (err != null) {
                throw new PersistenceException(err);
            }
        }
    }

    private static BasicDBObject getDbObj(final GameHistory.Entry entry) {
        final BasicDBObject dbObj = getDbObjNoWeight(entry).append("w", entry.getWeight());
        final Object dbId = entry.getDbId();
        if (dbId != null) {
            return dbObj.append("_id", dbId);
        }
        return dbObj;
    }

    private static BasicDBObject getDbObjForKey(final GameHistory.Key k) {
        return new BasicDBObject()
                .append("x", k.getStateOfX())
                .append("o", k.getStateOfO())
                .append("t", k.getWhoseTurn())
                ;
    }

    private static BasicDBObject getDbObjNoWeight(final GameHistory.Entry e) {
        return getDbObjForKey(e)
                .append("l", e.getMoveLocLum())
                ;
    }

    GameHistory.Entry find(final GameHistory.Entry entry) throws PersistenceException {
        final DBCollection coll = getDbCollection();
        final DBObject dbObj = getDbObjNoWeight(entry);
        final DBObject fullDbObj = coll.findOne(dbObj);
        if (null == fullDbObj) {
            return null;
        }
        return create(entry, fullDbObj);
    }

    private static GameHistory.Entry create(GameHistory.Key key, DBObject dbObj) {
        final int l = (Integer) dbObj.get("l");
        final double w = (Double) dbObj.get("w");
        final Object dbId = dbObj.get("_id");
        return new GameHistory.Entry(key, l, w, dbId);
    }

    GameHistory.Entry findMax(final GameHistory.Key key) throws PersistenceException {
        final DBCollection coll = getDbCollection();
        final DBObject dbObj = getDbObjForKey(key);
        final long start = System.currentTimeMillis();
        final DBObject found = coll.findOne(dbObj
                , new BasicDBObject()
                .append("_id", true) //return id
                .append("l", true) //return location
                .append("w", true) //return weight
                , new BasicDBObject()
                .append("w", -1) //order by
        );
        final long span = System.currentTimeMillis() - start;
        if (span > 1000) {
            log.info("PersistController findMax query took "+(span/1000d)+" seconds!");
        }
        if (null == found) {
            return null;
        }
        return create(key, found);
    }

    void delete(final GameHistory.Entry entry) throws PersistenceException {
        final DBCollection coll = getDbCollection();
        final DBObject dbObj = getDbObj(entry);
        final WriteResult wr = coll.remove(dbObj);
        final String err = wr.getError();
        if (err != null) {
            throw new PersistenceException(err);
        }
    }

    static void cleanUp() {
        if (dbColl != null) {
            dbColl = null;
            db.cleanCursors(true);
            db = null;
            mongoClient.close();
            mongoClient = null;
        }
    }

    private DBCollection getDbCollection() throws PersistenceException {
        if (dbColl != null
                && (null == host || mongoClient.getAddress().sameHost(host))
                && db.getName().equals(dbName))
        {
            return dbColl; //reuse db collection
        }

        try {
            mongoClient = new MongoClient(host);
        }
        catch (UnknownHostException e) {//TODO: Extract resource
            throw new PersistenceException("Failed to get mongo client", e);
        }

        db = mongoClient.getDB(dbName);

        dbColl = db.getCollection(COLL_NAME);

        return dbColl;
    }

    static class PersistenceException extends Exception
    {
        private PersistenceException(final String msg) {
            super(msg);
        }

        private PersistenceException(final String msg, final Throwable t) {
            super(msg, t);
        }
    }
}

