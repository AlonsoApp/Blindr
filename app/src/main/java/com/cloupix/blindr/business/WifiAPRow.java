package com.cloupix.blindr.business;

import com.cloupix.blindr.R;

import java.util.ArrayList;

/**
 * Created by alonsousa on 6/12/15.
 *
 */
public class WifiAPRow extends WifiAP {

    public static final int[] allCircleRes = new int[]{
            R.mipmap.purple_circle,
            R.mipmap.green_circle,
            R.mipmap.red_circle,
            R.mipmap.amber_circle,
            R.mipmap.indigo_circle,
            R.mipmap.pink_circle,
    };

    private int imgCircleRes;
    private int apNumber;


    public WifiAPRow() {
        imgCircleRes = R.mipmap.purple_circle;
        apNumber = 1;
    }

    public WifiAPRow(String ssid, String BSSID, int imgCircleRes, int apNumber) {
        super(ssid, BSSID);
        this.imgCircleRes = imgCircleRes;
        this.apNumber = apNumber;
    }

    public WifiAPRow(String ssid, String BSSID, ArrayList<WifiAPLecture> lectures, int imgCircleRes, int apNumber) {
        super(ssid, BSSID, lectures);
        this.imgCircleRes = imgCircleRes;
        this.apNumber = apNumber;
    }


    public WifiAPRow(String ssid, String BSSID, WifiAPLecture lecture, int imgCircleRes, int apNumber) {
        super(ssid, BSSID, lecture);
        this.imgCircleRes = imgCircleRes;
        this.apNumber = apNumber;
    }

    public int getImgCircleRes() {
        return imgCircleRes;
    }

    public void setImgCircleRes(int imgCircleRes) {
        this.imgCircleRes = imgCircleRes;
    }

    public int getApNumber() {
        return apNumber;
    }

    public void setApNumber(int apNumber) {
        this.apNumber = apNumber;
    }
}
