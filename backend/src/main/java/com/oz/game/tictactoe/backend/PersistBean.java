package com.oz.game.tictactoe.backend;

import java.util.Arrays;

/**
 * TicTacToe persist model we are sending through endpoints
 */
public class PersistBean {
    private PersistBeanEntry[] entries = null;
    private Integer winner = null;

    public PersistBeanEntry[] getEntries() {
        return entries;
    }

    public void setEntries(PersistBeanEntry[] entries) {
        this.entries = entries;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersistBean{");
        sb.append("entries=").append(Arrays.toString(entries));
        if (null == winner) {
            sb.append(",tie");
        }
        else {
            sb.append(",winner=").append((char) winner.intValue());
        }
        sb.append('}');
        return sb.toString();
    }
}