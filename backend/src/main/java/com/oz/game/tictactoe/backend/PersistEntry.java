package com.oz.game.tictactoe.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by developer on 10/15/15.
 */
@Entity
public class PersistEntry {
    //Database id
    @Id private Long dbId = null;

    //State of X
    @Index private int x = -1;
    //State of O
    @Index private int o = -1;
    //Whose turn was it?
    @Index private char t = ' ';

    //Location num of the move
    private int l = -1;

    //Weight of winning possibility
    private double w = Double.NaN;

    public int getStateOfX() {
        return x;
    }

    public int getStateOfO() {
        return o;
    }

    public char getWhoseTurn() {
        return t;
    }

    public void setStateOfX(int x) {
        this.x = x;
    }

    public void setStateOfO(int o) {
        this.o = o;
    }

    public void setWhoseTurn(char t) {
        this.t = t;
    }

    public Long getDbId() {
        return dbId;
    }

    public int getMoveLocNum() {
        return l;
    }

    public double getWeight() {
        return w;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public void setMoveLocNum(int l) {
        this.l = l;
    }

    public void setWeight(double w) {
        this.w = w;
    }
}
