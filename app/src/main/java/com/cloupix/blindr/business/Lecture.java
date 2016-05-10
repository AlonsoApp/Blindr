package com.cloupix.blindr.business;

import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alonsousa on 6/12/15.
 * Clase con la que trabajaremos en la app para poder tener control sobre nuestras funciones.
 * Los datpos están almacenados en el atributo ScanResult (es un atributo y no la clase padre porque
 * ScanResult no tiene un constructor público)
 *
 */
public class Lecture implements Parcelable{

    /**
     * ID of the lecture in the DataBase
     */
    private long lectureId;
    /**
     * If the Lecture has been Empirically taken or Mathematically generated
     */
    private boolean mathGenerated;
    /**
     * The address of the access point.
     */
    private String BSSID;
    /**
     * The detected signal level in dBm, also known as the RSSI.
     *
     * <p>Use {@link android.net.wifi.WifiManager#calculateSignalLevel} to convert this number into
     * an absolute signal level which can be displayed to a user.
     */
    private int level;
    /**
     * The primary 20 MHz frequency (in MHz) of the channel over which the client is communicating
     * with the access point.
     */
    private int frequency;
    /**
     * The ID of the Sector this Lecture is attached to
     */
    private long sectorId;
    /**
     * timestamp in microseconds (since boot) when
     * this result was last seen.
     */
    private long timestamp;

    public Lecture() {
    }
    public Lecture(ScanResult scanResult) {
        this.mathGenerated = false;
        setScanResult(scanResult);
    }

    public Lecture(ScanResult scanResult, long sectorId) {
        this.mathGenerated = false;
        setScanResult(scanResult);
        this.sectorId = sectorId;
    }

    public Lecture(ScanResult scanResult, long sectorId, long lectureId) {
        this.mathGenerated = false;
        setScanResult(scanResult);
        this.sectorId = sectorId;
        this.lectureId = lectureId;
    }

    public Lecture(boolean mathGenerated, String BSSID, int level, int frequency, long sectorId, long timestamp) {
        this.mathGenerated = mathGenerated;
        this.BSSID = BSSID;
        this.level = level;
        this.frequency = frequency;
        this.sectorId = sectorId;
        this.timestamp = timestamp;
    }

    public Lecture(long lectureId, boolean mathGenerated, String BSSID, int level, int frequency, long sectorId) {
        this.lectureId = lectureId;
        this.mathGenerated = mathGenerated;
        this.BSSID = BSSID;
        this.level = level;
        this.frequency = frequency;
        this.sectorId = sectorId;
    }

    public void setScanResult(ScanResult scanResult) {
        this.BSSID = scanResult.BSSID;
        this.level = scanResult.level;
        this.frequency = scanResult.frequency;
        this.timestamp = scanResult.timestamp;
    }

    public long getLectureId() {
        return lectureId;
    }

    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    public boolean isMathGenerated() {
        return mathGenerated;
    }

    public void setMathGenerated(boolean mathGenerated) {
        this.mathGenerated = mathGenerated;
    }

    public void setMathGenerated(int mathGenerated) {
        this.mathGenerated = mathGenerated>0;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public long getSectorId() {
        return sectorId;
    }

    public void setSectorId(long sectorId) {
        this.sectorId = sectorId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Parcelling part

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(lectureId);
        dest.writeByte((byte) (mathGenerated ? 1 : 0));
        dest.writeString(BSSID);
        dest.writeInt(level);
        dest.writeInt(frequency);
        dest.writeLong(sectorId);
        dest.writeLong(timestamp);
    }

    protected Lecture(Parcel in) {
        lectureId = in.readLong();
        mathGenerated = in.readByte() != 0;
        BSSID = in.readString();
        level = in.readInt();
        frequency = in.readInt();
        sectorId = in.readLong();
        timestamp = in.readLong();
    }

    public static final Creator<Lecture> CREATOR = new Creator<Lecture>() {
        @Override
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        @Override
        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };
}
