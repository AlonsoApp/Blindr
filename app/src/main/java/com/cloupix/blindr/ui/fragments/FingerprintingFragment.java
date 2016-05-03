package com.cloupix.blindr.ui.fragments;

import android.app.Fragment;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.adapters.GridAdapter;
import com.cloupix.blindr.business.Lecture;
import com.cloupix.blindr.business.WifiAP;
import com.cloupix.blindr.logic.WifiLogic;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by alonsousa on 15/12/15.
 *
 */
public class FingerprintingFragment extends Fragment implements AdapterView.OnItemClickListener, WifiLogic.WifiLogicScannCallbacks {

    public static final String TAG = "fragment_photo";

    private GridAdapter gridAdapter;

    private ProgressBar scanProgressBar;

    private int lecturesPerScan = 5;

    private WifiLogic wifiLogic;

    private ArrayList<WifiAP>[] wifiApMatrix;
    private int scanningPosition = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int matrixSize = getActivity().getResources().getInteger(R.integer.sector_height) * getActivity().getResources().getInteger(R.integer.sector_width);
        wifiApMatrix = new ArrayList[matrixSize];
    }

    @Override
    public void onStop() {
        super.onStop();
        if (wifiLogic !=null)
            wifiLogic.stopScan();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fingerprint, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridAdapter = new GridAdapter(getActivity());
        gridview.setAdapter(gridAdapter);

        gridview.setOnItemClickListener(this);

        scanProgressBar = (ProgressBar) rootView.findViewById(R.id.scanProgressBar);
        scanProgressBar.setMax(lecturesPerScan);


        return rootView;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        // Método que se ejecuta cuando pulsas un sector (un cuadradito).
        // Aqui hay que guardad (o hacer y guardar) la lectura de los wifis que vea en ese momento con todos sus datos
        // Recomiendo. Poner una barra de progreso mientras (durante un tiempo determinado) se recopilan muestras de los diferentes APs y se marque la casilla con un tick

        Snackbar.make(getView(), position + "", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        /*
        Aqui vamos a:
        2. Arrancar el listener de lecturas de wifi y almacenarlas

         */

        // TODO Comprobar si el anterior ha terminado, si no ha termiando, paramos el scanner antes

        scanningPosition = position;

        if(wifiLogic == null)
            wifiLogic = new WifiLogic(getActivity());

        scanProgressBar.setVisibility(View.VISIBLE);
        wifiLogic.startScan(this, lecturesPerScan);


    }


    @Override
    public void onReceive(List<ScanResult> results, int loopCounter) {
        scanProgressBar.setProgress(loopCounter+1);
        addScanResultList(results);
    }

    @Override
    public void onScanFinished() {
        // Pintamos de verde y quitamos el progres bar y guardamos
        scanProgressBar.setVisibility(View.INVISIBLE);
        scanProgressBar.setProgress(0);
        gridAdapter.setSectorComplete(scanningPosition);
    }


    // Este metodo coge una lista de APs del ultimo escaneo y mete las lecturas nuevas en los
    // WifiAPs de la lista que ya estaban creados en este punto y crea nuevos WifiAPs si la lectura
    // corresponde a un AP que no estaba previamente
    public void addScanResultList(List<ScanResult> scanResultList) {

        for(ScanResult scanResult : scanResultList) {
            // Creamos el envelope
            Lecture lecture = new Lecture(scanResult);

            if(wifiApMatrix[scanningPosition]== null)
                wifiApMatrix[scanningPosition] = new ArrayList<WifiAP>();

            boolean exists = false;
            for (WifiAP cell : wifiApMatrix[scanningPosition]) {
                // List.Cell.Mac = NewWifi.Mac
                if(cell.getBSSID().equals(scanResult.BSSID)){

                    // Lo metemos en la lista de lecturas de la celda
                    cell.addWifiAPLecture(lecture);
                    exists = true;
                    break;
                }
            }
            // Si no existía lo creamos y le asignamos un numero y un color de circulo
            if(!exists) {
                // Aqui es donde sacamos to_do lo que queremos del scanResult
                WifiAP newAP = new WifiAP(scanResult.SSID, scanResult.BSSID, lecture);
                wifiApMatrix[scanningPosition].add(newAP);
            }
        }

    }
}