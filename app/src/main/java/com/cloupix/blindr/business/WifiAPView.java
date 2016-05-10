package com.cloupix.blindr.business;

import android.graphics.drawable.Drawable;
import android.os.Parcel;

import com.cloupix.blindr.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by alonsousa on 6/12/15.
 *
 */
// This is a git example
public class WifiAPView extends WifiAP {

    public static final int[] allCircleResources = new int[]{
            R.mipmap.purple_circle,
            R.mipmap.green_circle,
            R.mipmap.red_circle,
            R.mipmap.amber_circle,
            R.mipmap.indigo_circle,
            R.mipmap.pink_circle
    };

    public static final int multipleAPCircleRes = R.mipmap.grey_circle;

    private long wifiAPViewId;
    private int backgroundCircle;
    private int apNumber;

    public WifiAPView() {
        backgroundCircle = 0;
        apNumber = 1;
    }

    public WifiAPView(String ssid, String BSSID, int backgroundCircle, int apNumber) {
        super(ssid, BSSID);
        this.backgroundCircle = backgroundCircle;
        this.apNumber = apNumber;
    }

    public WifiAPView(String ssid, String BSSID, ArrayList<Lecture> lectures, int backgroundCircle, int apNumber) {
        super(ssid, BSSID, lectures);
        this.backgroundCircle = backgroundCircle;
        this.apNumber = apNumber;
    }


    public WifiAPView(String ssid, String BSSID, Lecture lecture, int backgroundCircle, int apNumber) {
        super(ssid, BSSID, lecture);
        this.backgroundCircle = backgroundCircle;
        this.apNumber = apNumber;
    }

    public int getBackgroundCircleRes() {
        return allCircleResources[backgroundCircle];
    }

    public int getApNumber() {
        return apNumber;
    }

    public void setApNumber(int apNumber) {
        this.apNumber = apNumber;
    }

    public int getBackgroundCircle() {
        return backgroundCircle;
    }

    public void setBackgroundCircle(int backgroundCircle) {
        this.backgroundCircle = backgroundCircle;
    }

    public void mergeWifiAP(WifiAP wifiAP) {
        super.setSSID(wifiAP.getSSID());
        super.setBSSID(wifiAP.getBSSID());
        super.setLectures(wifiAP.getLectures());
    }

    public long getWifiAPViewId() {
        return wifiAPViewId;
    }

    public void setWifiAPViewId(long wifiAPViewId) {
        this.wifiAPViewId = wifiAPViewId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(wifiAPViewId);
        dest.writeInt(backgroundCircle);
        dest.writeInt(apNumber);
    }

    protected WifiAPView(Parcel in) {
        super(in);
        wifiAPViewId = in.readLong();
        backgroundCircle = in.readInt();
        apNumber = in.readInt();
    }

    public static final Creator<WifiAPView> CREATOR = new Creator<WifiAPView>() {
        @Override
        public WifiAPView createFromParcel(Parcel in) {
            return new WifiAPView(in);
        }

        @Override
        public WifiAPView[] newArray(int size) {
            return new WifiAPView[size];
        }
    };
}
