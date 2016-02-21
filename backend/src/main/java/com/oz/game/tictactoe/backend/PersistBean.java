package com.oz.game.tictactoe.backend;

import java.util.Arrays;

/**
 * TicTacToe persist model we are sending through endpoints
 */
public class PersistBean {
    private PersistBeanEntry[] entries = null;
    private int winner = (int)' ';

    public PersistBeanEntry[] getEntries() {
        return entries;
    }

    public void setEntries(PersistBeanEntry[] entries) {
        this.entries = entries;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersistBean{");
        sb.append("entries=").append(Arrays.toString(entries));
        if (Character.isSpaceChar(winner)) {
            sb.append(",tie");
        }
        else {
            sb.append(",winner=").append((char) winner);
        }
        sb.append('}');
        return sb.toString();
    }
}