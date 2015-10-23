package com.oz.game.tictactoe.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotDefault;

/**
 * Created by developer on 10/15/15.
 */
@Entity
public class PersistBeanEntry implements PersistEntry {
    //Database id
    @Id private Long dbId = null;

    //State of X
    @Index(IfNotDefault.class)
    private int x = -1;
    //State of O
    @Index(IfNotDefault.class)
    private int o = -1;
    //Whose turn was it?
    @Index(IfNotDefault.class)
    private int t = (int)' ';
    //Location num of the move
    @Index(IfNotDefault.class)
    private int l = -1;
    //Weight of winning possibility
    @Index(IfNotDefault.class)
    private double w = Double.NaN;

    @Override
    public int getStateOfX() {
        return x;
    }

    @Override
    public int getStateOfO() {
        return o;
    }

    @Override
    public int getWhoseTurn() {
        return t;
    }

    public void setStateOfX(int x) {
        this.x = x;
    }

    public void setStateOfO(int o) {
        this.o = o;
    }

    public void setWhoseTurn(int t) {
        this.t = t;
    }

    public Long getDbId() {
        return dbId;
    }

    @Override
    public int getMoveLocNum() {
        return l;
    }

    @Override
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersistBeanEntry{");
        if (dbId != null) {
            sb.append("dbId=").append(dbId).append(", ");
        }
        sb.append("x=").append(x);
        sb.append(", o=").append(o);
        sb.append(", t=").append(t);
        sb.append(", l=").append(l);
        sb.append(", w=").append(w);
        sb.append('}');
        return sb.toString();
    }
}
