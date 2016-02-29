package com.cloupix.blindr.business;

import android.net.wifi.ScanResult;

/**
 * Created by alonsousa on 6/12/15.
 * Clase con la que trabajaremos en la app para poder tener control sobre nuestras funciones.
 * Los datpos están almacenados en el atributo ScanResult (es un atributo y no la clase padre porque
 * ScanResult no tiene un constructor público)
 *
 */
public class WifiAPLecture {


    private ScanResult scanResult;

    public WifiAPLecture(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }
}
