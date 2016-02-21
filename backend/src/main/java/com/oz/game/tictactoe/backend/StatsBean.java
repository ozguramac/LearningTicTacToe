package com.oz.game.tictactoe.backend;

/**
 * TicTacToe statistics model we are transporting through endpoint
 */
public class StatsBean {
    private int lastCount = 0;

    public int getLastCount() {
        return lastCount;
    }

    void setLastCount(int count) {
        lastCount = count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatsBean{");
        sb.append(",lastCount=").append(lastCount);
        sb.append('}');
        return sb.toString();
    }
}