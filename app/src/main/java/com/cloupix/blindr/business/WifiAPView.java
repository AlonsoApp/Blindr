package com.cloupix.blindr.business;

import android.graphics.drawable.Drawable;

import com.cloupix.blindr.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by alonsousa on 6/12/15.
 *
 */
// This is a git example
public class WifiAPView extends WifiAP {

    public static final String[] allCircleNames = new String[]{
            "purple_circle",
            "green_circle",
            "red_circle",
            "amber_circle",
            "indigo_circle",
            "pink_circle",
    };

    public static final int multipleAPCircleRes = R.mipmap.grey_circle;

    private long wifiAPViewId;
    private String backgroundImage;
    private int apNumber;

    public WifiAPView() {
        backgroundImage = "purple_circle";
        apNumber = 1;
    }

    public WifiAPView(String ssid, String BSSID, String backgroundImage, int apNumber) {
        super(ssid, BSSID);
        this.backgroundImage = backgroundImage;
        this.apNumber = apNumber;
    }

    public WifiAPView(String ssid, String BSSID, ArrayList<Lecture> lectures, String backgroundImage, int apNumber) {
        super(ssid, BSSID, lectures);
        this.backgroundImage = backgroundImage;
        this.apNumber = apNumber;
    }


    public WifiAPView(String ssid, String BSSID, Lecture lecture, String backgroundImage, int apNumber) {
        super(ssid, BSSID, lecture);
        this.backgroundImage = backgroundImage;
        this.apNumber = apNumber;
    }

    public int getImgCircleRes() {
        return getResId(this.backgroundImage, Drawable.class);
    }

    public int getApNumber() {
        return apNumber;
    }

    public void setApNumber(int apNumber) {
        this.apNumber = apNumber;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void mergeWifiAP(WifiAP wifiAP) {
        super.setSSID(wifiAP.getSSID());
        super.setBSSID(wifiAP.getBSSID());
        super.setLectures(wifiAP.getLectures());
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long getWifiAPViewId() {
        return wifiAPViewId;
    }

    public void setWifiAPViewId(long wifiAPViewId) {
        this.wifiAPViewId = wifiAPViewId;
    }
}
