package com.cloupix.blindr.business;

import java.util.ArrayList;

/**
 * Created by alonsousa on 15/12/15.
 *
 */
public class Sector {

    private long sectorId;
    private int listN;
    private int matrixX;
    private int matrixY;
    private double latitude;
    private double longitude;
    private long mapId;

    ArrayList<Lecture> lectures;
    ArrayList<WifiAP> wifiAPs;



    public Sector() {
        this.lectures = new ArrayList<>();
        this.wifiAPs = new ArrayList<>();
    }

    public Sector(long sectorId){
        this.sectorId = sectorId;
    }

    public Sector(int listN, int matrixX, int matrixY, double latitude, double longitude, long mapId) {
        this.listN = listN;
        this.matrixX = matrixX;
        this.matrixY = matrixY;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapId = mapId;
    }

    public Sector(long sectorId, int listN, int matrixX, int matrixY, double latitude, double longitude, long mapId) {
        this.sectorId = sectorId;
        this.listN = listN;
        this.matrixX = matrixX;
        this.matrixY = matrixY;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapId = mapId;
        this.lectures = new ArrayList<>();
        this.wifiAPs = new ArrayList<>();
    }

    public long getSectorId() {
        return sectorId;
    }

    public void setSectorId(long sectorId) {
        this.sectorId = sectorId;
    }

    public int getListN() {
        return listN;
    }

    public void setListN(int listN) {
        this.listN = listN;
    }

    public int getMatrixX() {
        return matrixX;
    }

    public void setMatrixX(int matrixX) {
        this.matrixX = matrixX;
    }

    public int getMatrixY() {
        return matrixY;
    }

    public void setMatrixY(int matrixY) {
        this.matrixY = matrixY;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public ArrayList<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(ArrayList<Lecture> lectures) {
        this.lectures = lectures;
    }

    public ArrayList<WifiAP> getWifiAPs() {
        return wifiAPs;
    }

    public void setWifiAPs(ArrayList<WifiAP> wifiAPs) {
        this.wifiAPs = wifiAPs;
    }
}
