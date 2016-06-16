package com.cloupix.blindr.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.cloupix.blindr.R;

import java.util.List;

/**
 * Created by alonsoapp on 11/04/16.
 *
 */
public class WifiLogic {

    public static final int SCAN_LOOP_INIFINITE = -1;

    private Context context;
    private WifiManager wifi;

    private int scanLoopCounter = 0;

    private BroadcastReceiver br;

    public WifiLogic(Context context){
        this.context = context;
        wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public void startScan(final WifiLogicScannCallbacks wifiLogicScannCallbacks, final int scanLoopTimes){


        if (!wifi.isWifiEnabled())
        {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.wifi_disabled), Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        //this.adapter = new SimpleAdapter(WiFiDemo.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        //lv.setAdapter(this.adapter);


        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                try {

                    wifiLogicScannCallbacks.onReceive(wifi.getScanResults(), scanLoopCounter);

                    if(scanLoopCounter<scanLoopTimes || scanLoopTimes == SCAN_LOOP_INIFINITE) {
                        wifi.startScan();
                        scanLoopCounter++;
                    }else{
                        wifiLogicScannCallbacks.onScanFinished();
                        context.unregisterReceiver(br);
                        scanLoopCounter = 0;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };


        context.registerReceiver( br, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifi.startScan();
    }

    public void stopScan(){
        try{
            context.unregisterReceiver(br);
        }catch (IllegalArgumentException e){

        }
    }


    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface WifiLogicScannCallbacks {

        void onReceive(List<ScanResult> results, int loopCounter);
        void onScanFinished();
    }
}
