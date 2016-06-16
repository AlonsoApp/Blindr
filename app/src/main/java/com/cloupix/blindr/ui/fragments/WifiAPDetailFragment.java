package com.cloupix.blindr.ui.fragments;

import android.app.Fragment;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.Reading;
import com.cloupix.blindr.business.WifiAP;
import com.cloupix.blindr.logic.BarChartLogic;
import com.cloupix.blindr.logic.LineChartLogic;
import com.cloupix.blindr.logic.WifiLogic;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WifiAPDetailFragment extends Fragment implements WifiLogic.WifiLogicScannCallbacks {

    public static final String ARG_BSSI = "arg_bssi";

    private TextView textViewSSID, textViewBSSID, textViewRSSI, textViewAverageRSSI;
    private LineChart lineChart;
    private BarChart barChart;

    private WifiLogic wifiLogic;
    private LineChartLogic lineChartLogic;
    private BarChartLogic barChartLogic;
    private WifiAP detailedWifiAP;

    public WifiAPDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sacamos los agrs
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String bssi = bundle.getString(ARG_BSSI, "");
            detailedWifiAP = new WifiAP();
            detailedWifiAP.setBSSID(bssi);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wifi_apdetail, container, false);

        textViewSSID = (TextView) rootView.findViewById(R.id.textViewSSID);
        textViewBSSID = (TextView) rootView.findViewById(R.id.textViewBSSID);
        textViewRSSI = (TextView) rootView.findViewById(R.id.textViewRSSI);
        textViewAverageRSSI = (TextView) rootView.findViewById(R.id.textViewAverageRSSI);
        lineChart = (LineChart) rootView.findViewById(R.id.lineChart);
        barChart = (BarChart) rootView.findViewById(R.id.barChart);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(wifiLogic==null)
            wifiLogic = new WifiLogic(getActivity());

        wifiLogic.startScan(this, WifiLogic.SCAN_LOOP_INIFINITE);
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiLogic.stopScan();
    }

    @Override
    public void onReceive(List<ScanResult> results, int loopCounter) {
        addScanResult(results);
        updateUI();
    }

    @Override
    public void onScanFinished() {

    }

    // Buscamos el scanResult con el BSSID que nos interesa y lo añadimos a la lista de Lectures del
    // detailedWifiAP
    public void addScanResult(List<ScanResult> scanResultList) {

        for(ScanResult scanResult : scanResultList) {


            // List.Cell.Mac = NewWifi.Mac
            if(detailedWifiAP.getBSSID().equals(scanResult.BSSID)){


                Reading reading = new Reading(scanResult);

                // Lo metemos en la lista de lecturas de la celda
                detailedWifiAP.addWifiAPReading(reading);

                if(TextUtils.isEmpty(detailedWifiAP.getSSID()))
                    detailedWifiAP.setSSID(scanResult.SSID);
                return;
            }
        }
    }

    private void updateUI(){
        textViewSSID.setText(detailedWifiAP.getSSID());
        textViewBSSID.setText(detailedWifiAP.getBSSID());
        textViewRSSI.setText(detailedWifiAP.getReadings().get(detailedWifiAP.getReadings().size()-1).getLevel() + "dB");
        textViewAverageRSSI.setText(detailedWifiAP.getAverageRSSI() + "dB");

        if(lineChartLogic==null){
            lineChartLogic = new LineChartLogic();
        }

        lineChartLogic.setupChart(lineChart, detailedWifiAP);

        if(barChartLogic == null){
            barChartLogic = new BarChartLogic();
        }

        barChartLogic.setupChart(barChart, detailedWifiAP);
        //lineChart = (LineChart) rootView.findViewById(R.id.lineChart);
        //barChart = (BarChart) rootView.findViewById(R.id.barChart);
    }



}
