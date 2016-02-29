package com.cloupix.blindr.business;

/**
 * Created by alonsousa on 15/12/15.
 *
 */
public class MapSector {

    // [ N, E, S, W ]
    private boolean[] stroke = new boolean[]{false, false, false, false};

    public MapSector() {

    }

    public boolean isnStroke() {
        return stroke[0];
    }

    public void setnStroke(boolean nStroke) {
        this.stroke[0] = nStroke;
    }

    public boolean iseStroke() {
        return stroke[1];
    }

    public void seteStroke(boolean eStroke) {
        this.stroke[1] = eStroke;
    }

    public boolean issStroke() {
        return stroke[2];
    }

    public void setsStroke(boolean sStroke) {
        this.stroke[2] = sStroke;
    }

    public boolean iswStroke() {
        return stroke[3];
    }

    public void setwStroke(boolean wStroke) {
        this.stroke[3] = wStroke;
    }

    public boolean[] getStroke() {
        return stroke;
    }

    public void setStroke(boolean[] stroke) {
        this.stroke = stroke;
    }
}
