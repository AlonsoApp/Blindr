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
public class Reading implements Parcelable{

    /**
     * ID of the lecture in the DataBase
     */
    private long readingId;
    /**
     * If the Reading has been Empirically taken or Mathematically generated
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
     * The ID of the Sector this Reading is attached to
     */
    private long sectorId;
    /**
     * timestamp in microseconds (since boot) when
     * this result was last seen.
     */
    private long timestamp;
    /**
     * defines if this object must be deleted from the db
     */
    private boolean deleteDBEntity;
    /**
     * defines if this object is new a must be created on the db
     */
    private boolean newDBEntity = true;

    public Reading() {
    }
    public Reading(ScanResult scanResult) {
        this.mathGenerated = false;
        setScanResult(scanResult);
    }

    public Reading(ScanResult scanResult, long sectorId) {
        this.mathGenerated = false;
        setScanResult(scanResult);
        this.sectorId = sectorId;
    }

    public Reading(ScanResult scanResult, long sectorId, long readingId) {
        this.mathGenerated = false;
        setScanResult(scanResult);
        this.sectorId = sectorId;
        this.readingId = readingId;
    }

    public Reading(boolean mathGenerated, String BSSID, int level, int frequency, long sectorId, long timestamp) {
        this.mathGenerated = mathGenerated;
        this.BSSID = BSSID;
        this.level = level;
        this.frequency = frequency;
        this.sectorId = sectorId;
        this.timestamp = timestamp;
    }

    public Reading(long readingId, boolean mathGenerated, String BSSID, int level, int frequency, long sectorId) {
        this.readingId = readingId;
        this.mathGenerated = mathGenerated;
        this.BSSID = BSSID;
        this.level = level;
        this.frequency = frequency;
        this.sectorId = sectorId;
    }

    public Reading(long readingId, boolean mathGenerated, String BSSID, int level, int frequency, long sectorId, long timestamp) {
        this.readingId = readingId;
        this.mathGenerated = mathGenerated;
        this.BSSID = BSSID;
        this.level = level;
        this.frequency = frequency;
        this.sectorId = sectorId;
        this.timestamp = timestamp;
    }

    public void setScanResult(ScanResult scanResult) {
        this.BSSID = scanResult.BSSID;
        this.level = scanResult.level;
        this.frequency = scanResult.frequency;
        this.timestamp = scanResult.timestamp;
    }

    public long getReadingId() {
        return readingId;
    }

    public void setReadingId(long readingId) {
        this.readingId = readingId;
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

    // Parcelling part

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(readingId);
        dest.writeByte((byte) (mathGenerated ? 1 : 0));
        dest.writeString(BSSID);
        dest.writeInt(level);
        dest.writeInt(frequency);
        dest.writeLong(sectorId);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (deleteDBEntity ? 1 : 0));
        dest.writeByte((byte) (newDBEntity ? 1 : 0));
    }

    protected Reading(Parcel in) {
        readingId = in.readLong();
        mathGenerated = in.readByte() != 0;
        BSSID = in.readString();
        level = in.readInt();
        frequency = in.readInt();
        sectorId = in.readLong();
        timestamp = in.readLong();
        deleteDBEntity = in.readByte() != 0;
        newDBEntity = in.readByte() != 0;
    }

    public static final Creator<Reading> CREATOR = new Creator<Reading>() {
        @Override
        public Reading createFromParcel(Parcel in) {
            return new Reading(in);
        }

        @Override
        public Reading[] newArray(int size) {
            return new Reading[size];
        }
    };
}
