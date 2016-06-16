package com.cloupix.blindr.business;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.cloupix.blindr.R;

/**
 * Created by alonsoapp on 23/04/16.
 *
 */
public class SectorView extends Sector {


    private boolean nStroke, eStroke, sStroke, wStroke, nwseStroke, neswStroke;

    /**
     * TODO Borrar esto del modelo de datos
     * This parameter will be deprecated as the functionality it provides will be replaced by the
     * amount of readings.
     * */
    private boolean scanned;
    private long sectorViewId;

    public SectorView() {
        this.nStroke = false;
        this.eStroke = false;
        this.sStroke = false;
        this.wStroke = false;
        this.nwseStroke = false;
        this.neswStroke = false;
        this.scanned = false;
    }

    public SectorView(long sectorId) {
        super(sectorId);
        this.scanned = false;
        this.nStroke = false;
        this.eStroke = false;
        this.sStroke = false;
        this.wStroke = false;
        this.nwseStroke = false;
        this.neswStroke = false;
    }

    public SectorView(long sectorId, boolean nStroke, boolean eStroke, boolean sStroke, boolean wStroke, boolean nwseStroke, boolean neswStroke, boolean scanned) {
        super(sectorId);
        this.nStroke = nStroke;
        this.eStroke = eStroke;
        this.sStroke = sStroke;
        this.wStroke = wStroke;
        this.nwseStroke = nwseStroke;
        this.neswStroke = neswStroke;
        this.scanned = scanned;
    }

    // Seguramente aqui tengamos que poner lo de completado o no. Esta clase es solo visual,
    // aqui no guardamso nada de info (que yo recuerde al menos, ya veremos que ando un poco
    // perdido que hace 4 meeses que no toco esto)


    public boolean isnStroke() {
        return nStroke;
    }

    public void setnStroke(boolean nStroke) {
        this.nStroke = nStroke;
    }

    public boolean iseStroke() {
        return eStroke;
    }

    public void seteStroke(boolean eStroke) {
        this.eStroke = eStroke;
    }

    public boolean issStroke() {
        return sStroke;
    }

    public void setsStroke(boolean sStroke) {
        this.sStroke = sStroke;
    }

    public boolean iswStroke() {
        return wStroke;
    }

    public void setwStroke(boolean wStroke) {
        this.wStroke = wStroke;
    }

    public boolean isNwseStroke() {
        return nwseStroke;
    }

    public void setNwseStroke(boolean nwseStroke) {
        this.nwseStroke = nwseStroke;
    }

    public boolean isNeswStroke() {
        return neswStroke;
    }

    public void setNeswStroke(boolean neswStroke) {
        this.neswStroke = neswStroke;
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    public void setScanned(int scanned) {
        this.scanned = scanned>0;
    }

    public long getSectorViewId() {
        return sectorViewId;
    }

    public void setSectorViewId(long sectorViewId) {
        this.sectorViewId = sectorViewId;
    }

    public void addWifiAPWithCheck(WifiAP newWifiAP){
        // Si ya esta en la lista no lo volvemso a meter
        for(WifiAP wifiAP: wifiAPs){
            if(TextUtils.equals(wifiAP.getBSSID(), newWifiAP.getBSSID()))
                if(wifiAP instanceof WifiAPView)
                    if(!((WifiAPView)wifiAP).isDeleteDBEntity())
                        return;
                else
                    return;
        }
        wifiAPs.add(newWifiAP);
    }

    public int getLocationProbabilityColorRes(double maxProb){
        double RED_THRESHOLD = maxProb;
        double ORANGE_THRESHOLD = (maxProb/4)*3;
        double YELLOW_THRESHOLD = (maxProb/4)*2;
        double CREAM_THRESHOLD = (maxProb/4)*1;

        double locProb = super.getLocationProbability();
        if(locProb>=RED_THRESHOLD){
            return R.color.location_red;
        }else if(locProb>=ORANGE_THRESHOLD){
            return R.color.location_orange;
        }else if(locProb>=YELLOW_THRESHOLD){
            return R.color.location_yellow;
        }else if(locProb>CREAM_THRESHOLD){
            return R.color.location_cream;
        }else{
            return android.R.color.transparent;
        }
    }

    public void mergeSector(Sector sector) {
        super.setSectorId(sector.getSectorId());
        super.setListN(sector.getListN());
        super.setMatrixX(sector.getMatrixX());
        super.setMatrixY(sector.getMatrixY());
        super.setMapId(sector.getMapId());
        super.setLatitude(sector.getLatitude());
        super.setLongitude(sector.getLongitude());
    }

    public static final Parcelable.Creator<SectorView> CREATOR = new Parcelable.Creator<SectorView>() {
        public SectorView createFromParcel(Parcel in) {
            return new SectorView(in);
        }

        public SectorView[] newArray(int size) {
            return new SectorView[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (nStroke ? 1 : 0));
        dest.writeByte((byte) (eStroke ? 1 : 0));
        dest.writeByte((byte) (sStroke ? 1 : 0));
        dest.writeByte((byte) (wStroke ? 1 : 0));
        dest.writeByte((byte) (nwseStroke ? 1 : 0));
        dest.writeByte((byte) (neswStroke ? 1 : 0));
        dest.writeByte((byte) (scanned ? 1 : 0));
        dest.writeLong(sectorViewId);
    }

    private SectorView(Parcel in) {
        super(in);
        nStroke = in.readByte() != 0;
        eStroke = in.readByte() != 0;
        sStroke = in.readByte() != 0;
        wStroke = in.readByte() != 0;
        nwseStroke = in.readByte() != 0;
        neswStroke = in.readByte() != 0;
        scanned = in.readByte() != 0;
        sectorViewId = in.readLong();
    }

}
