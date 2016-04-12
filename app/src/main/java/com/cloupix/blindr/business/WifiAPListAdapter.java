package com.cloupix.blindr.business;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.logic.LineChartLogic;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by AlonsoUSA on 12/5/15.
 *
 */
public class WifiAPListAdapter extends BaseAdapter {

    private ArrayList<WifiAPRow> listWifiAPRow;
    private LayoutInflater mInflater;
    private Context context;

    public WifiAPListAdapter(Context context) {
        this.context = context;
        this.listWifiAPRow = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        this.listWifiAPRow = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return listWifiAPRow.size();
    }

    @Override
    public Object getItem(int position) {
        return listWifiAPRow.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.row_wifi_ap, null);
            holder = new ViewHolder();
            holder.textViewSSID = (TextView) convertView.findViewById(R.id.textViewSSID);
            holder.textViewBSSID = (TextView) convertView.findViewById(R.id.textViewBSSID);
            holder.textViewRSSIValue = (TextView) convertView.findViewById(R.id.textViewRSSIValue);
            holder.textViewApNumber = (TextView) convertView.findViewById(R.id.textViewApNumber);
            holder.imgViewCircle = (ImageView) convertView.findViewById(R.id.imgViewCircle);
            holder.lineChart = (LineChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        WifiAPRow wifiApRow = listWifiAPRow.get(position);

        holder.textViewSSID.setText(wifiApRow.getSSID());
        holder.textViewBSSID.setText(wifiApRow.getBSSID());
        holder.textViewApNumber.setText(Integer.toString(wifiApRow.getApNumber()));
        holder.imgViewCircle.setImageResource(wifiApRow.getImgCircleRes());

        ArrayList<WifiAPLecture> wifiAPLectures = wifiApRow.getLectures();
        if(wifiAPLectures!=null && !wifiAPLectures.isEmpty()) {
            WifiAPLecture lecture = wifiAPLectures.get(wifiAPLectures.size()-1);
            holder.textViewRSSIValue.setText(lecture.getScanResult().level + "dB");


            // Chart
            LineChartLogic lineChartLogic = new LineChartLogic();
            lineChartLogic.setupChart(holder.lineChart, wifiApRow);

        }
        return convertView;
    }

    static class ViewHolder {
        TextView textViewSSID, textViewBSSID, textViewRSSIValue, textViewApNumber;
        ImageView imgViewCircle;
        LineChart lineChart;
    }

    /*
    public void addWifiAP(WifiAP wifiAP) {
        boolean exists = false;
        for (WifiAPRow cell : listWifiAPRow) {
            // List.Cell.Mac = NewWifi.Mac
            if(cell.getMac().equals(wifiAP.getMac())){
                cell.addWifiAPLectures(wifiAP.getLectures());
                exists = true;
                break;
            }
        }
        // Si no existía lo creamos y le asignamos yn numero y un color de circulo
        if(!exists) {
            // TODO Echar un opjo a esto a ver si da lo que tiene que dar
            int circleRes = WifiAPRow.allCircleRes[listWifiAPRow.size() % WifiAPRow.allCircleRes.length];
            WifiAPRow newCell = new WifiAPRow(wifiAP.getSsid(), wifiAP.getMac(), wifiAP.getLectures(), circleRes, listWifiAPRow.size()+1);
            listWifiAPRow.add(newCell);
        }

    }
    */


    public void addScanResultList(List<ScanResult> scanResultList) {

        for(ScanResult scanResult : scanResultList) {
            // Creamos el envelope
            WifiAPLecture wifiAPLecture = new WifiAPLecture(scanResult);


            boolean exists = false;
            for (WifiAPRow cell : listWifiAPRow) {
                // List.Cell.Mac = NewWifi.Mac
                if(cell.getBSSID().equals(scanResult.BSSID)){

                    // Lo metemos en la lista de lecturas de la celda
                    cell.addWifiAPLecture(wifiAPLecture);
                    exists = true;
                    break;
                }
            }
            // Si no existía lo creamos y le asignamos un numero y un color de circulo
            if(!exists) {
                // TODO Echar un opjo a esto a ver si da lo que tiene que dar
                int circleRes = WifiAPRow.allCircleRes[listWifiAPRow.size() % WifiAPRow.allCircleRes.length];
                // Aqui es donde sacamos todo lo que queremos del scanResult
                WifiAPRow newRow = new WifiAPRow(scanResult.SSID, scanResult.BSSID, wifiAPLecture, circleRes, listWifiAPRow.size()+1);
                listWifiAPRow.add(newRow);
            }
        }


        // Ordenamos la lista
        Collections.sort(listWifiAPRow, new WifiAPComparator());

    }

    public String getBSSI(int position){
        return listWifiAPRow.get(position).getBSSID();
    }

}
