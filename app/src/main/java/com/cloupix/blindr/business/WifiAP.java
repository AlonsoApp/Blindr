package com.cloupix.blindr.business;

import android.os.Parcel;
import android.os.Parcelable;

import com.cloupix.blindr.business.comparators.LevelMapComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by alonsousa on 6/12/15.
 *
 */
public class WifiAP implements Parcelable {


    /**
     * The network name.
     */
    private String SSID;

    /**
     * Ascii encoded SSID. This will replace SSID when we deprecate it. @hide
     * Lo dejamos para más a delante
     */
    //public WifiSsid wifiSsid;

    /**
     * The address of the access point.
     */
    private String BSSID;

    private ArrayList<Reading> readings;

    public WifiAP() {
        this.readings = new ArrayList<Reading>();
    }

    public WifiAP(String SSID, String BSSID) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.readings = new ArrayList<Reading>();
    }

    public WifiAP(String SSID, String BSSID, ArrayList<Reading> readings) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.readings = readings;
    }

    public WifiAP(String SSID, String BSSID, final Reading reading) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.readings = new ArrayList<Reading>(){{add(reading);}};
    }

    public String getSSID() {
        return SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public ArrayList<Reading> getReadings() {
        return readings;
    }

    public void setSSID(String ssid) {
        this.SSID = ssid;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public void setReadings(ArrayList<Reading> readings) {
        this.readings = readings;
    }

    public void addWifiAPReading(Reading reading) {
        readings.add(reading);
    }

    public void addWifiAPReadings(ArrayList<Reading> readings) {
        readings.addAll(readings);
    }

    public int getAverageRSSI(){
        int[] levels = new int[readings.size()];
        for (int i = 0; i< readings.size(); i++){
            levels[i] = readings.get(i).getLevel();
        }
        // Si queremos quitar los valores más bajos y más altos de la media tendríamos que ordenar y
        // podar el array aqui
        int totalLevel = 0;
        for(int level : levels){
            totalLevel += level;
        }
        return totalLevel/levels.length;
    }

    public TreeMap<Integer, Integer> getLevelHistogram(){
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        // Ordenamos la lista
        //Collections.sort(readings, new WifiAPLectureComparator());

        for (Reading reading : readings) {
            if (map.containsKey(reading.getLevel())) {
                map.put(reading.getLevel(), map.get(reading.getLevel())+1);
            }else{
                map.put(reading.getLevel(), 1);
            }
        }

        LevelMapComparator lmc = new LevelMapComparator(map);
        TreeMap sorted_map = new TreeMap(lmc);


        sorted_map.putAll(map);
        System.out.println("results: " + sorted_map);

        return sorted_map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SSID);
        dest.writeString(BSSID);
    }

    protected WifiAP(Parcel in) {
        SSID = in.readString();
        BSSID = in.readString();
    }

    public static final Creator<WifiAP> CREATOR = new Creator<WifiAP>() {
        @Override
        public WifiAP createFromParcel(Parcel in) {
            return new WifiAP(in);
        }

        @Override
        public WifiAP[] newArray(int size) {
            return new WifiAP[size];
        }
    };
}
