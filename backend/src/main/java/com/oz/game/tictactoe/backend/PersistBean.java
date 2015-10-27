package com.oz.game.tictactoe.backend;

import java.util.Arrays;

/**
 * TicTacToe persist model we are sending through endpoints
 */
public class PersistBean {
    private PersistBeanEntry[] entries = null;

    public PersistBeanEntry[] getEntries() {
        return entries;
    }

    public void setEntries(PersistBeanEntry[] entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersistBean{");
        sb.append("entries=").append(Arrays.toString(entries));
        sb.append('}');
        return sb.toString();
    }
}