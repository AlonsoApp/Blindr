package com.cloupix.blindr.business.adapters;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloupix.blindr.R;
import com.cloupix.blindr.business.Lecture;
import com.cloupix.blindr.business.comparators.WifiAPComparator;
import com.cloupix.blindr.business.WifiAPView;
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

    private ArrayList<WifiAPView> listWifiAPView;
    private LayoutInflater mInflater;
    private Context context;

    public WifiAPListAdapter(Context context) {
        this.context = context;
        this.listWifiAPView = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listWifiAPView.size();
    }

    @Override
    public Object getItem(int position) {
        return listWifiAPView.get(position);
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

        WifiAPView wifiApView = listWifiAPView.get(position);

        holder.textViewSSID.setText(wifiApView.getSSID());
        holder.textViewBSSID.setText(wifiApView.getBSSID());
        holder.textViewApNumber.setText(Integer.toString(wifiApView.getApNumber()));
        holder.imgViewCircle.setImageResource(wifiApView.getBackgroundCircleRes());

        ArrayList<Lecture> lectures = wifiApView.getLectures();
        if(lectures !=null && !lectures.isEmpty()) {
            Lecture lecture = lectures.get(lectures.size()-1);
            holder.textViewRSSIValue.setText(lecture.getLevel() + "dB");


            // Chart
            LineChartLogic lineChartLogic = new LineChartLogic();
            lineChartLogic.setupChart(holder.lineChart, wifiApView);

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
        for (WifiAPView cell : listWifiAPView) {
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
            int circleRes = WifiAPView.allCircleNames[listWifiAPView.size() % WifiAPView.allCircleNames.length];
            WifiAPView newCell = new WifiAPView(wifiAP.getSsid(), wifiAP.getMac(), wifiAP.getLectures(), circleRes, listWifiAPView.size()+1);
            listWifiAPView.add(newCell);
        }

    }
    */


    public void addScanResultList(List<ScanResult> scanResultList) {

        for(ScanResult scanResult : scanResultList) {
            // Creamos el envelope
            Lecture lecture = new Lecture(scanResult);


            boolean exists = false;
            for (WifiAPView cell : listWifiAPView) {
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
                // TODO Echar un opjo a esto a ver si da lo que tiene que dar
                int backgroundCircle = listWifiAPView.size() - (WifiAPView.allCircleResources.length*(listWifiAPView.size() / WifiAPView.allCircleResources.length));
                // Aqui es donde sacamos todo lo que queremos del scanResult
                WifiAPView newRow = new WifiAPView(scanResult.SSID, scanResult.BSSID, lecture, backgroundCircle, listWifiAPView.size()+1);
                listWifiAPView.add(newRow);
            }
        }


        // Ordenamos la lista
        Collections.sort(listWifiAPView, new WifiAPComparator());

    }

    public String getBSSI(int position){
        return listWifiAPView.get(position).getBSSID();
    }

}
