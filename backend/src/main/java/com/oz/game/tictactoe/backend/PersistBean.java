package com.oz.game.tictactoe.backend;

/**
 * TicTacToe persist model we are sending through endpoints
 */
public class PersistBean {
    private PersistEntry[] entries = null;

    public PersistEntry[] getEntries() {
        return entries;
    }

    public void setEntries(PersistEntry[] entries) {
        this.entries = entries;
    }

}