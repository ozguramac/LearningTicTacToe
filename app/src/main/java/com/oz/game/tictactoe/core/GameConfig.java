package com.oz.game.tictactoe.core;

import com.oz.game.tictactoe.io.GameInput;
import com.oz.game.tictactoe.io.GameOutput;

/**
 * Created by developer on 8/15/15.
 */
public class GameConfig {
    private String dbHost = null;
    private String dbName;
    private PlayerType playerOne, playerTwo;
    private boolean bPrintState = false;
    private GameInput in;
    private GameOutput out;

    public boolean isPrintState() {
        return bPrintState;
    }

    public GameConfig printState(boolean bPrintState) {
        this.bPrintState = bPrintState;
        return this;
    }

    public String getDbHost() {
        return dbHost;
    }

    public GameConfig dbHost(String dbHost) {
        this.dbHost = dbHost;
        return this;
    }

    public String getDbName() {
        return dbName;
    }

    public GameConfig dbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public GameConfig input(GameInput in) {
        this.in = in;
        return this;
    }

    public GameInput getInput() {
        return in;
    }

    public GameConfig output(GameOutput out) {
        this.out = out;
        return this;
    }

    public GameOutput getOutput() {
        return out;
    }

    public PlayerType getPlayerOne() {
        return playerOne;
    }

    public GameConfig playerOne(PlayerType playerOne) {
        this.playerOne = playerOne;
        return this;
    }

    public PlayerType getPlayerTwo() {
        return playerTwo;
    }

    public GameConfig playerTwo(PlayerType playerTwo) {
        this.playerTwo = playerTwo;
        return this;
    }

    public static enum PlayerType {
         COMPUTER
        ,HUMAN
    }
}
