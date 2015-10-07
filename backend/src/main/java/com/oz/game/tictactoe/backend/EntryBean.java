package com.oz.game.tictactoe.backend;

/**
 * TicTacToe game entry model we are sending through endpoints
 */
public class EntryBean {

    //Database id
    private Object dbId;

    //State of X and O
    private int x, o;
    //Whose turn was it?
    private char t;

    //Location num of the move
    private int l;

    //Weight of winning possibility
    private double w;

    public EntryBean() {
    }

    EntryBean(final Object dbId, final int x, final int o, final char t, final int l, final double w) {
        this.dbId = dbId;
        this.x = x;
        this.o = o;
        this.t = t;
        this.l = l;
        this.w = w;
    }


    public Object getDbId() {
        return dbId;
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

    public int getMoveLocNum() {
        return l;
    }

    public double getWeight() {
        return w;
    }
}