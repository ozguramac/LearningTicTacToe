package com.oz.game.tictactoe.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotDefault;

/**
 * Created by developer on 10/15/15.
 */
@Entity
public class PersistBeanEntry {
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

    public Long getDbId() {
        return dbId;
    }

    void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public int getX() {
        return x;
    }

    public int getO() {
        return o;
    }

    public int getT() {
        return t;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setO(int o) {
        this.o = o;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getL() {
        return l;
    }

    public double getW() {
        return w;
    }

    public void setL(int l) {
        this.l = l;
    }

    public void setW(double w) {
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
