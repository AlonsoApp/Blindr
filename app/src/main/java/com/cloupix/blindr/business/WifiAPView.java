package com.cloupix.blindr.business;

import android.os.Parcel;

import com.cloupix.blindr.R;

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
    private long wifiAPSectorId;
    /**
     * defines if this object must be deleted from the db
     */
    private boolean deleteDBEntity;
    /**
     * defines if this object is new a must be created on the db
     */
    private boolean newDBEntity = true;

    public WifiAPView() {
        backgroundCircle = 0;
        apNumber = 1;
    }

    public WifiAPView(String ssid, String BSSID, int backgroundCircle, int apNumber) {
        super(ssid, BSSID);
        this.backgroundCircle = backgroundCircle;
        this.apNumber = apNumber;
    }

    public WifiAPView(String ssid, String BSSID, ArrayList<Reading> readings, int backgroundCircle, int apNumber) {
        super(ssid, BSSID, readings);
        this.backgroundCircle = backgroundCircle;
        this.apNumber = apNumber;
    }


    public WifiAPView(String ssid, String BSSID, Reading reading, int backgroundCircle, int apNumber) {
        super(ssid, BSSID, reading);
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
        super.setReadings(wifiAP.getReadings());
    }

    public long getWifiAPViewId() {
        return wifiAPViewId;
    }

    public void setWifiAPViewId(long wifiAPViewId) {
        this.wifiAPViewId = wifiAPViewId;
    }

    public void setWifiAPSectorId(long wifiAPSectorId) {
        this.wifiAPSectorId = wifiAPSectorId;
    }

    public long getWifiAPSectorId() {
        return wifiAPSectorId;
    }

    public boolean isDeleteDBEntity() {
        return deleteDBEntity;
    }

    public void setDeleteDBEntity(boolean deleteDBEntity) {
        this.deleteDBEntity = deleteDBEntity;
    }

    public boolean isNewDBEntity() {
        return newDBEntity;
    }

    public void setNewDBEntity(boolean newDBEntity) {
        this.newDBEntity = newDBEntity;
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
        dest.writeLong(wifiAPSectorId);
        dest.writeByte((byte) (deleteDBEntity ? 1 : 0));
        dest.writeByte((byte) (newDBEntity ? 1 : 0));
    }

    protected WifiAPView(Parcel in) {
        super(in);
        wifiAPViewId = in.readLong();
        backgroundCircle = in.readInt();
        apNumber = in.readInt();
        wifiAPSectorId = in.readLong();
        deleteDBEntity = in.readByte() != 0;
        newDBEntity = in.readByte() != 0;
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
