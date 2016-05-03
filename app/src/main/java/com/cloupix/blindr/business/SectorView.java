package com.cloupix.blindr.business;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 23/04/16.
 *
 */
public class SectorView extends Sector {

    public static final int STROKE_N = 0;
    public static final int STROKE_E = 1;
    public static final int STROKE_S = 2;
    public static final int STROKE_W = 3;
    public static final int STROKE_NW_SE = 4;
    public static final int STROKE_NE_SW = 5;

    public static final boolean[] STROKE_EMPTY_SECTOR = new boolean[]{false, false, false, false, false, false};
    // Visual properties
    // [ N, E, S, W ]
    private boolean[] stroke = STROKE_EMPTY_SECTOR;

    private boolean scanned = false;
    private long sectorViewId;

    public SectorView() {
    }

    public SectorView(long sectorId, boolean[] stroke, boolean scanned) {
        super(sectorId);
        this.stroke = stroke;
        this.scanned = scanned;
    }

    // Seguramente aqui tengamos que poner lo de completado o no. Esta clase es solo visual,
    // aqui no guardamso nada de info (que yo recuerde al menos, ya veremos que ando un poco
    // perdido que hace 4 meeses que no toco esto)

    public boolean isnStroke() {
        return stroke[STROKE_N];
    }

    public void setnStroke(boolean nStroke) {
        this.stroke[STROKE_N] = nStroke;
    }

    public boolean iseStroke() {
        return stroke[STROKE_E];
    }

    public void seteStroke(boolean eStroke) {
        this.stroke[STROKE_E] = eStroke;
    }

    public boolean issStroke() {
        return stroke[STROKE_S];
    }

    public void setsStroke(boolean sStroke) {
        this.stroke[STROKE_S] = sStroke;
    }

    public boolean iswStroke() {
        return stroke[STROKE_W];
    }

    public void setwStroke(boolean wStroke) {
        this.stroke[STROKE_W] = wStroke;
    }

    public boolean isnwseStroke() {
        return stroke[STROKE_NW_SE];
    }

    public void setnwseStroke(boolean nwseStroke) {
        this.stroke[STROKE_NW_SE] = nwseStroke;
    }

    public boolean isneswStroke() {
        return stroke[STROKE_NE_SW];
    }

    public void setneswStroke(boolean neswStroke) {
        this.stroke[STROKE_NE_SW] = neswStroke;
    }


    public boolean[] getStroke() {
        return stroke;
    }

    public boolean getStroke(int ref) {
        return stroke[ref];
    }

    public void setStroke(boolean[] stroke) {
        this.stroke = stroke;
    }

    public void setStroke(int ref, int val){
        stroke[ref] = val>0;
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    public void setScanned(int scanned) {
        this.scanned = scanned>0;
    }

    public long getSectorViewId() {
        return sectorViewId;
    }

    public void setSectorViewId(long sectorViewId) {
        this.sectorViewId = sectorViewId;
    }

    public void addWifiAPWithCheck(WifiAP newWifiAP){
        // Si ya esta en la lista no lo volvemso a meter
        for(WifiAP wifiAP: wifiAPs){
            if(TextUtils.equals(wifiAP.getBSSID(), newWifiAP.getBSSID()))
                return;
        }
        wifiAPs.add(newWifiAP);
    }

    public void mergeSector(Sector sector) {
        super.setSectorId(sector.getSectorId());
        super.setListN(sector.getListN());
        super.setMatrixX(sector.getMatrixX());
        super.setMatrixY(sector.getMatrixY());
        super.setMapId(sector.getMapId());
        super.setLatitude(sector.getLatitude());
        super.setLongitude(sector.getLongitude());
    }
}
