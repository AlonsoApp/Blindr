package com.cloupix.blindr.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by alonsousa on 6/12/15.
 *
 */
public class WifiAP {


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

    private ArrayList<Lecture> lectures;

    public WifiAP() {
        this.lectures = new ArrayList<Lecture>();
    }

    public WifiAP(String SSID, String BSSID) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.lectures = new ArrayList<Lecture>();
    }

    public WifiAP(String SSID, String BSSID, ArrayList<Lecture> lectures) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.lectures = lectures;
    }

    public WifiAP(String SSID, String BSSID, final Lecture lecture) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.lectures = new ArrayList<Lecture>(){{add(lecture);}};
    }

    public String getSSID() {
        return SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public ArrayList<Lecture> getLectures() {
        return lectures;
    }

    public void setSSID(String ssid) {
        this.SSID = ssid;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public void setLectures(ArrayList<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void addWifiAPLecture(Lecture lecture) {
        lectures.add(lecture);
    }

    public void addWifiAPLectures(ArrayList<Lecture> lectures) {
        lectures.addAll(lectures);
    }

    public int getAverageRSSI(){
        int[] levels = new int[lectures.size()];
        for (int i = 0; i<lectures.size(); i++){
            levels[i] = lectures.get(i).getLevel();
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
        //Collections.sort(lectures, new WifiAPLectureComparator());

        for (Lecture lecture : lectures) {
            if (map.containsKey(lecture.getLevel())) {
                map.put(lecture.getLevel(), map.get(lecture.getLevel())+1);
            }else{
                map.put(lecture.getLevel(), 1);
            }
        }

        LevelMapComparator lmc = new LevelMapComparator(map);
        TreeMap sorted_map = new TreeMap(lmc);


        sorted_map.putAll(map);
        System.out.println("results: " + sorted_map);

        return sorted_map;
    }
}
