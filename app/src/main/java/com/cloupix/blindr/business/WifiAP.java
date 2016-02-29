package com.cloupix.blindr.business;

import java.util.ArrayList;

/**
 * Created by alonsousa on 6/12/15.
 *
 */
public class WifiAP {


    /**
     * The network name.
     */
    public String SSID;

    /**
     * Ascii encoded SSID. This will replace SSID when we deprecate it. @hide
     * Lo dejamos para más a delante
     */
    //public WifiSsid wifiSsid;

    /**
     * The address of the access point.
     */
    public String BSSID;

    private ArrayList<WifiAPLecture> lectures;

    public WifiAP() {
    }

    public WifiAP(String SSID, String BSSID) {
        this.SSID = SSID;
        this.BSSID = BSSID;
    }

    public WifiAP(String SSID, String BSSID, ArrayList<WifiAPLecture> lectures) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.lectures = lectures;
    }

    public WifiAP(String SSID, String BSSID, final WifiAPLecture lecture) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.lectures = new ArrayList<WifiAPLecture>(){{add(lecture);}};
    }

    public String getSSSID() {
        return SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public ArrayList<WifiAPLecture> getLectures() {
        return lectures;
    }

    public void setSSID(String ssid) {
        this.SSID = ssid;
    }

    public void setBSSID(String mac) {
        this.BSSID = BSSID;
    }

    public void setLectures(ArrayList<WifiAPLecture> lectures) {
        this.lectures = lectures;
    }

    public void addWifiAPLecture(WifiAPLecture lecture) {
        lectures.add(lecture);
    }

    public void addWifiAPLectures(ArrayList<WifiAPLecture> lectures) {
        lectures.addAll(lectures);
    }
}
